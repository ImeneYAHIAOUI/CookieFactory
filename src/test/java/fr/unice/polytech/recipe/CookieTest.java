package fr.unice.polytech.recipe;


import fr.unice.polytech.entities.recipe.*;
import fr.unice.polytech.exception.CookieException;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;


public class CookieTest {
    public PartyCookie cookie;
    @BeforeEach
    public void setUp() throws CookieException {
        List<Dough> doughs = List.of(new Dough("chocolate", 1), new Dough("strawberry", 1));
        List<Flavour> flavours = List.of(new Flavour("chocolate", 1), new Flavour("strawberry", 1));
        List<Topping> toppings = List.of(new Topping("chocolat chips", 1.), new Topping("m&ms", 1.), new Topping("strawberry flakes", 1.));
        cookie = CookieFactory.createPartyCookieFromScratch(null, null,
                "chocolala", Cooking.CHEWY, Mix.MIXED, doughs.get(0),
                flavours.get(0), List.of(toppings.get(0)),new ArrayList<>());

    }


}
