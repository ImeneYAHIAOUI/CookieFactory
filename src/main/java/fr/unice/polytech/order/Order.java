package fr.unice.polytech.order;

import fr.unice.polytech.client.Client;
import fr.unice.polytech.store.Cook;

import java.util.Date;
import java.util.List;

public class Order {
    public String id;
    public Double price;
    public Date pickupTime;
    public Date  createdAt;
    public List<Item> items ;
    public Client client;
    public Cook cook;
    public OrderStatus status;

    public  Order(String id, Double price, Date pickupTime, Date createdAt, List<Item> items, Client client, Cook cook, OrderStatus status) {
        this.id = id;
        this.price = price;
        this.pickupTime = pickupTime;
        this.createdAt = createdAt;
        this.items = items;
        this.client = client;
        this.cook = cook;
        this.status = status;
    }

    public Boolean SetStatus(OrderStatus status){
        return true;
    }

    public String getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public Date getPickupTime() {
        return pickupTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public List<Item> getItems() {
        return items;
    }

    public Client getClient() {
        return client;
    }

    public Cook getCook() {
        return cook;
    }


    public OrderStatus getStatus() {
        return status;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    public void setPickupTime(Date pickupTime){
        this.pickupTime = pickupTime;
    }

    public void setCook(Cook cook){
        this.cook = cook;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setStatus(OrderStatus status) throws OrderException {
        if(this.status.equals(OrderStatus.CANCELED)){
            throw new OrderException("this order hes been canceled");
        }
        if(this.status.equals(OrderStatus.IN_PROGRESS) && status.equals(OrderStatus.CANCELED)) {
            throw new OrderException("This order is being prepared, you cannot cancel it anymore");
        }
        if(this.status.equals((status))){
            switch (status){
                case NOT_STARTED:
                    throw new OrderException("This order status is already \"not started\"");
                case CANCELED:
                    throw new OrderException("This order status is already \"canceled\"");
                case IN_PROGRESS:
                    throw new OrderException("This order status is already \"in progress\"");
                case READY:
                    throw new OrderException("This order status is already \"ready\"");
                case COMPLETED:
                    throw new OrderException("This order status is already \"completed\"");
                case  OBSOLETE :
                    throw new OrderException("This order status is already \"obsolete\"");
                case PAYED:
                    throw new OrderException("This order status is already \"payed\"");


            }
        }
        this.status = status;
    }





}

