package fr.unice.polytech.entities.recipe;

import fr.unice.polytech.exception.CookieException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class PartyCookieWithBaseRecipe extends PartyCookie {
    @Getter
    private final List<Ingredient> addedIngredients;
    @Getter
    private final List<Ingredient> removedIngredients;

    @Getter
    private final Cookie baseCookie;

     PartyCookieWithBaseRecipe(Cookie cookie, CookieSize size, Theme theme, List<Ingredient> addedIngredients, List<Ingredient> removedIngredients) throws CookieException {
        super("personalized "+cookie.getName(), cookie.getCookingTime()* size.getMultiplier(), cookie.getCooking(), cookie.getMix(), cookie.getDough(), cookie.getFlavour(), cookie.getToppings(), size, theme);
        this.baseCookie = cookie;
        List<Ingredient> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(cookie.getFlavour());
        recipeIngredients.add(cookie.getDough());
        recipeIngredients.addAll(cookie.getToppings());
        if(removedIngredients.stream().anyMatch(ingredient -> ! recipeIngredients.contains(ingredient))){
            throw new CookieException("You can't remove ingredients that are not in the recipe");
        }
        long addedDoughCount = addedIngredients.stream().filter(ingredient -> ingredient instanceof Dough).count();
        long removedDoughCount = removedIngredients.stream().filter(ingredient -> ingredient instanceof Dough).count();
        if(addedDoughCount - removedDoughCount > 4){
            throw new CookieException("You can't have more than 4 doughs in one party cookie");
        }
        this.addedIngredients = addedIngredients;
        this.removedIngredients = removedIngredients;
        setPrice();
     }



    @Override
    public void setPrice() {
        double additionalPrice = addedIngredients.stream().mapToDouble(Ingredient::getPrice).sum()*getSize().getMultiplier();
        double removedPrice = removedIngredients.stream().mapToDouble(Ingredient::getPrice).sum()*getSize().getMultiplier();
        setPrice(baseCookie.getPrice()*getSize().getMultiplier()+additionalPrice-removedPrice);

    }
}

