package fr.unice.polytech.entities.order;

import fr.unice.polytech.entities.recipe.cookies.Cookie;
import lombok.Getter;

public class Item {
    @Getter
    private final Cookie cookie;
    @Getter
    private int quantity;

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

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
    public void decreaseQuantity(int quantity) {
        this.quantity -= quantity;
    }
}
