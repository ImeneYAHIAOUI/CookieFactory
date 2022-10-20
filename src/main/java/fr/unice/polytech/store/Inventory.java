package fr.unice.polytech.store;

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

        public void removeIngredient(Ingredient ingredient) {
            this.remove(ingredient);
        }

        public boolean hasIngredient(Ingredient ingredient) {
            return this.containsKey(ingredient);
        }

        public void addAmountQuantity(Ingredient ingredient,int quantity){
            this.replace(ingredient, this.get(ingredient) + quantity);
        }
        public void suppressAmountQuantity(Ingredient ingredient, int quantity){
            if (this.get(ingredient) - quantity >=0){
                this.replace(ingredient, this.get(ingredient) - quantity);
            }
            else{
                System.out.println("Retrait supérieur à la quantité existante");
            }
        }
}

