package fr.unice.polytech.recipe;

public class Ingredient {
    private final String name;
    private final double price;
    private int quantity;

    public Ingredient(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                '}';
    }

    public int getQuantity() {
        return quantity;
    }
}
