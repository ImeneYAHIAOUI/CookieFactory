package fr.unice.polytech.recipe;

public class Flavour extends Ingredient {
    public Flavour(String name,double price) {
        super(name, price, IngredientType.FLAVOUR);
    }
}
