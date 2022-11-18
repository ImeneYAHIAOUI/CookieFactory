package fr.unice.polytech.services;

import fr.unice.polytech.cod.COD;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.store.Store;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Service to create TooGoodToGo bags from obsolete orders
 * There is one instance of this service per store
 */
@Data
public class TooGoodToGo {
    private final Store store;
    private int bagCount;
    private final ScheduledThreadPoolExecutor executor;

    private ScheduledFuture<?> bagTask;

    public TooGoodToGo(Store store, ScheduledThreadPoolExecutor executor) {
        this.store = store;
        this.bagCount = 0;
        this.executor = executor;
    }

    public TooGoodToGo(Store store) {
        this.store = store;
        this.bagCount = 0;
        this.executor = new ScheduledThreadPoolExecutor(1);
    }

    /**
     * Initializes the thread pool to create bags every 3 hours
     */
    public void initThreadPool() {
        executor.scheduleAtFixedRate(() -> {
            if (LocalTime.now(COD.getCLOCK()).isAfter(store.getClosingTime())) {
                bagTask.cancel(true);
                return;
            }
            checkObsoleteOrders();
        }, 0, 3, java.util.concurrent.TimeUnit.HOURS);
    }

    /**
     * Checks if there are obsolete orders and converts them into bags
     */
    private void checkObsoleteOrders() {
        List<Order> obsoleteOrders = COD.getInstance().getOrders()
                .stream()
                .filter(order -> order.getStore().equals(store) && order.getStatus().equals(OrderStatus.OBSOLETE))
                .toList();
        obsoleteOrders.forEach(this::convertOrder);
    }


    /**
     * Convert an order into a TooGoodToGo bag
     *
     * @param order the order to convert
     */
    public void convertOrder(Order order) {
        bagCount++;
        System.out.println("Surprise bag published " + order.getId());
    }
}
