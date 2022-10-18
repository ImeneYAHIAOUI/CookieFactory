package fr.unice.polytech.store;


import fr.unice.polytech.client.Cart;
import fr.unice.polytech.recipe.Cookie;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class Store {
    public List<Cook> cooks;
    public List<Cookie> recipes;
    public String address;
    public LocalTime openingTime;
    public LocalTime closingTime;
    public int id;

    public Store(List<Cook> cooks, List<Cookie> recipes, String address, LocalTime openningTime, LocalTime closingTime,int id) {
        this.cooks = cooks;
        this.recipes = recipes;
        this.address = address;
        this.openingTime = openningTime;
        this.closingTime = closingTime;
        this.id = id;
    }


    public  List<Cookie> getRecipes(){
        return recipes;
    }
    public void setHours(LocalTime openingTime, LocalTime closingTime){
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    @Override
    public String toString() {
        return "Store{" +
                "address='" + address + '\'' +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                '}';
    }

    public Cook getFreeCook(Cart cart){
        //On considère le cook toujours libre pour l'instant
        return cooks.get(0);
    }

}
