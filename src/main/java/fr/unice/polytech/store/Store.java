package fr.unice.polytech.store;

import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.recipe.Cookie;

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

    public Store(List<Cook> cooks, List<Cookie> cookies, String adress, Double tax, Date openningTime, Date closingTime) {
        this.cooks = cooks;
        this.cookies = cookies;
        this.inventory = new Inventory();
        this.adress = adress;
        this.tax = tax;
        this.openningTime = openningTime;
        this.closingTime = closingTime;
    }


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
