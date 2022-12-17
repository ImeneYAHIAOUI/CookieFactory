package fr.unice.polytech.entities.recipe.cookies;

import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.recipe.enums.CookieSize;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.enums.Theme;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.exception.CookieException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Concrete builder for {@link PartyCookieWithBaseRecipe} objects.
 */
public class PartyCookieFromScratchBuilder extends PartyCookieBuilder {
    private Collection<Ingredient> supplementaryIngredients;

    public PartyCookieFromScratchBuilder() {
        super();
        supplementaryIngredients = new ArrayList<>();
    }

    public PartyCookieFromScratchBuilder setSupplementaryIngredients(Collection<Ingredient> supplementaryIngredients) {
        this.supplementaryIngredients = supplementaryIngredients;
        return this;
    }

    public PartyCookieFromScratchBuilder setSize(CookieSize size) {
        super.setSize(size);
        return this;
    }

    
    public PartyCookieFromScratchBuilder setName(String name) {
        super.setName(name);
        return this;
    }

    
    public PartyCookieFromScratchBuilder setPrice(Double price) {
        super.setPrice(price);
        return this;
    }

    
    public PartyCookieFromScratchBuilder setCookingTime(int cookingTime) {
        super.setCookingTime(cookingTime);
        return this;
    }

    
    public PartyCookieFromScratchBuilder setCooking(Cooking cooking) {
        super.setCooking(cooking);
        return this;
    }

    
    public PartyCookieFromScratchBuilder setMix(Mix mix) {
        super.setMix(mix);
        return this;
    }

    
    public PartyCookieFromScratchBuilder setDough(Dough dough) {
        super.setDough(dough);
        return this;
    }

    
    public PartyCookieFromScratchBuilder setFlavour(Flavour flavour) {
        super.setFlavour(flavour);
        return this;
    }

    
    public PartyCookieFromScratchBuilder setToppings(Collection<Topping> toppings) {
        super.setToppings(toppings);
        return this;
    }

    
    public PartyCookieFromScratchBuilder setTheme(Theme theme) {
        super.setTheme(theme);
        return this;
    }

    
    public PartyCookieFromScratch build() {
        try {
            return new PartyCookieFromScratch(
                    size,
                    theme,
                    name,
                    cooking,
                    mix,
                    dough,
                    flavour,
                    toppings.stream().toList(),
                    supplementaryIngredients.stream().toList()
            );
        } catch (CookieException e) {
            e.printStackTrace();
            return null;
        }
    }
}
