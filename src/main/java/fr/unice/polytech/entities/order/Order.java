package fr.unice.polytech.entities.order;

import fr.unice.polytech.entities.client.Cart;
import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.TimeSlot;
import fr.unice.polytech.exception.OrderStateException;
import fr.unice.polytech.repositories.OrderRepository;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Data
public class Order {
    private final List<Item> items;
    public final Store store;
    private UUID id;
    private Client client;
    private final Cook cook;
    private OrderState state;
    private double price;
    private final Map<OrderStatus, Date> history;
    private TimeSlot timeSlot;
    private LocalDateTime pickupTime;
    private OrderRepository orderRepository;

    public Order(UUID id,Client client, Cook cook, Store store, Duration totalCookingTime) {
        this.id=id;
        this.client = client;
        this.cook = cook;
        this.state = new OrderNotStarted(this);
        this.items = List.copyOf(client.getCart().getItems());
        this.history = new HashMap<>();
        this.history.put(OrderStatus.NOT_STARTED, new Date());
        this.store = store;
        Cart cart = client.getCart();
        this.pickupTime = cart.getPickupTime();
        this.price = cart.getTotal();
        this.timeSlot = new TimeSlot(pickupTime.minus(totalCookingTime), pickupTime);
        //Tant qu'on a pas l'interaction entre les cooks et le système,
        //On met directement l'order en status READY
    }

    void setState(OrderState state) {
        this.state = state;
    }

    public void setState(OrderStatus status) throws OrderStateException {
        switch (status) {
            case NOT_STARTED -> throw new OrderStateException("You can't go back to an order NotStarted.");
            case PAYED -> state.payOrder();
            case IN_PROGRESS -> state.startOrder();
            case READY -> state.finishOrder();
            case COMPLETED -> state.retrieveOrder();
            case OBSOLETE -> state.abandonOrder();
            case CONVERTED -> state.convertOrder();
            case CANCELLED -> state.cancelOrder();
        }
        this.history.put(status, new Date());
    }
}

