package fr.unice.polytech.store;

import fr.unice.polytech.exception.CookException;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.recipe.Cookie;
import fr.unice.polytech.recipe.PartyCookie;
import fr.unice.polytech.recipe.Theme;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class Cook {
    @Getter
    private final SortedMap<TimeSlot, Order> workingTimeSlot;
    final int id;
    @Getter
    private final List<Theme> themeList;

    public Cook(int id) {
        this.id = id;
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
    public boolean canMakeCookie(Cookie cookie){
        if (cookie instanceof PartyCookie){
            return themeList.contains(((PartyCookie) cookie).getTheme());
        }
        return true;
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

