package fr.unice.polytech.store;

import fr.unice.polytech.order.Order;

import java.util.ArrayList;
import java.util.List;

public class Cook {

    public List<Order> assignedOrders;

    public Cook(){
        this.assignedOrders = new ArrayList<>();
    }

    public void addOrder(Order order){
        this.assignedOrders.add(order);
    }
}

