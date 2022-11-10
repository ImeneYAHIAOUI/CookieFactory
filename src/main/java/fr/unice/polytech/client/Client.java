package fr.unice.polytech.client;

import fr.unice.polytech.order.Order;
import lombok.Getter;

public abstract class Client {
    @Getter
    private final String phoneNumber;
    @Getter
    private final Cart cart;

    public Client(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.cart = new Cart();
    }

    public void validateOrder(Order order) {
        cart.emptyItems();
    }

    public void getNotified(Order order, String message) {
        System.out.println(order + " : " + message);
    }

    public boolean isRegistered(){
        return false;
    }
}
