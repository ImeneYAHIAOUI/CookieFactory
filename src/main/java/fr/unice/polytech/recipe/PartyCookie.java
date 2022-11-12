package fr.unice.polytech.recipe;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PartyCookie extends Recipe{
    @Getter
    @Setter
    private Size size;
    @Getter
    @Setter
    private Theme theme;

    public PartyCookie( Theme theme, Size size,String name, Double price,
                        int cookingTime, Cooking cooking, Mix mix, Dough dough,
                        Flavour flavour, List<Topping> toppings){
        super( name, price,  cookingTime, cooking,  mix, dough,  flavour,toppings);
        this.size=size;
        this.theme=theme;
    }

}
