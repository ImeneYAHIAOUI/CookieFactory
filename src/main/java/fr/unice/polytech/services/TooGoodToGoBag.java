package fr.unice.polytech.services;

import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.store.Store;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class TooGoodToGoBag {
    private final List<Item> cookies;
    private final Store store;
    private final double price;
    private final String description;
    TooGoodToGoBag(Order order) {

        this.cookies = new ArrayList<>();
        order.getItems().forEach(item -> cookies.add(item));

        this.store = order.store;
        this.price = order.getPrice();
        int size=0;
        for (Item item :order.getItems()) {
            size+= item.getQuantity();
        }
        this.description = size + " cookies at "+store.getAddress();
    }
}
