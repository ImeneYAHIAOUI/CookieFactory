package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.NotificationMessage;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.exception.OrderStateException;
import fr.unice.polytech.interfaces.NotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledThreadPoolExecutor;

@Component
public class NotificationManager implements NotificationHandler {

    private final ScheduledThreadPoolExecutor executor;

    @Autowired
    public NotificationManager(ScheduledThreadPoolExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void fiveMinutesNotification(Order order) {
        if (!order.getState().getOrderStatus().equals(OrderStatus.COMPLETED)) {
            notifyClient(order, NotificationMessage.COMMAND_READY_5_MIN);
            executor.schedule(() -> oneHourNotification(order), 55, java.util.concurrent.TimeUnit.MINUTES);
        }
    }
    @Override
    public void oneHourNotification(Order order) {
        if (!order.getState().getOrderStatus().equals(OrderStatus.COMPLETED)) {
            notifyClient(order, NotificationMessage.COMMAND_READY_1_HOUR);
            executor.schedule(() -> {
                try {
                    twoHoursNotification(order);
                } catch (OrderException | OrderStateException e) {
                    throw new RuntimeException(e);
                }
            }, 1, java.util.concurrent.TimeUnit.HOURS);
        }
    }

    @Override
    public void twoHoursNotification(Order order) throws OrderException, OrderStateException {
        if (!order.getState().getOrderStatus().equals(OrderStatus.COMPLETED)) {
            notifyClient(order, NotificationMessage.COMMAND_OBSOLETE);
            order.setState(OrderStatus.OBSOLETE);
        }
    }

    @Override
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
    @Override
    public  void notifyClient(Order order, NotificationMessage message) {
        Client client = order.getClient();
        notifyClient(client.getPhoneNumber(), message.getMessage());
    }
    public void notifyClient(String clientPhoneNumber,String message) {
        System.out.println("Sending SMS to " + clientPhoneNumber);
        System.out.println("Message : " + message);
    }
}
