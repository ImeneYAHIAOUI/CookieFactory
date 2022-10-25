package fr.unice.polytech.order;

import fr.unice.polytech.recipe.Cookie;

public class Item {
    private int quantity;
    private final Cookie cookie;

    public Item(int quantity, Cookie cookie) {
        this.quantity = quantity;
        this.cookie = cookie;
    }

    @Override
    public String toString() {
        return "Item{" +
                "Quantity=" + quantity +
                ", cookie=" + cookie +
                '}';
    }

    public int getQuantity() {
        return quantity;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
}
