package fr.unice.polytech.entities.order;

import fr.unice.polytech.exception.OrderStateException;

public class OrderCompleted extends OrderState {
    public OrderCompleted(Order order) {
        super(order);
    }

    @Override
    void payOrder() throws OrderStateException {
        throw new OrderStateException("You can't pay a completed order.");
    }

    @Override
    void cancelOrder() throws OrderStateException {
        throw new OrderStateException("You can't cancel a completed order.");
    }

    @Override
    void startOrder() throws OrderStateException {
        throw new OrderStateException("You can't start a completed order.");
    }

    @Override
    void finishOrder() throws OrderStateException {
        throw new OrderStateException("You can't finish a completed order.");
    }

    @Override
    void retrieveOrder() throws OrderStateException {
        throw new OrderStateException("You can't retrieve a completed order.");
    }

    @Override
    void abandonOrder() throws OrderStateException {
        throw new OrderStateException("You can't abandon a completed order.");
    }

    @Override
    void convertOrder() throws OrderStateException {
        throw new OrderStateException("You can't convert a completed order.");
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.COMPLETED;
    }
}
