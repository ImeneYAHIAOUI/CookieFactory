package fr.unice.polytech.client;

public class Client {
    private int phoneNumber;
    private Cart cart;

    public Client(int phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.cart = new Cart();
    }

    public Cart getCart(){
        return this.cart;
    }

    public void emptyCart() {
        cart.emptyItems();
    }
}