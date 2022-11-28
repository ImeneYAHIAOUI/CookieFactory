package fr.unice.polytech.services;

import fr.unice.polytech.cod.COD;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.store.Store;
import lombok.Data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
/**
 * Service to create TooGoodToGo bags from obsolete orders
 * There is one instance of this service per store
 */
@Data
public class TooGoodToGo {
    private int bagCount;
    private List<TooGoodToGoBag> bags;
    private final ScheduledThreadPoolExecutor executor;

    private ScheduledFuture<?> bagTask;

    public TooGoodToGo(ScheduledThreadPoolExecutor executor) {
        this.executor = executor;
    }

    public TooGoodToGo() {
        this.bagCount = 0;
        bags=new ArrayList<>();
        this.executor = new ScheduledThreadPoolExecutor(1);
    }

    /**
     * Initializes the thread pool to create bags every 3 hours
     */
    public void initThreadPool(Store store) {
        executor.scheduleAtFixedRate(() -> {
            if (LocalTime.now(COD.getCLOCK()).isAfter(store.getClosingTime())) {
                bagTask.cancel(true);
                return;
            }
            checkObsoleteOrders(store);
        }, 0, 3, java.util.concurrent.TimeUnit.HOURS);
    }

    /**
     * Checks if there are obsolete orders and converts them into bags
     */
    private void checkObsoleteOrders(Store store) {
        List<Order> obsoleteOrders = COD.getInstance().getOrders()
                .stream()
                .filter(order -> order.getStore().equals(store) && order.getStatus().equals(OrderStatus.OBSOLETE))
                .toList();

        obsoleteOrders.forEach(order -> convertOrder(store,order));
    }


    /**
     * Convert an order into a TooGoodToGo bag
     *
     * @param order the order to convert
     */
    public void convertOrder(Store store,Order order) {
        bagCount++;
        store.addTooGooToGoBAG(new TooGoodToGoBag(order));
        System.out.println("Surprise bag published " + order.getId());
    }
}
