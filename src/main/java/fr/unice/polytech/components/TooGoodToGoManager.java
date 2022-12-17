package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.MailSubscriber;
import fr.unice.polytech.entities.client.NotificationMessage;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.tooGoodToGo.TooGoodToGoBag;
import fr.unice.polytech.exception.OrderStateException;
import fr.unice.polytech.interfaces.MailPublisher;
import fr.unice.polytech.interfaces.OrderFinder;
import fr.unice.polytech.interfaces.TooGoodToGoBagCreator;
import fr.unice.polytech.interfaces.TooGoodToGoBagFinder;
import fr.unice.polytech.repositories.TooGoodToGoBagsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Service to create TooGoodToGo bags from obsolete orders
 * There is one instance of this service per store
 */
@Component
@Scope("prototype")
public class TooGoodToGoManager implements TooGoodToGoBagCreator, TooGoodToGoBagFinder, MailPublisher {
    @Getter
    private final ScheduledThreadPoolExecutor executor;
    private final Collection<MailSubscriber> subscribers = new ArrayList<>();
    private final OrderFinder orderFinder;
    private final TooGoodToGoBagsRepository tooGoodToGoBagsRepository;
    private final Map<Store, ScheduledFuture<?>> scheduledTasks = new HashMap<>();
    private final Clock clock;

    @Autowired
    public TooGoodToGoManager(
            TooGoodToGoBagsRepository tooGoodToGoBagsRepository,
            OrderFinder orderFinder,
            Clock clock,
            ScheduledThreadPoolExecutor tooGoodToGoExecutor
    ) {
        this.tooGoodToGoBagsRepository = tooGoodToGoBagsRepository;
        this.orderFinder = orderFinder;
        this.clock = clock;
        this.executor = tooGoodToGoExecutor;
    }

    @Override
    public void initScheduler(Store store) {
        ScheduledFuture<?> bagTask = executor.scheduleAtFixedRate(
                () -> performOrderRecycling(store),
                0,
                3,
                java.util.concurrent.TimeUnit.HOURS
        );
        scheduledTasks.put(store, bagTask);
    }

    public void performOrderRecycling(Store store) {
        if (LocalTime.now(clock).isAfter(store.getClosingTime())) {
            if (scheduledTasks.containsKey(store)) {
                scheduledTasks.get(store).cancel(true);
                scheduledTasks.remove(store);
            }
            return;
        }
        Collection<Order> obsoleteOrders = checkForObsoleteOrders();
        Collection<TooGoodToGoBag> bags = convertOrders(obsoleteOrders);
        publishBags(bags);
    }

    @Override
    public Collection<Order> checkForObsoleteOrders() {
        return orderFinder.getOrdersWithStatus(OrderStatus.OBSOLETE);
    }

    @Override
    public Collection<TooGoodToGoBag> convertOrders(Collection<Order> orders) {
        if (orders == null)
            return null;
        Collection<TooGoodToGoBag> tooGoodToGoBagCollection = orders.stream()
                     .map(TooGoodToGoBag::new)
                     .toList();
        orders.forEach(order -> {
            try {
                order.setState(OrderStatus.CONVERTED);
            } catch (OrderStateException e) {
                throw new RuntimeException(e);
            }
        });
        publishBags(tooGoodToGoBagCollection);
        return tooGoodToGoBagCollection;
    }

    @Override
    public void publishBags(Collection<TooGoodToGoBag> bags) {
        bags.forEach(tooGoodToGoBag -> tooGoodToGoBagsRepository.save(tooGoodToGoBag, UUID.randomUUID()));
    }

    @Override
    public void notifyClients() {
        subscribers.forEach(client -> client.sendMail(NotificationMessage.TOOGOODTOGO_BAGS_AVAILABLE));
    }

    @Override
    public void addSubscriber(MailSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public Collection<TooGoodToGoBag> getBags(Store store) {
        Collection<TooGoodToGoBag> bags = new ArrayList<>();
        for (TooGoodToGoBag bag : tooGoodToGoBagsRepository.findAll()) {
            if (bag.getStore().equals(store)) {
                bags.add(bag);
            }
        }
        return bags;
    }
}
