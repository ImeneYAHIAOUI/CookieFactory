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
    public Cookie(String name){
        this.Name=name;
    }

    public void setCooking(Cooking cooking) {
        this.cooking = cooking;
    }

    public void setCookingTime(Double cookingTime) {
        CookingTime = cookingTime;
    }

    public void setMix(Mix mix) {
        this.mix = mix;
    }

    public void setDough(Dough dough) {
        this.dough = dough;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "Name='" + Name + '\'' +
                '}';
    }

    public void setFlavour(Flavour flavour) {
        this.flavour = flavour;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public void setToppingList(List<Topping> toppingList) {
        this.toppingList = toppingList;
    }

    public void addTopping(Topping topping){
        toppingList.add(topping);
    }


}
