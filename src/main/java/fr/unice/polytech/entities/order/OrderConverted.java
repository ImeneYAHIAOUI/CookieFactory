package fr.unice.polytech.entities.order;

import fr.unice.polytech.exception.OrderStateException;

public class OrderConverted extends OrderState {
    public OrderConverted(Order order) {
        super(order);
    }

    @Override
    void payOrder() throws OrderStateException {
        throw new OrderStateException("You can't pay a converted order.");
    }

    @Override
    void cancelOrder() throws OrderStateException {
        throw new OrderStateException("You can't cancel a converted order.");
    }

    @Override
    void startOrder() throws OrderStateException {
        throw new OrderStateException("You can't start a converted order.");
    }

    @Override
    void finishOrder() throws OrderStateException {
        throw new OrderStateException("You can't finish a converted order.");
    }

    @Override
    void retrieveOrder() throws OrderStateException {
        throw new OrderStateException("You can't retrieve a converted order.");
    }

    @Override
    void abandonOrder() throws OrderStateException {
        throw new OrderStateException("You can't abandon a converted order.");
    }

    @Override
    void convertOrder() throws OrderStateException {
        throw new OrderStateException("You can't convert a converted order.");
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.CONVERTED;
    }
}
