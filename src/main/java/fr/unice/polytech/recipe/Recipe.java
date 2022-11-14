package fr.unice.polytech.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
public  class Recipe {
    @Getter
    protected String name;
    @Getter
    @Setter
    protected Double price;
    @Getter
    protected int cookingTime;
    @Getter
    protected Cooking cooking;
    @Getter
    protected Mix mix;
    @Getter
    protected Dough dough;
    @Getter
    protected Flavour flavour;
    @Getter
    protected List<Topping> toppings;

}
