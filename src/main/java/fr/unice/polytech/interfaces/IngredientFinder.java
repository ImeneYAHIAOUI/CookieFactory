package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.exception.CatalogException;

import java.util.List;
import java.util.UUID;

public interface IngredientFinder {
    Ingredient getIngredientById(UUID id) throws CatalogException;
    Ingredient getIngredientByName(String name) throws CatalogException;

    boolean containsById(UUID id);
    boolean containsByName(String name);
    void deleteAllIngredients();
    List<Ingredient> getIngredients();
}
