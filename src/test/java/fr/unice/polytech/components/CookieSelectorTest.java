package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.client.UnregisteredClient;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.recipe.enums.CookieSize;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.enums.Theme;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.entities.store.Occasion;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.exception.ServiceNotAvailable;
import fr.unice.polytech.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CookieSelectorTest {
    @Autowired
    private CookieChoice cookieChoice;
    @Autowired
    private CookieFinder cookieFinder;
    @Autowired
    private CookieRegistration cookieRegistration;
    @MockBean
    StoreProcessor storeProcessor;
    @MockBean
    ClientHandler clientHandler;

    Store store;
    Cookie cookie;
    RegisteredClient client;

    @BeforeEach
    public void setUp() throws CookieException {
        store = mock(Store.class);
        when(store.getThemeList()).thenReturn(List.of(Theme.ANIMAL,Theme.MOVIE));
        when(store.getOccasionList()).thenReturn(List.of(Occasion.BIRTHDAY,Occasion.WEDDING));
        client = new RegisteredClient("*****","0606060606","example@mail.com");
        cookie = new SimpleCookieBuilder()
                .setName("chocolala")
                .setPrice(1.)
                .setCookingTime(15)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(new Dough(UUID.randomUUID(), "chocolate", 1))
                .setFlavour(new Flavour(UUID.randomUUID(), "chocolate", 1))
                .setToppings(List.of(new Topping(UUID.randomUUID(), "chocolat", 1.)))
                .build();
        cookieRegistration.suggestRecipe(cookie);
        cookieRegistration.acceptRecipe(cookie,1.);
        when(storeProcessor.getMaxCookieAmount(any(Cookie.class),any(Store.class))).thenReturn(10);



    }

    @Test
    public void personalizeCookieFromBaseRecipeTest() throws CookieException, ServiceNotAvailable, OrderException {

        assertThrows(ServiceNotAvailable.class, () -> cookieChoice. personalizeCookieFromBaseRecipe(client, store, cookie, 1,
                                                                                                    CookieSize.L,Occasion.BIRTH,Theme.ANIMAL,
                                                                                                    new ArrayList<>(), new ArrayList<>()));
        assertThrows(ServiceNotAvailable.class, () -> cookieChoice. personalizeCookieFromBaseRecipe(client, store, cookie, 1,
                                                                                                    CookieSize.L,Occasion.BIRTHDAY,Theme.GAME,
                                                                                                    new ArrayList<>(), new ArrayList<>()));
        assertThrows(CookieException.class, () -> cookieChoice. personalizeCookieFromBaseRecipe(client, store, cookie, 11,
                                                                                                    CookieSize.L,Occasion.BIRTHDAY,Theme.ANIMAL,
                                                                                                    new ArrayList<>(), new ArrayList<>()));
        cookieChoice. personalizeCookieFromBaseRecipe(client, store, cookie, 1,
                                                      CookieSize.L,Occasion.BIRTHDAY,Theme.ANIMAL,
                                                      new ArrayList<>(), new ArrayList<>());
        assertEquals(1,client.getCart().getItems().size());
    }

    @Test
    public void personalizeCookieFromScratch() throws CookieException, ServiceNotAvailable, OrderException {
        assertThrows(ServiceNotAvailable.class, () -> cookieChoice.personalizeCookieFromScratch(client, store,"chocolate",Cooking.CHEWY,Mix.MIXED,
                                                                                                new Dough(UUID.randomUUID(),"chocoalte",1.),new Flavour(UUID.randomUUID(),"chocoalte",1.),new ArrayList<>(), 1,
                                                                                                CookieSize.L,Occasion.BIRTH,Theme.ANIMAL,
                                                                                               new ArrayList<>()));
        assertThrows(ServiceNotAvailable.class, () -> cookieChoice.personalizeCookieFromScratch(client, store,"chocolate",Cooking.CHEWY,Mix.MIXED,
                                                                                                new Dough(UUID.randomUUID(),"chocoalte",1.),new Flavour(UUID.randomUUID(),"chocoalte",1.),new ArrayList<>(), 1,
                                                                                                CookieSize.L,Occasion.BIRTHDAY,Theme.GAME,
                                                                                                new ArrayList<>()));
        assertThrows(CookieException.class, () -> cookieChoice.personalizeCookieFromScratch(client, store,"chocolate",Cooking.CHEWY,Mix.MIXED,
                                                                                                new Dough(UUID.randomUUID(),"chocoalte",1.),new Flavour(UUID.randomUUID(),"chocoalte",1.),new ArrayList<>(), 11,
                                                                                                CookieSize.L,Occasion.BIRTHDAY,Theme.ANIMAL,
                                                                                                new ArrayList<>()));
        cookieChoice.personalizeCookieFromScratch(client, store,"chocolate",Cooking.CHEWY,Mix.MIXED,
                                                  new Dough(UUID.randomUUID(),"chocoalte",1.),new Flavour(UUID.randomUUID(),"chocoalte",1.),new ArrayList<>(), 1,
                                                  CookieSize.L,Occasion.BIRTHDAY,Theme.ANIMAL,
                                                  new ArrayList<>());
        assertEquals(1,client.getCart().getItems().size());

    }

    @Test
    public void chooseCookie() throws CookieException, OrderException {
        when(clientHandler.isBanned(client)).thenReturn(false,true);
        cookieChoice.chooseCookie(client,store,cookie,1);
        assertEquals(1,client.getCart().getItems().size());
        assertThrows(OrderException.class, () -> cookieChoice.chooseCookie(client,store,cookie,1));
    }
}
