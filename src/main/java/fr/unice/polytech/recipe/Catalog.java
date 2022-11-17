package fr.unice.polytech.recipe;

import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.exception.IngredientTypeException;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
    private static List<Ingredient> INGREDIENTS = new ArrayList<>();
    private static IngredientFactory  INGREDIENTFACTORY = new IngredientFactory();

    public Catalog(){
        INGREDIENTS = new ArrayList<>();
        INGREDIENTFACTORY = new IngredientFactory();
    }

    private boolean contains(String name){
        for (Ingredient i: INGREDIENTS) {
            if(i.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;

    }

    public void addIngredient(String name, double price, IngredientType ingredientType) throws CatalogException, IngredientTypeException {
        if(contains(name))
            throw new CatalogException("The ingredient "+name+" is already in the catalog.");

        INGREDIENTS.add(INGREDIENTFACTORY.createIngredient(name, price, ingredientType));
    }

    public Ingredient getIngredient(String name) throws CatalogException {
        for (Ingredient i: INGREDIENTS) {
            if(i.getName().equalsIgnoreCase(name))
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
