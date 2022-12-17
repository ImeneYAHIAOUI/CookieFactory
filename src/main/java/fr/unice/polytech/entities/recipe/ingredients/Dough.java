package fr.unice.polytech.entities.recipe.ingredients;

import fr.unice.polytech.entities.recipe.enums.IngredientType;

import java.util.UUID;

public class Dough extends Ingredient {
    public Dough(UUID id, String name, double price) {
        super(id,name, price);
    }

    @Override
    public String toString() {
        return "Dough{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public IngredientType getIngredientType() {
        return IngredientType.DOUGH;
    }
}
