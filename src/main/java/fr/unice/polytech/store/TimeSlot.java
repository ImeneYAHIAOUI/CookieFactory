package fr.unice.polytech.store;

import fr.unice.polytech.order.Order;

import java.util.Date;

public class TimeSlot {
    public Date begin;
    public Date end;
    public Order order;

    public TimeSlot(Date begin, Date end, Order order) {
        this.begin = begin;
        this.end = end;
        this.order = order;
    }
}
