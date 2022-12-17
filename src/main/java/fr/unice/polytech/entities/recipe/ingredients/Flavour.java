package fr.unice.polytech.entities.recipe.ingredients;


import fr.unice.polytech.entities.recipe.enums.IngredientType;

import java.util.UUID;

public class Flavour extends Ingredient {
    public Flavour(UUID id,String name, double price) {
        super(id,name, price);
    }

    @Override
    public String toString() {
        return "Flavour{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public IngredientType getIngredientType() {
        return IngredientType.FLAVOUR;
    }
}
