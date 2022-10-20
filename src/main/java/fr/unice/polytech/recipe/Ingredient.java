package fr.unice.polytech.recipe;

public class Ingredient {
    private String name;
    private double price;

    public Ingredient(String name,double price) {
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return name;
    }
    public double getPrice(){
        return price;
    }
}
