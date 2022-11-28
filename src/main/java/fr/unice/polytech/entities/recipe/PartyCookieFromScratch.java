package fr.unice.polytech.entities.recipe;

import fr.unice.polytech.exception.CookieException;
import lombok.Getter;

import java.util.List;

public class PartyCookieFromScratch extends PartyCookie{

    @Getter
    private final List<Ingredient> supplementaryIngredients;


    PartyCookieFromScratch(CookieSize size, Theme theme, String name, Cooking cooking, Mix mix, Dough dough, Flavour flavour, List<Topping> toppings, List<Ingredient> supplementaryIngredients) throws CookieException {
        super(name,size.getBaseCookingTime(), cooking, mix, dough, flavour, toppings, size, theme);
        long addedDoughCount = supplementaryIngredients.stream().filter(ingredient -> ingredient instanceof Dough).count();
        if(addedDoughCount>3){
            throw new CookieException("You can't have more than 4 doughS in a party cookie");
        }
        this.supplementaryIngredients=supplementaryIngredients;
        setPrice();
    }

    @Override
    public void setPrice() {
        double price = getDough().getPrice() + getFlavour().price;
        price += getToppings().stream().mapToDouble(Topping::getPrice).sum();
        price += supplementaryIngredients.stream().mapToDouble(Ingredient::getPrice).sum();
        setPrice(price*getSize().getMultiplier());
    }
}
