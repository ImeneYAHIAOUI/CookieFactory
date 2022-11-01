package fr.unice.polytech.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@AllArgsConstructor
public class Cookie {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Double price;
    @Getter
    @Setter
    private int cookingTime;
    @Getter
    @Setter
    private Cooking cooking;
    @Getter
    @Setter
    private Mix mix;
    @Getter
    @Setter
    private Dough dough;
    @Getter
    @Setter
    private Flavour flavour;
    @Getter
    @Setter
    private List<Topping> toppings;

    public void addTopping(Topping topping) {
        toppings.add(topping);
    }
}
