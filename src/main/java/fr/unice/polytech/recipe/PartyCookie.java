package fr.unice.polytech.recipe;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PartyCookie extends Cookie{
    @Getter
    private final CookieSize size;
    @Getter
    private final Theme theme;

    public PartyCookie(String name, double price, int cookingTime, Cooking cooking, Mix mix, Dough dough, Flavour flavour, List<Topping> toppings,CookieSize size,Theme theme) {
        super(name, price, cookingTime,cooking, mix, dough, flavour, toppings);
        this.size = size;
        this.theme = theme;
    }

}
