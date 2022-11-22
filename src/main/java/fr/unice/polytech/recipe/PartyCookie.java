package fr.unice.polytech.recipe;

import fr.unice.polytech.exception.CookieException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class PartyCookie extends Cookie{


    @Getter
    List<Ingredient> additionalIngredients = new ArrayList<>();
    @Getter
    List<Ingredient> removedIngredients = new ArrayList<>();
    public PartyCookie(Cookie cookie,CookieSize size,Theme theme) {
        super(cookie.getName(), cookie.getPrice(), cookie.getCookingTime(),cookie.getCooking(), cookie.getMix(), cookie.getDough()
                , cookie.getFlavour(), cookie.getToppings());
        setSize(size);
        setTheme(theme);
    }

    public void setAdditionalIngredients(List<Ingredient> ingredients){
        additionalIngredients=ingredients;
        double additionalPrice = 0;
        for(Ingredient ingredient:ingredients){
            additionalPrice += ingredient.getPrice()*getSize().getMultiplier();
        }
        setPrice(getPrice()+additionalPrice);
    }

    public  void setRemovedIngredients(List<Ingredient> ingredients) throws CookieException {
        removedIngredients=ingredients;
        double removedPrice = 0;
        for(Ingredient ingredient:ingredients){
            if(  getToppings().contains(ingredient) || getDough().equals(ingredient) || getFlavour().equals(ingredient))
            {
                removedPrice += ingredient.getPrice()*getSize().getMultiplier();
            }
            else
                System.err.println("Ingredient "+ingredient.getName()+" is not in the cookie recipe");

        }
        setPrice(getPrice()-removedPrice);
    }


}
