package fr.unice.polytech.Order;

import fr.unice.polytech.Client.Client;
import fr.unice.polytech.Store.Cook;

import java.util.Date;
import java.util.List;

public class Order {
    public int id;
    public Double price;
    public Date receiptTime;
    public Date  createdAI;
    public List<Item> items ;
    public Client client;
    public Cook cook;
    public OrderStatus status;

    public Boolean SetStatus(OrderStatus status){
        return true;
    }

}

