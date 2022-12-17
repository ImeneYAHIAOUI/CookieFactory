package fr.unice.polytech.entities.recipe.ingredients;

import fr.unice.polytech.entities.recipe.enums.IngredientType;
import fr.unice.polytech.exception.IngredientTypeException;
import fr.unice.polytech.interfaces.IngredientFinder;

import java.util.UUID;

public class IngredientFactory {
    IngredientFinder ingredientFinder;

    public IngredientFactory(){}

    public Ingredient createIngredient(IngredientFinder ingredientFinder, String name, double price, IngredientType ingredientType) throws IngredientTypeException {
        if(ingredientType==null)
            throw new IngredientTypeException("IngredientType doesn't exist");
        this.ingredientFinder=ingredientFinder;
        UUID id;
        do{
            id = UUID.randomUUID();
        }while (ingredientFinder.containsById(id));
        switch (ingredientType){
            case DOUGH -> {
                return new Dough(id,name, price);
            }
            case FLAVOUR -> {
                return new Flavour(id,name, price);
            }
            case TOPPING -> {
                return new Topping(id,name, price);
            }
            default -> throw new IngredientTypeException("IngredientType "+ingredientType+" non possible");

        }
    }
}
