package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.BadQuantityException;
import fr.unice.polytech.exception.CatalogException;

public interface InventoryFiller {
    boolean hasIngredient(Ingredient ingredient,Inventory inventory);
    void addIngredients(Ingredient ingredient, int quantity, Store store) throws BadQuantityException, CatalogException;
    void addAmountQuantity(Ingredient ingredient, int quantity,Inventory inventory);
}
