package fr.unice.polytech.services;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import fr.unice.polytech.client.NotificationMessage;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;


public class StatusScheduler {

    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);


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

    public static StatusScheduler getInstance(ScheduledThreadPoolExecutor executor ) {
        if (INSTANCE == null) {
            INSTANCE = new StatusScheduler(executor);
        }
        return INSTANCE;
    }

    public void fiveMinutesNotification(Order order, String clientPhoneNumber) {
        if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
            SMSService.getInstance().notifyClient(clientPhoneNumber, NotificationMessage.COMMAND_READY_5_MIN.getMessage());
            executor.schedule(() -> oneHourNotification(order, clientPhoneNumber), 55, java.util.concurrent.TimeUnit.MINUTES);
        }
    }

    public void oneHourNotification(Order order, String clientPhoneNumber) {
        if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
            SMSService.getInstance().notifyClient(clientPhoneNumber, NotificationMessage.COMMAND_READY_1_HOUR.getMessage());
            executor.schedule(() -> {
                try {
                    twoHoursNotification(order, clientPhoneNumber);
                } catch (OrderException e) {
                    throw new RuntimeException(e);
                }
            }, 1, java.util.concurrent.TimeUnit.HOURS);
        }
    }

    public void twoHoursNotification(Order order, String clientPhoneNumber) throws OrderException {
        if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
            SMSService.getInstance().notifyClient(clientPhoneNumber, NotificationMessage.COMMAND_OBSOLETE.getMessage());
            order.setStatus(OrderStatus.OBSOLETE);
        }
    }

    public void statusSchedulerTask(Order order, String clientPhoneNumber) {
        SMSService.getInstance().notifyClient(clientPhoneNumber, NotificationMessage.COMMAND_READY.getMessage());
        executor.schedule(() -> {
            fiveMinutesNotification(order, clientPhoneNumber);
        }, 5, java.util.concurrent.TimeUnit.MINUTES);
    }


}
