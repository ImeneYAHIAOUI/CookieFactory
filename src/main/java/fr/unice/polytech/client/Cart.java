package fr.unice.polytech.client;

import fr.unice.polytech.order.Item;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Item> items;

    Cart() {
        this.items = new ArrayList<>();
    }
    public void addItem(Item item) {
        if ( items.stream().anyMatch(item1 -> item1.getCookie() == item.getCookie()) ) {
            Item existingItem = items.stream().filter(item1 -> item1.getCookie() == item.getCookie()).findFirst().orElse(null);
            existingItem.incrementQuantity(item.getQuantity());

        } else {
            items.add(item);
        }
    }
    public List<Item> getItems() {
        return List.copyOf(items);
    }
    public void emptyItems(){
        items.clear();
    }
}
