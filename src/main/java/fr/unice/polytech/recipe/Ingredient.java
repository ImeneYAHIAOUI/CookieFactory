package fr.unice.polytech.recipe;

public class Ingredient {
    public String name;
    public Double price;

    public Ingredient(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return name;
    }
    public Double getPrice(){
        return price;
    }
}
