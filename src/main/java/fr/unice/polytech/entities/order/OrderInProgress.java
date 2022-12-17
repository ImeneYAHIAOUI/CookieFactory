package fr.unice.polytech.entities.order;

import fr.unice.polytech.exception.OrderStateException;

public class OrderInProgress extends OrderState {
    public OrderInProgress(Order order) {
        super(order);
    }

    @Override
    void payOrder() throws OrderStateException {
        throw new OrderStateException("You can't pay an order in progress.");
    }

    @Override
    void cancelOrder() throws OrderStateException {
        throw new OrderStateException("You can't cancel an order in progress.");
    }

    @Override
    void startOrder() throws OrderStateException {
        throw new OrderStateException("You can't start an order in progress.");
    }

    @Override
    void finishOrder() throws OrderStateException {
        order.setState(new OrderReady(order));
    }

    @Override
    void retrieveOrder() throws OrderStateException {
        throw new OrderStateException("You can't retrieve an order in progress.");
    }

    @Override
    void abandonOrder() throws OrderStateException {
        throw new OrderStateException("You can't abandon an order in progress.");
    }

    @Override
    void convertOrder() throws OrderStateException {
        throw new OrderStateException("You can't convert an order in progress.");
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.IN_PROGRESS;
    }
}
