package fr.unice.polytech.store;

import fr.unice.polytech.order.Order;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlot {
    private final LocalTime begin;
    private final LocalTime end;
    private final Order order;

    public TimeSlot(LocalTime begin, LocalTime end, Order order) {
        this.begin = begin;
        this.end = end;
        this.order = order;
    }

    public LocalTime getBegin() {
        return begin;
    }

    public LocalTime getEnd() {
        return end;
    }

    public Order getOrder() {
        return order;
    }
}
