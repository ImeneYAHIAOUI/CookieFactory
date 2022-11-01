package fr.unice.polytech.store;

import fr.unice.polytech.COD;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.recipe.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Cook {
    public final List<Order> assignedOrders;
    @Getter
    private final List<TimeSlot> availableTimeSlots;
    @Getter
    @Setter
    public COD cod;
    final int id;

    public Cook(int id) {
        this.id = id;
        this.availableTimeSlots = new ArrayList<>();
        this.assignedOrders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        assignedOrders.add(order);
    }

    public void suggestRecipe(String cookieName, Double CookingTime, Cooking cooking, Mix mix, Dough dough, Flavour flavour, List<Topping> toppingList) {
        Cookie newCookie = new Cookie(cookieName, 0.0, CookingTime, cooking, mix, dough, flavour, toppingList);
        cod.suggestRecipe(newCookie);
    }
}

