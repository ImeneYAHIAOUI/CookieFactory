package fr.unice.polytech.store;


import fr.unice.polytech.client.Cart;
import fr.unice.polytech.recipe.Cookie;

import java.util.Date;
import java.util.List;

public class Store {
    public List<Cook> cooks;
    public List<Cookie> recipes;
    public String address;
    public Date openingTime;
    public Date closingTime;

    public Store(List<Cook> cooks, List<Cookie> recipes, String address, Date openningTime, Date closingTime) {
        this.cooks = cooks;
        this.recipes = recipes;
        this.address = address;
        this.openingTime = openningTime;
        this.closingTime = closingTime;
    }


    public  List<Cookie> getRecipes(){
        return recipes;
    }
    public void setHours(Date openingTime, Date closingTime){
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
