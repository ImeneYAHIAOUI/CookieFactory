package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.BadQuantityException;
import fr.unice.polytech.exception.CatalogException;

import java.util.List;

public interface InventoryUpdater {
    void putBackIngredientsInInventory(List<Item> items, Store store) throws BadQuantityException, CatalogException;
    void decreaseIngredientQuantity(Ingredient ingredient, int quantity, Inventory inventory)
            throws BadQuantityException;
}
