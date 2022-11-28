package fr.unice.polytech.entities.recipe;

public class Topping extends Ingredient {
    public Topping(String name, double price) {
        super(name, price);
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
