package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.enums.CookieSize;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.enums.Theme;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.entities.store.Occasion;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.*;

import java.util.List;

public interface CookieChoice {

    void personalizeCookieFromBaseRecipe(Client client, Store store, Cookie cookie, int amount, CookieSize size, Occasion occasion, Theme theme, List<Ingredient> addedIngredients, List<Ingredient> removedIngredients) throws CookieException, OrderException, ServiceNotAvailable;
    void personalizeCookieFromScratch(Client client, Store store, String cookieName, Cooking cooking, Mix mix, Dough dough, Flavour flavour, List<Topping> toppings, int amount, CookieSize size, Occasion occasion, Theme theme, List<Ingredient> SupplementaryIngredients) throws CookieException, OrderException, ServiceNotAvailable;
    void chooseCookie(Client client, Store store, String cookieName, int amount) throws CookieException, OrderException;
    void chooseCookie(Client client, Store store, Cookie cookie, int amount) throws CookieException, OrderException;


}
