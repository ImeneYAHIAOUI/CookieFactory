package fr.unice.polytech.entities.client;

import fr.unice.polytech.entities.order.Item;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    @Getter
    @Setter
    private List<Item> items;
    @Getter
    @Setter
    private LocalDateTime pickupTime;
    @Getter
    @Setter
    private Double subtotal;
    @Getter
    @Setter
    private Double tax;
    @Getter
    @Setter
    private Double total;
    public Cart() {
        this.items = new ArrayList<>();
        pickupTime = null;
        subtotal=0.0;
        total=0.0;
        tax=0.0;
    }
}
