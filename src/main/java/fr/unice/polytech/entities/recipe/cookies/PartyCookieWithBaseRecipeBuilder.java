package fr.unice.polytech.entities.recipe.cookies;

import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.recipe.enums.CookieSize;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.enums.Theme;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.exception.CookieException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Concrete builder for {@link PartyCookieWithBaseRecipe} objects.
 */
public class PartyCookieWithBaseRecipeBuilder extends PartyCookieBuilder {

    private Collection<Ingredient> addedIngredients;
    private Collection<Ingredient> removedIngredients;
    private Cookie baseCookie;

    public PartyCookieWithBaseRecipeBuilder() {
        super();
        this.addedIngredients = new ArrayList<>();
        this.removedIngredients = new ArrayList<>();
    }

    public PartyCookieWithBaseRecipeBuilder setTheme(Theme theme) {
        super.setTheme(theme);
        return this;
    }

    public PartyCookieWithBaseRecipeBuilder setAddedIngredients(Collection<Ingredient> addedIngredients) {
        this.addedIngredients = addedIngredients;
        return this;
    }

    public PartyCookieWithBaseRecipeBuilder setRemovedIngredients(Collection<Ingredient> removedIngredients) {
        this.removedIngredients = removedIngredients;
        return this;
    }

    public PartyCookieWithBaseRecipeBuilder setBaseCookie(Cookie baseCookie) {
        this.baseCookie = baseCookie;
        return this;
    }

    public PartyCookieWithBaseRecipeBuilder setSize(CookieSize size) {
        this.size = size;
        return this;
    }

    public PartyCookieWithBaseRecipeBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PartyCookieWithBaseRecipeBuilder setPrice(Double price) {
        this.price = price;
        return this;
    }

    public PartyCookieWithBaseRecipeBuilder setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public PartyCookieWithBaseRecipeBuilder setCooking(Cooking cooking) {
        this.cooking = cooking;
        return this;
    }

    public PartyCookieWithBaseRecipeBuilder setMix(Mix mix) {
        this.mix = mix;
        return this;
    }

    public PartyCookieWithBaseRecipeBuilder setDough(Dough dough) {
        this.dough = dough;
        return this;
    }

    public PartyCookieWithBaseRecipeBuilder setFlavour(Flavour flavour) {
        this.flavour = flavour;
        return this;
    }

    public PartyCookieWithBaseRecipeBuilder setToppings(Collection<Topping> toppings) {
        this.toppings = toppings;
        return this;
    }

    public PartyCookieWithBaseRecipe build() {
        try {
            return new PartyCookieWithBaseRecipe(
                    baseCookie,
                    size,
                    theme,
                    addedIngredients.stream().toList(),
                    removedIngredients.stream().toList()
            );
        } catch (CookieException e) {
            e.printStackTrace();
            return null;
        }
    }
}
