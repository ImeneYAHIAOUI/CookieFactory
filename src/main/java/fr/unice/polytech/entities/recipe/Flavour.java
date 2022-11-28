package fr.unice.polytech.entities.recipe;


public class Flavour extends Ingredient {
    public Flavour(String name,double price) {
        super(name, price);
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
