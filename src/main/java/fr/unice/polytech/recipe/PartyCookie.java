package fr.unice.polytech.recipe;

import fr.unice.polytech.exception.CookieException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public abstract class PartyCookie extends Cookie{


    @Getter
    private final CookieSize size;
    @Getter
    @Setter
    private Theme theme;

     PartyCookie(String name, int cookingTime,Cooking cooking, Mix mix,Dough dough, Flavour flavour,List<Topping> toppings,CookieSize size,Theme theme) throws CookieException {
        super(name, 0,cookingTime, cooking, mix, dough, flavour, toppings);
        this.size=size;
        this.theme=theme;
    }

    public abstract void setPrice();




}
