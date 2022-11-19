package fr.unice.polytech.recipe;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


public class CookieTest {
    public Cookie cookie;
    @BeforeEach
    public void setUp() {
        List<Dough> doughs = List.of(new Dough("chocolate", 1), new Dough("strawberry", 1));
        List<Flavour> flavours = List.of(new Flavour("chocolate", 1), new Flavour("strawberry", 1));
        List<Topping> toppings = List.of(new Topping("chocolat chips", 1.), new Topping("m&ms", 1.), new Topping("strawberry flakes", 1.));
        cookie = new Cookie("chocolala", 1.,
                15, Cooking.CHEWY, Mix.MIXED, doughs.get(0),
                flavours.get(0), List.of(toppings.get(0)));

    }
    @Test
    void setCookieTest(){
        double price=cookie.getPrice();
        assert(cookie.getSize()==null);
        assert(cookie.getPrice().equals(price));
        cookie.setSize(CookieSize.L);
        assert(cookie.getSize().equals(CookieSize.L));
        assert(cookie.getPrice().equals(price*4));
        cookie.setSize(CookieSize.XL);
        assert(cookie.getSize().equals(CookieSize.XL));
        assert(cookie.getPrice().equals(price* 5));
        cookie.setSize(CookieSize.XXL);
        assert(cookie.getPrice().equals(price*6));
        assert(cookie.getSize().equals(CookieSize.XXL));
    }
    @Test
    void getBasicPriceTest(){
        double price=cookie.getPrice();
        assert(cookie.getSize()==null);
        assert(cookie.getBasicPrice()==price);
        cookie.setSize(CookieSize.L);
        assert(cookie.getSize().equals(CookieSize.L));
        assert(cookie.getBasicPrice()==price);
        cookie.setSize(CookieSize.XL);
        assert(cookie.getSize().equals(CookieSize.XL));
        assert(cookie.getBasicPrice()==price);
        cookie.setSize(CookieSize.XXL);
        assert(cookie.getBasicPrice()==price);
        assert(cookie.getSize().equals(CookieSize.XXL));
    }

}
