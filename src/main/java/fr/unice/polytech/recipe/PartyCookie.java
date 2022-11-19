package fr.unice.polytech.recipe;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PartyCookie extends Cookie{
    public PartyCookie(Cookie cookie,CookieSize size,Theme theme) {
        super(cookie.getName(), cookie.getPrice(), cookie.getCookingTime(),cookie.getCooking(), cookie.getMix(), cookie.getDough()
                , cookie.getFlavour(), cookie.getToppings());
        setSize(size);
        setTheme(theme);
    }


}
