package fr.unice.polytech.recipe;

import java.util.List;

public class Cookie {

    private String name;
    private Double Price;
    private Double CookingTime;
    private Cooking cooking;
    private Mix mix;
    private Dough dough;
    private Flavour flavour;
    private List<Topping> toppingList;



    public Cookie(String name, Double price, Double cookingTime, Cooking cooking, Mix mix, Dough dough, Flavour flavour, List<Topping> toppingList) {

        this.name =name;
        this.Price = price;
        this.CookingTime = cookingTime;
        this.cooking = cooking;
        this.mix = mix;
        this.dough = dough;
        this.flavour = flavour;
        this.toppingList = toppingList;

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
        this.name = name;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "name='" + name + '\'' +
                ", Price=" + Price +
                ", CookingTime=" + CookingTime +
                ", cooking=" + cooking +
                ", mix=" + mix +
                ", dough=" + dough +
                ", flavour=" + flavour +
                ", toppingList=" + toppingList +
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

    public String getName() {
        return name;
    }

    public void addTopping(Topping topping){
        toppingList.add(topping);
    }


    public Ingredient getDough()
    {
        return dough;
    }

    public Ingredient getFlavor()
    {
        return flavour;
    }

    public List<Topping> getToppings()
    {
        return toppingList;
    }


}
