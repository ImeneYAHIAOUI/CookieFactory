package fr.unice.polytech.services;

import fr.unice.polytech.recipe.Cookie;
import fr.unice.polytech.store.Store;
import lombok.Data;

import java.util.List;

@Data
public class TooGoodTooGoBag {
    private final List<Cookie> cookies;
    private final Store store;
    private final double price;
    private final String description;

    TooGoodTooGoBag(List<Cookie> cookies, Store store, double price) {
        this.cookies = cookies;
        this.store = store;
        this.price = price;
        this.description = cookies.size() + " cookies";
    }
}
