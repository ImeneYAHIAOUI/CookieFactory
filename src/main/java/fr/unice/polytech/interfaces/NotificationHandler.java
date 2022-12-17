package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.client.NotificationMessage;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.exception.OrderStateException;

public interface NotificationHandler {
    void statusSchedulerTask(Order order);
    void notifyClient(Order order, NotificationMessage message);
    void twoHoursNotification(Order order) throws OrderException, OrderStateException;
    void oneHourNotification(Order order);
    void fiveMinutesNotification(Order order);
    void notifyClient(String clientPhoneNumber,String message);
}
