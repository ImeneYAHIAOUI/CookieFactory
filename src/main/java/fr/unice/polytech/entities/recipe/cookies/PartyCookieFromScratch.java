package fr.unice.polytech.entities.recipe.cookies;

import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.recipe.enums.CookieSize;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.enums.Theme;
import fr.unice.polytech.entities.recipe.ingredients.*;
import fr.unice.polytech.exception.CookieException;
import lombok.Getter;

import java.util.List;

public class PartyCookieFromScratch extends PartyCookie {

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
        double price = getDough().getPrice() + getFlavour().getPrice();
        price += getToppings().stream().mapToDouble(Topping::getPrice).sum();
        price += supplementaryIngredients.stream().mapToDouble(Ingredient::getPrice).sum();
        setPrice(price*getSize().getMultiplier());
    }
}
