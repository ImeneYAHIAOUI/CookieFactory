package fr.unice.polytech.recipe;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class Ingredient {
    @Getter
    private final String name;
    @Getter
    private final double price;
    @Getter
    private int quantity;
}
