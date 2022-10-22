package fr.unice.polytech.client;

import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;

import java.util.ArrayList;
import java.util.List;

public class RegisteredClient extends Client {

    private final String id;
    private final String password;
    private int nb_cookie;
    private final List<Order> pastOrders;

    public RegisteredClient(String id, String password, int phoneNumber) {
        super(phoneNumber);
        this.id = id;
        this.password = password;
        nb_cookie = 0;
        this.pastOrders = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public int getNb_cookie() {
        return nb_cookie;
    }

    @Override
    public void emptyCart(Order order) {
        int nb = 0;
        for (Item item: this.getCart().getItems()) {
            nb += item.getQuantity();
        }
        nb_cookie +=nb;
        pastOrders.add(order);
        getCart().emptyItems();
    }

    public List<Order> getPastOrders() {
        return List.copyOf(pastOrders);
    }
}
