package fr.unice.polytech.services;

import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.NotificationMessage;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;

import java.util.concurrent.ScheduledThreadPoolExecutor;


public class StatusScheduler {

    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(100);
    private static StatusScheduler INSTANCE = null;

    public static StatusScheduler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StatusScheduler();
        }
        return INSTANCE;
    }

    private StatusScheduler(ScheduledThreadPoolExecutor executor) {
        executor.setRemoveOnCancelPolicy(true);
        this.executor = executor;
    }

    private StatusScheduler() {
        executor.setRemoveOnCancelPolicy(true);
    }

    public void emptySchedulerQueue() {
        executor.getQueue()
                .forEach(runnable -> executor.remove(runnable));
        executor.purge();
    }

    public static StatusScheduler getInstance(ScheduledThreadPoolExecutor executor) {
        INSTANCE = new StatusScheduler(executor);
        return INSTANCE;
    }

    public void fiveMinutesNotification(Order order) {
        if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
            notifyClient(order, NotificationMessage.COMMAND_READY_5_MIN);
            executor.schedule(() -> oneHourNotification(order), 55, java.util.concurrent.TimeUnit.MINUTES);
        }
    }

    public void oneHourNotification(Order order) {
        if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
            notifyClient(order, NotificationMessage.COMMAND_READY_1_HOUR);
            executor.schedule(() -> {
                try {
                    twoHoursNotification(order);
                } catch (OrderException e) {
                    throw new RuntimeException(e);
                }
            }, 1, java.util.concurrent.TimeUnit.HOURS);
        }
    }

    public void twoHoursNotification(Order order) throws OrderException {
        if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
            notifyClient(order, NotificationMessage.COMMAND_OBSOLETE);
            order.setStatus(OrderStatus.OBSOLETE);
        }
    }

    public void statusSchedulerTask(Order order) {
        notifyClient(order, NotificationMessage.COMMAND_READY);
        executor.schedule(() -> fiveMinutesNotification(order), 5, java.util.concurrent.TimeUnit.MINUTES);
    }

    /**
     * Helper method to notify the client
     *
     * @param order   the order for which the client will be notified
     * @param message the message to send to the client
     */
    private void notifyClient(Order order, NotificationMessage message) {
        Client client = order.getClient();
        SMSService.getInstance().notifyClient(client.getPhoneNumber(), message.getMessage());
    }

    /**
     * Check status change consistency and update the order status
     *
     * @param order  the order to update
     * @param status the new status
     * @throws OrderException if the status change is not allowed
     */
    public void setStatus(Order order, OrderStatus status) throws OrderException {
        if (order.getStatus().equals(OrderStatus.CANCELLED)) {
            throw new OrderException("This order has been canceled");
        }
        if (!(order.getStatus().equals(OrderStatus.NOT_STARTED) || order.getStatus().equals(OrderStatus.PAYED)) && status.equals(OrderStatus.CANCELLED)) {
            throw new OrderException("This order's status is" + status + "it cannot be cancelled anymore");
        }
        if (order.getStatus().equals((status))) {
            throw new OrderException("This order's status is already " + status);
        }
        order.setStatus(status);
        if (status.equals(OrderStatus.READY)) {
            statusSchedulerTask(order);
        }
    }
}
