package fr.unice.polytech.store;

import fr.unice.polytech.order.Order;
import lombok.Data;

import java.util.Date;

@Data
public class TimeSlot {
    private final Date begin;
    private final Date end;
    private final Order order;
}
