package fr.unice.polytech.entities.recipe.ingredients;

import fr.unice.polytech.entities.recipe.enums.IngredientType;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@ToString
public abstract class Ingredient {

    @Getter
    private final UUID id;
    @Getter
    protected final String name;
    @Getter
    protected final double price;

    protected Ingredient(UUID id,String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public abstract IngredientType getIngredientType();
}
