package fr.unice.polytech.store;


import fr.unice.polytech.client.Cart;
import fr.unice.polytech.exception.AlreadyExist;
import fr.unice.polytech.exception.BadQuantity;
import fr.unice.polytech.recipe.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Store {
    private final List<Cook> cooks;
    private final List<Cookie> recipes;
    public String address;
    public LocalTime openingTime;
    public LocalTime closingTime;
    private int id;
    private final Inventory inventory;


    public Store(List<Cook> cooks, List<Cookie> recipes, String address, LocalTime openingTime, LocalTime closingTime,int id,Inventory inventory) {
        this.cooks = cooks;
        this.recipes = recipes;
        this.address = address;
        if (openingTime.isBefore(closingTime)) {
            this.openingTime = openingTime;
            this.closingTime = closingTime;
        } else {
            System.out.println("Error, given closingTime is before openingTime, openingTime get closedTime value and closingTime get openingTime value");
            this.openingTime = closingTime;
            this.closingTime = openingTime;
        }
        this.id = id;
        this.inventory = inventory;
    }


    public  List<Cookie> getRecipes(){
        return recipes;
    }
    public void setHours(LocalTime openingTime, LocalTime closingTime){
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    @Override
    public String toString() {
        return "Store{" +
                "address='" + address + '\'' +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                '}';
    }

    public Cook getFreeCook(Cart cart){
        //On considère le cook toujours libre pour l'instant
        return cooks.get(0);
    }

    public void addIngredients(Ingredient ingredient, int quantity) throws AlreadyExist, BadQuantity {
        if (quantity < 0) {
            throw new BadQuantity("Quantity cannot be under 0");
        }
        if (!this.inventory.hasIngredient(ingredient))
            this.inventory.addIngredient(ingredient,quantity);
        else{
            throw new AlreadyExist();
        }
    }

    public void removeIngredient(Ingredient ingredient){
        this.inventory.removeIngredient(ingredient);
    }
    public Inventory getInventory(){
        return inventory;
    }

    public int getMaxCookieAmount(Cookie cookie) {
        List<Integer> ingredientAmounts = new ArrayList<>();
        ingredientAmounts.add(inventory.get(cookie.getDough()));
        ingredientAmounts.add(inventory.get(cookie.getFlavor()));
        List<Topping> toppingList = cookie.getToppings();

        for (Topping topping : toppingList) {
            ingredientAmounts.add(inventory.get(topping));
        }
        return Collections.min(ingredientAmounts);

    }

    public void addCookies(List<Cookie> cookieList)
    {
        this.recipes.addAll(cookieList);
    }

    public void removeCookies(Ingredient ingredient)
    {
        if (ingredient instanceof Dough)
        {
            recipes.removeIf(cookie -> cookie.getDough().equals(ingredient));
        }

        if (ingredient instanceof Flavour)
        {
            recipes.removeIf(cookie -> cookie.getFlavor().equals(ingredient));
        }

        if (ingredient instanceof Topping)
        {
            recipes.removeIf(cookie -> cookie.getToppings().contains(ingredient));
        }


    }

}
