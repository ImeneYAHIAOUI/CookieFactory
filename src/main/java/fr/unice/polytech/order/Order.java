package fr.unice.polytech.order;

import fr.unice.polytech.client.Client;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.services.SMSService;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Store;
import fr.unice.polytech.store.TimeSlot;
import lombok.Data;

import java.time.LocalTime;
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
    private double price;
    private final Map<OrderStatus, Date> history;
    private final TimeSlot timeSlot;
    private LocalTime pickupTime;

    public Order(String id, Client client, Cook cook, Store store) {
        this.id = id;
        this.client = client;
        this.cook = cook;
        this.status = OrderStatus.NOT_STARTED;
        this.items = List.copyOf(client.getCart().getItems());
        this.history = new HashMap<>();
        this.history.put(OrderStatus.NOT_STARTED, new Date());
        this.price = 0;
        this.store = store;
        this.pickupTime = client.getCart().getPickupTime();
        this.timeSlot = new TimeSlot(pickupTime.minus(client.getCart().totalCookingTime()), pickupTime);
        //Tant qu'on a pas l'interaction entre les cooks et le système,
        //On met directement l'order en status READY
    }

    public void setStatus(OrderStatus status) throws OrderException {
        if (this.status.equals(OrderStatus.CANCELLED)) {
            throw new OrderException("This order has been canceled");
        }
        if (!(this.status.equals(OrderStatus.NOT_STARTED) || this.status.equals(OrderStatus.PAYED)) && status.equals(OrderStatus.CANCELLED)) {
            throw new OrderException("This order's status is" + status + "it cannot be cancelled anymore");
        }
        if (this.status.equals((status))) {
            throw new OrderException("This order's status is already "+status);
        }
        if (status.equals(OrderStatus.READY)) {
            SMSService.getInstance().notifyClient(this.client.getPhoneNumber());
        }
        this.status = status;
        this.history.put(status, new Date());
    }
}

