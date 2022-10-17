package fr.unice.polytech.order;

import fr.unice.polytech.client.Client;
import fr.unice.polytech.store.Cook;

import java.util.Date;
import java.util.List;

public class Order {
    public String id;
    public Client client;
    public Cook cook;
    public OrderStatus status;

    public  Order(String id, Client client, Cook cook) {
        this.id = id;
        this.client = client;
        this.cook = cook;
        this.status = OrderStatus.READY;
        //Tant qu'on a pas l'interaction entre les cooks et le système,
        //On met directement l'order en status READY
    }

    public Boolean SetStatus(OrderStatus status){
        return true;
    }

    public String getId() {
        return id;
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

    public void setCook(Cook cook){
        this.cook = cook;
    }


    public void setClient(Client client) {
        this.client = client;
    }

    public void setStatus(OrderStatus status) throws OrderException {
        if(this.status.equals(OrderStatus.CANCELLED)){
            throw new OrderException("this order hes been canceled");
        }
        if(! (this.status.equals(OrderStatus.NOT_STARTED) || this.status.equals(OrderStatus.PAYED)) && status.equals(OrderStatus.CANCELLED)) {
            throw new OrderException("this order's status is"+status+"it cannot be cancelled anymore");
        }
        if(this.status.equals((status))){
            switch (status){
                case NOT_STARTED:
                    throw new OrderException("This order's status is already \"not started\"");
                case CANCELLED:
                    throw new OrderException("This order's status is already \"cancelled\"");
                case IN_PROGRESS:
                    throw new OrderException("This order's status is already \"in progress\"");
                case READY:
                    throw new OrderException("This order's status is already \"ready\"");
                case COMPLETED:
                    throw new OrderException("This order's status is already \"completed\"");
                case  OBSOLETE :
                    throw new OrderException("This order's status is already \"obsolete\"");
                case PAYED:
                    throw new OrderException("This order's status is already \"payed\"");
            }
        }
        this.status = status;
    }





}

