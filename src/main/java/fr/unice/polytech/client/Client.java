package fr.unice.polytech.client;

public class Client {
    private int phoneNumber;
    private Cart cart;

    public Client(int phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.cart = new Cart();
    }
}
