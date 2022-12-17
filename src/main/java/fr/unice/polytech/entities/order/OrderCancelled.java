package fr.unice.polytech.entities.order;

import fr.unice.polytech.exception.OrderStateException;

public class OrderCancelled extends OrderState {
    public OrderCancelled(Order order) {
        super(order);
    }

    @Override
    void payOrder() throws OrderStateException {
        throw new OrderStateException("You can't pay a cancelled order.");
    }

    @Override
    void cancelOrder() throws OrderStateException {
        throw new OrderStateException("You can't cancel a cancelled order.");
    }

    @Override
    void startOrder() throws OrderStateException {
        throw new OrderStateException("You can't start a cancelled order.");
    }

    @Override
    void finishOrder() throws OrderStateException {
        throw new OrderStateException("You can't finish a cancelled order.");
    }

    @Override
    void retrieveOrder() throws OrderStateException {
        throw new OrderStateException("You can't retrieved a cancelled order.");
    }

    @Override
    void abandonOrder() throws OrderStateException {
        throw new OrderStateException("You can't abandon a cancelled order.");
    }

    @Override
    void convertOrder() throws OrderStateException {
        throw new OrderStateException("You can't convert a cancelled order.");
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.CANCELLED;
    }
}
