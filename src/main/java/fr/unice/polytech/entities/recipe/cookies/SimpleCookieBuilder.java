package fr.unice.polytech.entities.recipe.cookies;

import fr.unice.polytech.entities.recipe.enums.CookieSize;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.exception.CookieException;

import java.util.Collection;

public class SimpleCookieBuilder extends CookieBuilder {

    public SimpleCookieBuilder setSize(CookieSize size) {
        super.setSize(size);
        return this;
    }

    public SimpleCookieBuilder setName(String name) {
        super.setName(name);
        return this;
    }

    public SimpleCookieBuilder setPrice(Double price) {
        super.setPrice(price);
        return this;
    }

    public SimpleCookieBuilder setCookingTime(int cookingTime) {
        super.setCookingTime(cookingTime);
        return this;
    }

    public SimpleCookieBuilder setCooking(Cooking cooking) {
        super.setCooking(cooking);
        return this;
    }

    public SimpleCookieBuilder setMix(Mix mix) {
        super.setMix(mix);
        return this;
    }

    public SimpleCookieBuilder setDough(Dough dough) {
        super.setDough(dough);
        return this;
    }

    public SimpleCookieBuilder setFlavour(Flavour flavour) {
        super.setFlavour(flavour);
        return this;
    }

    public SimpleCookieBuilder setToppings(Collection<Topping> toppings) {
        super.setToppings(toppings);
        return this;
    }

    public Cookie build() throws CookieException {
        return new Cookie(
                name,
                price,
                cookingTime,
                cooking,
                mix,
                dough,
                flavour,
                toppings.stream().toList()
        );
    }
}
