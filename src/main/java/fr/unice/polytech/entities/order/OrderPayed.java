package fr.unice.polytech.entities.order;

import fr.unice.polytech.exception.OrderStateException;

public class OrderPayed extends OrderState {
    public OrderPayed(Order order) {
        super(order);
    }

    @Override
    void payOrder() throws OrderStateException {
        throw new OrderStateException("You can't pay a payed order.");
    }

    @Override
    void cancelOrder() throws OrderStateException {
        order.setState(new OrderCancelled(order));
    }

    @Override
    void startOrder() throws OrderStateException {
        order.setState(new OrderInProgress(order));
    }

    @Override
    void finishOrder() throws OrderStateException {
        order.setState(new OrderReady(order));
    }

    @Override
    void retrieveOrder() throws OrderStateException {
        throw new OrderStateException("You can't retrieve a payed order.");
    }

    @Override
    void abandonOrder() throws OrderStateException {
        throw new OrderStateException("You can't abandon a payed order.");
    }

    @Override
    void convertOrder() throws OrderStateException {
        throw new OrderStateException("You can't convert a payed order.");
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.PAYED;
    }
}
