package fr.unice.polytech.client;

import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class RegisteredClient extends Client {

    @Getter
    private final String id;
    @Getter
    private final String password;
    @Getter
    private final List<Order> pastOrders;
    @Getter
    private int nbCookie;

    public RegisteredClient(String id, String password, int phoneNumber) {
        super(phoneNumber);
        this.id = id;
        this.password = password;
        nbCookie = 0;
        this.pastOrders = new ArrayList<>();
    }

    @Override
    public void emptyCart(Order order) {
        int nb = 0;
        for (Item item : this.getCart().getItems()) {
            nb += item.getQuantity();
        }
        nbCookie += nb;
        pastOrders.add(order);
        getCart().emptyItems();
    }
}
