package fr.unice.polytech.entities.order;

import fr.unice.polytech.exception.OrderStateException;

public class OrderReady extends OrderState {
    public OrderReady(Order order) {
        super(order);
    }

    @Override
    void payOrder() throws OrderStateException {
        throw new OrderStateException("You can't pay a order ready to be retrieved.");
    }

    @Override
    void cancelOrder() throws OrderStateException {
        throw new OrderStateException("You can't cancel a order ready to be retrieved.");
    }

    @Override
    void startOrder() throws OrderStateException {
        throw new OrderStateException("You can't start a order ready to be retrieved.");
    }

    @Override
    void finishOrder() throws OrderStateException {
        throw new OrderStateException("You can't finish a order ready to be retrieved.");
    }

    @Override
    void retrieveOrder() throws OrderStateException {
        order.setState(new OrderCompleted(order));
    }

    @Override
    void abandonOrder() throws OrderStateException {
        order.setState(new OrderObsolete(order));
    }

    @Override
    void convertOrder() throws OrderStateException {
        throw new OrderStateException("You can't convert a order ready to be retrieved.");
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.READY;
    }
}
