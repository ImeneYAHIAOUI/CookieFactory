package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.recipe.enums.IngredientType;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.exception.IngredientTypeException;

public interface IngredientRegistration {

    void addIngredient(String name, double price, IngredientType ingredientType) throws IngredientTypeException;
    void removeIngredient(String name, IngredientType ingredientType);
}