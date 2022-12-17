package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.exception.CookieException;

import java.util.List;

public interface CookieRegistration {
    void suggestRecipe(Cookie cookie);

    void suggestRecipe(String name, double price, int time, Cooking cooking_chosen, Mix mix_chosen, Ingredient doughIngredient, Ingredient flavourIngredient, List<Ingredient> toppings) throws CatalogException, CookieException;

    void acceptRecipe(Cookie cookie, Double price);

    void declineRecipe(Cookie cookie);
}
