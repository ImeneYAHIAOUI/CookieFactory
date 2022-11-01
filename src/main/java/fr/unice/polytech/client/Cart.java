package fr.unice.polytech.client;

import fr.unice.polytech.order.Item;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    @Getter
    private final List<Item> items;


    public Cart() {
        this.items = new ArrayList<>();
    }
    public void addItem(Item item) {
        if ( items.stream().anyMatch(item1 -> item1.getCookie() == item.getCookie()) ) {
            Item existingItem = items.stream().filter(item1 -> item1.getCookie() == item.getCookie()).findFirst().orElse(null);
            assert existingItem != null;
            existingItem.increaseQuantity(item.getQuantity());

        } else {
            items.add(item);
        }

    }
    public void emptyItems(){
        items.clear();
    }
}
