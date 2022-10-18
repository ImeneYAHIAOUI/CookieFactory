package fr.unice.polytech.order;

import fr.unice.polytech.recipe.Cookie;

public class Item {
    public int Quantity;
    public Cookie cookie;

    public Item(int quantity, Cookie cookie) {
        Quantity = quantity;
        this.cookie = cookie;
    }

    @Override
    public String toString() {
        return "Item{" +
                "Quantity=" + Quantity +
                ", cookie=" + cookie +
                '}';
    }

    public int getQuantity() {
        return Quantity;
    }

    public Cookie getCookie() {
        return cookie;
    }
}
