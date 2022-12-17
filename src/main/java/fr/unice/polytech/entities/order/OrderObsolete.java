package fr.unice.polytech.entities.order;

import fr.unice.polytech.exception.OrderStateException;

public class OrderObsolete extends OrderState {
    public OrderObsolete(Order order) {
        super(order);
    }

    @Override
    void payOrder() throws OrderStateException {
        throw new OrderStateException("You can't pay an obsolete order.");
    }

    @Override
    void cancelOrder() throws OrderStateException {
        throw new OrderStateException("You can't cancel an obsolete order.");
    }

    @Override
    void startOrder() throws OrderStateException {
        throw new OrderStateException("You can't start an obsolete order.");
    }

    @Override
    void finishOrder() throws OrderStateException {
        throw new OrderStateException("You can't finish an obsolete order.");
    }

    @Override
    void retrieveOrder() throws OrderStateException {
        throw new OrderStateException("You can't retrieve an obsolete order.");
    }

    @Override
    void abandonOrder() throws OrderStateException {
        throw new OrderStateException("You can't abandon an obsolete order.");
    }

    @Override
    void convertOrder() throws OrderStateException {
        order.setState(new OrderConverted(order));
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.OBSOLETE;
    }
}
