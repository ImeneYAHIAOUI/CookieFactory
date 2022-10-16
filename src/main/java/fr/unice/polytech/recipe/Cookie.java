package fr.unice.polytech.recipe;

import java.util.List;

public class Cookie {
    public String Name;
    public Double Price;
    public Double CookingTime;
    public Cooking cooking;
    public Mix mix;
    public Dough dough;
    public Flavour flavour;
    public List<Topping> toppingList;

    public Cookie(String name, Double price, Double cookingTime, Cooking cooking, Mix mix, Dough dough, Flavour flavour, List<Topping> toppingList) {
        Name = name;
        Price = price;
        CookingTime = cookingTime;
        this.cooking = cooking;
        this.mix = mix;
        this.dough = dough;
        this.flavour = flavour;
        this.toppingList = toppingList;
    }

    public void addTopping(Topping topping){

    }

}
