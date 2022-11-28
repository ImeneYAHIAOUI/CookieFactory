package fr.unice.polytech.entities.order;

import fr.unice.polytech.entities.client.Cart;
import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.TimeSlot;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Order {
    private final List<Item> items;
    public final Store store;
    private String id;
    private Client client;
    private final Cook cook;
    private OrderStatus status;
    @Getter
    @Setter
    private double price;
    private final Map<OrderStatus, Date> history;
    private final TimeSlot timeSlot;
    private LocalDateTime pickupTime;

    public Order(String id, Client client, Cook cook, Store store) {
        this.id = id;
        this.client = client;
        this.cook = cook;
        this.status = OrderStatus.NOT_STARTED;
        this.items = List.copyOf(client.getCart().getItems());
        this.history = new HashMap<>();
        this.history.put(OrderStatus.NOT_STARTED, new Date());
        this.store = store;
        Cart cart = client.getCart();
        this.pickupTime = cart.getPickupTime();
        this.price = cart.getTotal();
        this.timeSlot = new TimeSlot(pickupTime.minus(client.getCart().totalCookingTime()), pickupTime);
        //Tant qu'on a pas l'interaction entre les cooks et le système,
        //On met directement l'order en status READY
    }

    public void setStatus(OrderStatus status) throws OrderException {
        this.status = status;
        this.history.put(status, new Date());
    }
}

