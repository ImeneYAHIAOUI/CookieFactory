package fr.unice.polytech.entities.client;

import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import lombok.Getter;

public abstract class Client {
    @Getter
    private final String phoneNumber;
    @Getter
    private final Cart cart;

    public Client(String phoneNumber){
        this.phoneNumber = phoneNumber;
        this.cart = new Cart();
    }

    public void getNotified(Order order, String message) {
        System.out.println(order + " : " + message);
    }
}
