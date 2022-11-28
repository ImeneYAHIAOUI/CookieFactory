package fr.unice.polytech.entities.recipe;

import fr.unice.polytech.exception.CookieException;

import java.util.List;

public class CookieFactory {

    public static Cookie createSimpleCookie(String name, double price, int cookingTime, Cooking cooking, Mix mix, Dough dough, Flavour flavour, List<Topping> toppings) throws CookieException {
        return new Cookie(name, price, cookingTime, cooking, mix, dough, flavour, toppings);
    }

    public static PartyCookieWithBaseRecipe createPartyCookieWithBaseCookie(Cookie cookie, CookieSize size, Theme theme, List<Ingredient> addedIngredients, List<Ingredient> removedIngredients) throws CookieException {
        return new PartyCookieWithBaseRecipe(cookie,size, theme, addedIngredients, removedIngredients);
    }

    public static PartyCookieFromScratch createPartyCookieFromScratch(CookieSize size, Theme theme, String name, Cooking cooking, Mix mix, Dough dough, Flavour flavour, List<Topping> toppings, List<Ingredient> supplementaryIngredients) throws CookieException {
        return new PartyCookieFromScratch(size, theme, name, cooking, mix, dough, flavour, toppings, supplementaryIngredients);
    }

}
