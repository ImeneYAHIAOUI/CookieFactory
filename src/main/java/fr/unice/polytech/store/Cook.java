package fr.unice.polytech.store;

import fr.unice.polytech.COD;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.recipe.*;

import java.util.ArrayList;
import java.util.List;

public class Cook {
    int id;
    public List<TimeSlot> avaibleTimeSlot;
    public COD cod;
    public List<TimeSlot> getTimeSlot(){
        return avaibleTimeSlot;
    }
    public List<Order> assignedOrders;

    public Cook(int id)
    {
        this.id = id;
        this.assignedOrders = new ArrayList<>();
    }

    public void addOrder(Order order){

        assignedOrders.add(order);
    }
    public void suggestRecipe(String cookieName, Double CookingTime, Cooking cooking, Mix mix, Dough dough , Flavour flavour, List<Topping> toppingList){
        Cookie newCookie = new Cookie(cookieName, 0.0, CookingTime, cooking, mix, dough, flavour, toppingList);
        cod.suggestRecipe(newCookie);
    }
}

