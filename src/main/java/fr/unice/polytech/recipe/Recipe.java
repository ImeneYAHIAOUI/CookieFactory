package fr.unice.polytech.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
public  class Recipe {
    @Getter
    @Setter
    protected String name;
    @Getter
    @Setter
    protected Double price;
    @Getter
    @Setter
    protected int cookingTime;
    @Getter
    @Setter
    protected Cooking cooking;
    @Getter
    @Setter
    protected Mix mix;
    @Getter
    @Setter
    protected Dough dough;
    @Getter
    @Setter
    protected Flavour flavour;
    @Getter
    @Setter
    protected List<Topping> toppings;

    public void addTopping(Topping topping) {
        toppings.add(topping);
    }


}
