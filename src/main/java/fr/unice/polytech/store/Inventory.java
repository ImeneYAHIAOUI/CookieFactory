package fr.unice.polytech.store;

import fr.unice.polytech.exception.BadQuantityException;
import fr.unice.polytech.recipe.Ingredient;

import java.util.HashMap;
import java.util.List;

public class Inventory extends HashMap<Ingredient, Integer> {

    public Inventory(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            this.put(ingredient, 0);
        }
    }

    public void addIngredient(Ingredient ingredient, int quantity) {
        this.put(ingredient, quantity);
    }


    public boolean hasIngredient(Ingredient ingredient) {
        return this.containsKey(ingredient);
    }

    public void addAmountQuantity(Ingredient ingredient, int quantity) {
        this.replace(ingredient, this.get(ingredient) + quantity);
    }

    public void decreaseIngredientQuantity(Ingredient ingredient, int quantity) throws BadQuantityException {
        if (this.get(ingredient) - quantity >= 0) {
            this.replace(ingredient, this.get(ingredient) - quantity);
        } else {
            System.out.println(this.get(ingredient) - quantity);
            throw new BadQuantityException("Negative quantity not allow");
        }


    }


}

