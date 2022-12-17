package fr.unice.polytech.entities.recipe.cookies;

import fr.unice.polytech.entities.recipe.enums.Theme;

/**
 * Abstract builder for {@link PartyCookie} objects.
 */
public abstract class PartyCookieBuilder extends CookieBuilder {
    protected Theme theme;

    public PartyCookieBuilder setTheme(Theme theme) {
        this.theme = theme;
        return this;
    }
}