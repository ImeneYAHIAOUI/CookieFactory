package fr.unice.polytech.store;

import fr.unice.polytech.COD;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.recipe.*;

import java.util.ArrayList;
import java.util.List;

public class Cook {
    public final List<Order> assignedOrders;
    public List<TimeSlot> avaibleTimeSlot;
    public COD cod;
    final int id;

    public Cook(int id) {
        this.id = id;
        this.assignedOrders = new ArrayList<>();
    }

    public List<TimeSlot> getTimeSlot() {
        return avaibleTimeSlot;
    }

    public void addOrder(Order order) {

        assignedOrders.add(order);
    }

    public void suggestRecipe(String cookieName, Double CookingTime, Cooking cooking, Mix mix, Dough dough, Flavour flavour, List<Topping> toppingList) {
        Cookie newCookie = new Cookie(cookieName, 0.0, CookingTime, cooking, mix, dough, flavour, toppingList);
        cod.suggestRecipe(newCookie);
    }
}

