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
        items.add(item);
    }
    public List<Item> getItems() {
        return items;
    }
    public void emptyItems(){
        items.clear();
    }
}
