package fr.unice.polytech.entities.recipe.cookies;

import fr.unice.polytech.entities.recipe.enums.CookieSize;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.ingredients.*;
import fr.unice.polytech.exception.CookieException;
import lombok.*;

import java.util.List;
import java.util.UUID;


@Data
public class Cookie {
    protected CookieSize size;
    protected String name;
    protected Double price;
    protected int cookingTime;
    protected Cooking cooking;
    protected Mix mix;
    protected Dough dough;
    protected Flavour flavour;
    protected final List<Topping> toppings;
    protected final UUID id;

    Cookie(String name, double price, int cookingTime,
           Cooking cooking, Mix mix, Dough dough, Flavour flavour,
           List<Topping> toppings) throws CookieException {
        this.id = UUID.randomUUID();
        this.name = name;
        this.price = price;
        this.cookingTime = cookingTime;
        this.cooking = cooking;
        this.mix = mix;
        this.dough = dough;
        this.flavour = flavour;
        assert toppings != null;
        if (toppings.size() <= 3) {
            this.toppings = toppings;
        } else {
            throw new CookieException("Too many toppings");
        }
    }

    public String toString() {
        return "Cookie {" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", cookingTime=" + cookingTime +
                ", cooking=" + cooking +
                ", mix=" + mix +
                ", dough=" + dough +
                ", flavour=" + flavour +
                ", toppings=" + toppings +
                '}';
    }
}
