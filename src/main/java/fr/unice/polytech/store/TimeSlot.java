package fr.unice.polytech.store;

import fr.unice.polytech.order.Order;

import java.util.Date;

public class TimeSlot {
    private final Date begin;
    private final Date end;
    private final Order order;

    public TimeSlot(Date begin, Date end, Order order) {
        this.begin = begin;
        this.end = end;
        this.order = order;
    }
}
