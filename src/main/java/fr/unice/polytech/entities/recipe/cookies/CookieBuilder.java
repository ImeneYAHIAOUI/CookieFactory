package fr.unice.polytech.entities.recipe.cookies;

import fr.unice.polytech.entities.recipe.enums.CookieSize;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Topping;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Abstract builder for {@link Cookie} objects.
 */
public abstract class CookieBuilder {
    protected CookieSize size;
    protected String name;
    protected Double price;
    protected int cookingTime;
    protected Cooking cooking;
    protected Mix mix;
    protected Dough dough;
    protected Flavour flavour;
    protected Collection<Topping> toppings;

    public CookieBuilder() {
        toppings = new ArrayList<>();
    }

    public CookieBuilder setSize(CookieSize size) {
        this.size = size;
        return this;
    }

    public CookieBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CookieBuilder setPrice(Double price) {
        this.price = price;
        return this;
    }

    public CookieBuilder setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public CookieBuilder setCooking(Cooking cooking) {
        this.cooking = cooking;
        return this;
    }

    public CookieBuilder setMix(Mix mix) {
        this.mix = mix;
        return this;
    }

    public CookieBuilder setDough(Dough dough) {
        this.dough = dough;
        return this;
    }

    public CookieBuilder setFlavour(Flavour flavour) {
        this.flavour = flavour;
        return this;
    }

    public CookieBuilder setToppings(Collection<Topping> toppings) {
        this.toppings = toppings;
        return this;
    }
}
