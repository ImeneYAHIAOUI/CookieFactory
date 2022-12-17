package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.exception.OrderException;

import java.util.List;
import java.util.UUID;

public interface OrderFinder {

    List<Order> getOrders();

    List<Order> getOrdersWithStatus(OrderStatus status);

    Order getOrder(UUID id) throws OrderException;
}
