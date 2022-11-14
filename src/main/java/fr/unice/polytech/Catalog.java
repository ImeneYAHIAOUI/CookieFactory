package fr.unice.polytech;

import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.recipe.*;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
    private static List<Ingredient> INGREDIENTS = new ArrayList<>();

    public Catalog(){
        INGREDIENTS = new ArrayList<>();
    }

    private boolean contains(String name){
        for (Ingredient i: INGREDIENTS) {
            if(i.getName().equals(name))
                return true;
        }
        return false;
    }

    public void addIngredient(String name, double price, IngredientType ingredientType) throws CatalogException {
        if(contains(name))
            throw new CatalogException("The ingredient "+name+" is already in the catalog.");

        switch (ingredientType){
            case DOUGH -> INGREDIENTS.add(new Dough(name, price));
            case FLAVOUR -> INGREDIENTS.add(new Flavour(name, price));
            case TOPPING -> INGREDIENTS.add(new Topping(name, price));
        }
    }

    public Ingredient getIngredient(String name) throws CatalogException {
        for (Ingredient i: INGREDIENTS) {
            if(i.getName().equals(name))
                return i;
        }
        throw new CatalogException("The ingredient "+name+" is not in the catalog.");
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder("Catalog : \n");
        for (Ingredient i: INGREDIENTS) {
            s.append(i).append("\n");
        }
        return s.toString();
    }
}
