package fr.unice.polytech.store;

import fr.unice.polytech.client.Cart;
import fr.unice.polytech.exception.CookException;
import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Cook {
    public List<TimeSlot> workingTimeSlot;
    final int id;

    public Cook(int id) {
        this.id = id;
        workingTimeSlot = new ArrayList<>();
    }

    public List<TimeSlot> getWorkingTimeSlot(){
        return List.copyOf(workingTimeSlot);
    }

    public boolean canCook(Cart cart, Store store) throws CookException {
        return firstPlace(store, timeOrder(cart.getItems())) != null;
    }

    public void cancelOrder(Order order){
        this.workingTimeSlot.removeIf(t -> t.getOrder().equals(order));
    }

    //Add the Order at the first time available to cook the order
    //The timeSlot list is sorted by time (to simplify th search of available times)
    public void addOrder(Order order) throws CookException {
        long timeOrder = timeOrder(order.getItems());
        LocalTime newTimeSlotBeginning = firstPlace(order.getStore(), timeOrder);

        if (newTimeSlotBeginning == null)
            throw new CookException("The cook "+id+" can't cook the order "+order.getId());

        this.workingTimeSlot.add(new TimeSlot(
                newTimeSlotBeginning,
                newTimeSlotBeginning.plusMinutes(timeOrder),
                order));
        this.workingTimeSlot.sort(new TimeSlotComparator());
    }

    //Calculate the needed time to cook all the items
    private long timeOrder(List<Item> items){
        long timeToCookOrder = 15;
        for (Item i: items) {
            timeToCookOrder += (long) i.getCookie().getCookingTime() * i.getQuantity();
        }
        while (timeToCookOrder%15 != 0)
            timeToCookOrder +=1;
        return timeToCookOrder;
    }

    //Search the first available time to do the order
    //Return null if this cook can't do the order
    private LocalTime firstPlace(Store store, long timeOrder) throws CookException {
        if(!store.getCooks().contains(this))
            throw new CookException("The store "+store.getId()+" does not contains the cook "+id);
        if (workingTimeSlot.isEmpty() && store.getOpeningTime().plusMinutes(timeOrder).isBefore(store.getClosingTime()))
            return store.getOpeningTime();
        LocalTime firstAvailabilityOrder = store.getOpeningTime();
        for (TimeSlot t: this.workingTimeSlot) {
            if(firstAvailabilityOrder.plusMinutes(timeOrder).isBefore(t.getBegin()))
                return firstAvailabilityOrder;
            firstAvailabilityOrder = t.getEnd();
        }
        return null;
    }

    //Pourquoi SuggestRecipe dans Cook ?
   /* public void suggestRecipe(String cookieName, Double CookingTime, Cooking cooking, Mix mix, Dough dough, Flavour flavour, List<Topping> toppingList) {
        Cookie newCookie = new Cookie(cookieName, 0.0, CookingTime, cooking, mix, dough, flavour, toppingList);
        cod.suggestRecipe(newCookie);
    }*/
}

