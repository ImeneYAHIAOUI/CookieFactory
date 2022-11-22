package fr.unice.polytech.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
public enum CookieSize {
     L(4),XL(5), XXL(6);
     @Getter
    private final int multiplier;

}
