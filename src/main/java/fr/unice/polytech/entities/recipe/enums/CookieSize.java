package fr.unice.polytech.entities.recipe.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
public enum CookieSize {
     L(4,4),XL(5,5), XXL(6,6);
     @Getter
    private final int multiplier;
     @Getter
    private final int baseCookingTime;

}
