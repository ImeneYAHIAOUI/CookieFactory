package fr.unice.polytech.entities.store;

import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.recipe.enums.Theme;
import fr.unice.polytech.exception.CookException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

public class Cook {
    @Getter
    private final SortedMap<TimeSlot, Order> workingTimeSlot;
    final UUID id;
    @Getter
    private final List<Theme> themeList;

    public Cook() {
        this.id = UUID.randomUUID();
        workingTimeSlot = new ConcurrentSkipListMap<>();
        themeList=new ArrayList<>();
    }

    /**
     * Returns whether this cook is available during the given time slot
     *
     * @param timeSlot the time slot to check the cook availability for
     * @return true if the cook is available
     */

    public boolean canTakeTimeSlot(TimeSlot timeSlot) {
        return workingTimeSlot.keySet().stream().noneMatch(timeSlot::overlaps);
    }

    //Add the Order at the first time available to cook the order
    //The timeSlot list is sorted by time (to simplify th search of available times)

    public void addOrder(Order order) throws CookException {
        if (!canTakeTimeSlot(order.getTimeSlot())) {
            throw new CookException("The cook " + id + " can't cook the order " + order.getId());
        }
        this.workingTimeSlot.put(order.getTimeSlot(), order);
    }
    /**
     * Removes the order from the cook's working time slot
     *
     * @param order the order to remove
     */

    public void cancelOrder(Order order) {
        workingTimeSlot.remove(order.getTimeSlot());
    }

    public void addTheme(Theme theme){
        themeList.add(theme);
    }
}

