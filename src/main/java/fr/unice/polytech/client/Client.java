package fr.unice.polytech.client;

import fr.unice.polytech.order.Order;

public abstract class Client {
    private final int phoneNumber;
    private final Cart cart;

    public Client(int phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.cart = new Cart();
    }

    public Cart getCart(){
        return this.cart;
    }

    public void emptyCart(Order order) {
        cart.emptyItems();
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }
}
