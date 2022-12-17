package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.recipe.cookies.Cookie;

import java.util.List;

public interface CookieFinder {

    List<Cookie> getRecipes();
    List<Cookie> getSuggestedRecipes();

}
