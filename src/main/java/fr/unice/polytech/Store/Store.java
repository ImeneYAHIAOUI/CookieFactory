package fr.unice.polytech.Store;

import fr.unice.polytech.Order.Item;
import fr.unice.polytech.Order.Order;
import fr.unice.polytech.Recipe.Cookie;

import java.util.Date;
import java.util.List;

public class Store {
    public List<Cook> cooks;
    public List<Cookie> cookies;
    public Inventory inventory;
    public String adress;
    public Double tax;
    public Date openningTime;
    public Date closingTime;


    public  List<Cookie> getCookieList(){
        return null;
    }
    public List<TimeSlot> getFreeTimeSlot(Order order){
        return null;
    }
    public void setHours(Date openningTime, Date closingTime){

    }
    public Cook getFreeCook(Order order){
        return null;
    }
    public void useInventory(List<Item> items){

    }

}
