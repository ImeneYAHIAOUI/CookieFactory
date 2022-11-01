package fr.unice.polytech.order;

import fr.unice.polytech.SMSService;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Store;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    @Setter
    private Cook cook;
    @Getter
    private OrderStatus status;

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

    public  Order(String id, Client client, Cook cook) {
        this.id = id;
        this.client = client;
        this.cook = cook;
        this.status = OrderStatus.NOT_STARTED;
        this.items = List.copyOf(client.getCart().getItems());
        //Tant qu'on a pas l'interaction entre les cooks et le système,
        //On met directement l'order en status READY
    }

    public void setStatus(OrderStatus status) throws OrderException {
        if (this.status.equals(OrderStatus.CANCELLED)) {
            throw new OrderException("this order hes been canceled");
        }
        if (!(this.status.equals(OrderStatus.NOT_STARTED) || this.status.equals(OrderStatus.PAYED)) && status.equals(OrderStatus.CANCELLED)) {
            throw new OrderException("this order's status is" + status + "it cannot be cancelled anymore");
        }
        if (this.status.equals((status))) {
            switch (status) {
                case NOT_STARTED -> throw new OrderException("This order's status is already \"not started\"");
                case CANCELLED -> throw new OrderException("This order's status is already \"cancelled\"");
                case IN_PROGRESS -> throw new OrderException("This order's status is already \"in progress\"");
                case READY -> throw new OrderException("This order's status is already \"ready\"");
                case COMPLETED -> throw new OrderException("This order's status is already \"completed\"");
                case OBSOLETE -> throw new OrderException("This order's status is already \"obsolete\"");
                case PAYED -> throw new OrderException("This order's status is already \"payed\"");
            }
        }
        if (status.equals(OrderStatus.READY)) {
            SMSService.getInstance().notifyClient(this.client.getPhoneNumber());
        }
        this.status = status;
    }

}

