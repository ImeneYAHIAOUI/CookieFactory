package fr.unice.polytech.recipe;

import fr.unice.polytech.exception.IngredientTypeException;

public class IngredientFactory {

    public IngredientFactory(){}

    public Ingredient createIngredient(String name, double price, IngredientType ingredientType) throws IngredientTypeException {
        switch (ingredientType){
            case DOUGH -> {
                return new Dough(name, price);
            }
            case FLAVOUR -> {
                return new Flavour(name, price);
            }
            case TOPPING -> {
                return new Topping(name, price);
            }
            default -> throw new IngredientTypeException("IngredientType "+ingredientType+" non possible");

        }
    }
}
