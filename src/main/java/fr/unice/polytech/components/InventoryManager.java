package fr.unice.polytech.components;

import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.recipe.cookies.PartyCookie;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.BadQuantityException;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.interfaces.InventoryFiller;
import fr.unice.polytech.interfaces.IngredientFinder;
import fr.unice.polytech.interfaces.InventoryUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryManager implements InventoryFiller, InventoryUpdater {


    private final IngredientFinder ingredientManager;
    @Autowired
    public InventoryManager( IngredientFinder ingredientFinder){
        this.ingredientManager=ingredientFinder;
    }
    public void addIngredients(Ingredient ingredient, int quantity, Store store) throws BadQuantityException, CatalogException {
        if(! ingredientManager.containsById(ingredient.getId())){
            throw new CatalogException("Ingredient not found");
        }
        if (quantity < 0) {
            throw new BadQuantityException("Quantity cannot be under 0");
        }
        addIngredient(ingredient,quantity,store.getInventory());
    }

    private void addIngredient(Ingredient ingredient, int quantity, Inventory inventory) {
        if(inventory.containsKey(ingredient))
            inventory.replace(ingredient, quantity+inventory.get(ingredient));
        else
            inventory.put(ingredient, quantity);
    }


    /**
     * Put back the ingredients in the store inventory
     * @param items the items of the cancelled order
     * @param store the store where the order was made
     */
    public  void putBackIngredientsInInventory(List<Item> items, Store store)
            throws CatalogException, BadQuantityException
    {
        for (Item item : items) {
            Cookie cookie = item.getCookie();
            int factor = 1;
            if(cookie instanceof PartyCookie){
                factor = ((PartyCookie) cookie).getSize().getMultiplier();
            }
            int numberOfCookie = item.getQuantity();
            addIngredients(item.getCookie().getDough(), numberOfCookie*factor, store);
            addIngredients(item.getCookie().getFlavour(), numberOfCookie*factor, store);
            //topping
            for (Ingredient topping : cookie.getToppings()) {
                addIngredients(topping, numberOfCookie*factor, store);
            }

        }
    }

    @Override
    public boolean hasIngredient(Ingredient ingredient,Inventory inventory) {
        return inventory.containsKey(ingredient);
    }

    @Override
    public void addAmountQuantity(Ingredient ingredient, int quantity,Inventory inventory) {
        inventory.replace(ingredient, inventory.get(ingredient) + quantity);
    }

    @Override
    public void decreaseIngredientQuantity(Ingredient ingredient, int quantity,Inventory inventory)
            throws BadQuantityException
    {
        if (! hasIngredient(ingredient,inventory)){
            inventory.put(ingredient,0);
        } if(inventory.get(ingredient) - quantity < 0){
            throw new BadQuantityException("Negative quantity not allow");
        }
        inventory.replace(ingredient, inventory.get(ingredient) - quantity);

    }
}
