package fr.unice.polytech.entities.store;

import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import java.util.HashMap;
import java.util.List;

public class Inventory extends HashMap<Ingredient, Integer> {

    public Inventory(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            this.put(ingredient, 0);
        }
    }
}

