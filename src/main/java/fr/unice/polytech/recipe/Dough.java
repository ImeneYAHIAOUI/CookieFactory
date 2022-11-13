package fr.unice.polytech.recipe;

public class Dough extends Ingredient {
    public Dough(String name, double price) {
        super(name, price);
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
