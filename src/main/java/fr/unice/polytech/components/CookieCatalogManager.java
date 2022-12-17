package fr.unice.polytech.components;

import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.IngredientType;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.interfaces.CookieFinder;
import fr.unice.polytech.interfaces.CookieRegistration;
import fr.unice.polytech.interfaces.IngredientFinder;
import fr.unice.polytech.repositories.CookieRepository;
import fr.unice.polytech.repositories.SuggestedCookieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CookieCatalogManager implements CookieRegistration, CookieFinder {

    CookieRepository cookieRepository;
    SuggestedCookieRepository suggestedCookieRepository;
    IngredientFinder ingredientFinder;

    @Autowired
    public CookieCatalogManager(CookieRepository cookieRepository, SuggestedCookieRepository suggestedCookieRepository, IngredientFinder ingredientFinder) {
        this.cookieRepository = cookieRepository;
        this.suggestedCookieRepository = suggestedCookieRepository;
        this.ingredientFinder = ingredientFinder;
    }
    @Override
    public void suggestRecipe(Cookie cookie) {
        suggestedCookieRepository.save(cookie, cookie.getId());
    }

    /**
     * Check if the ingredients of the recipes are in the catalog and if they are of the right Ingredient Type
     * before adding the recipe in the suggestedRecipes List
     * @param name of the new recipe
     * @param price of the new recipe
     * @param time to cook the recipe
     * @param cooking_chosen Cooking of the recipe
     * @param mix_chosen Mix of the recipe
     * @param doughIngredient Dough of the recipe
     * @param flavourIngredient Flavour of the recipe
     * @param toppings Toppings of the recipe
     * @throws CatalogException if one of the ingredients does not exist
     */
    @Override
    public void suggestRecipe(String name, double price, int time, Cooking cooking_chosen, Mix mix_chosen, Ingredient doughIngredient, Ingredient flavourIngredient, List<Ingredient> toppings) throws CatalogException, CookieException {
        List<Topping> toppingList = new ArrayList<>();
        for (Ingredient i: toppings) {
            if(ingredientFinder.containsByName(i.getName()) && i.getIngredientType().equals(IngredientType.TOPPING))
                toppingList.add((Topping) i);
            else
                throw new CatalogException("Bad Ingredient Type");
        }
        if(doughIngredient.getIngredientType().equals(IngredientType.DOUGH)
                && flavourIngredient.getIngredientType().equals(IngredientType.FLAVOUR)
                && ingredientFinder.containsByName(doughIngredient.getName())
                && ingredientFinder.containsByName((flavourIngredient.getName()))
        ) {
            Cookie suggested = new SimpleCookieBuilder()
                    .setName(name)
                    .setPrice(price)
                    .setCookingTime(time)
                    .setCooking(cooking_chosen)
                    .setMix(mix_chosen)
                    .setDough((Dough) doughIngredient)
                    .setFlavour((Flavour) flavourIngredient)
                    .setToppings(toppingList)
                    .build();
            suggestRecipe(suggested);
        }
        else
            throw new CatalogException("Bad Ingredient Type");
    }

    @Override
    public void acceptRecipe(Cookie cookie, Double price) {
        cookie.setPrice(price);
        cookieRepository.save(cookie, cookie.getId());
        suggestedCookieRepository.deleteById(cookie.getId());
    }

    @Override
    public void declineRecipe(Cookie cookie) {
        suggestedCookieRepository.deleteById(cookie.getId());
    }

    @Override
    public List<Cookie> getRecipes() {
        List<Cookie> recipes = new ArrayList<>();
        for (Cookie c: cookieRepository.findAll()) {
            recipes.add(c);
        }
        return recipes;
    }

    @Override
    public List<Cookie> getSuggestedRecipes() {
        List<Cookie> recipes = new ArrayList<>();
        for (Cookie c: suggestedCookieRepository.findAll()) {
            recipes.add(c);
        }
        return recipes;
    }


}
