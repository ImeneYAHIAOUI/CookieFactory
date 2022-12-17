package fr.unice.polytech.entities.recipe.cookies;

import fr.unice.polytech.entities.recipe.enums.CookieSize;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.enums.Theme;
import fr.unice.polytech.entities.recipe.ingredients.*;
import fr.unice.polytech.exception.CookieException;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public abstract class PartyCookie extends Cookie {
    @Getter
    @Setter
    protected Theme theme;

    PartyCookie(String name, int cookingTime, Cooking cooking, Mix mix, Dough dough, Flavour flavour, List<Topping> toppings, CookieSize size, Theme theme) throws CookieException {
        super(name, 0, cookingTime, cooking, mix, dough, flavour, toppings);
        this.size = size;
        this.theme = theme;
    }

    public abstract void setPrice();
}
