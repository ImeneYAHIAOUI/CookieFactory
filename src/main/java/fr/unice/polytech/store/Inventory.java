package fr.unice.polytech.store;

import fr.unice.polytech.order.Item;
import fr.unice.polytech.recipe.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<Ingredient> stock;

    public Inventory(){
        stock = new ArrayList<>();
    }

    public void fillInventory(List<Ingredient> Ingredient){}
    public void fill(List<Item> items){

    }
    public void use(List<Item> items){

    }
}
