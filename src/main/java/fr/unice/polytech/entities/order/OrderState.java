package fr.unice.polytech.entities.order;

import fr.unice.polytech.exception.OrderStateException;

public abstract class OrderState {
    protected Order order;

    public OrderState(Order order){
        this.order = order;
    }

    abstract void payOrder() throws OrderStateException;
    abstract void cancelOrder() throws OrderStateException;
    abstract void startOrder() throws OrderStateException;
    abstract void finishOrder() throws OrderStateException;
    abstract void retrieveOrder() throws OrderStateException;
    abstract void abandonOrder() throws OrderStateException;
    abstract void convertOrder() throws OrderStateException;
    public abstract OrderStatus getOrderStatus();
}
