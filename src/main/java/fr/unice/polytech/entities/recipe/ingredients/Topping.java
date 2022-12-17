package fr.unice.polytech.entities.recipe.ingredients;

import fr.unice.polytech.entities.recipe.enums.IngredientType;

import java.util.UUID;

public class Topping extends Ingredient {
    public Topping(UUID id, String name, double price) {
        super(id,name, price);
    }

    @Override
    public String toString() {
        return "Topping{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public IngredientType getIngredientType() {
        return IngredientType.TOPPING;
    }
}
