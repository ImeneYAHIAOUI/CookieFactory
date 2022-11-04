package fr.unice.polytech.order;

import fr.unice.polytech.SMSService;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Store;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {
    @Getter
    private final List<Item> items;
    @Getter
    public Store store;
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private Client client;
    @Getter
    private Cook cook;
    @Getter
    private OrderStatus status;
    @Getter
    @Setter
    private double price;

    @Getter
    @Setter
    private Map<OrderStatus, Date> history;

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", client=" + client +
                ", cook=" + cook +
                ", status=" + status +
                ", items=" + items +
                '}';
    }

    public Order(String id, Client client, Cook cook, Store store) {
        this.id = id;
        this.client = client;
        this.cook = cook;
        this.status = OrderStatus.NOT_STARTED;
        this.items = List.copyOf(client.getCart().getItems());
        this.history = new HashMap<>();
        this.history.put(OrderStatus.NOT_STARTED, new Date());
        this.price = 0;
        this.store =store;
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

