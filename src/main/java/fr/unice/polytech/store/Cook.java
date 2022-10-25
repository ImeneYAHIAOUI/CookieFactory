package fr.unice.polytech.store;

import fr.unice.polytech.COD;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderException;
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
        Cookie newCookie=new Cookie(cookieName);
        newCookie.setCookingTime(CookingTime);
        newCookie.setCooking(cooking);
        newCookie.setDough(dough);
        newCookie.setMix(mix);
        newCookie.setFlavour(flavour);
        newCookie.setToppingList(toppingList);
        cod.suggestRecipe(newCookie);
    }
}

