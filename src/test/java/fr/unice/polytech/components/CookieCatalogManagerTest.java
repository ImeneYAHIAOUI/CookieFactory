package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.UnregisteredClient;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.StoreFactory;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.exception.RegistrationException;
import fr.unice.polytech.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@SpringBootTest
public class CookieCatalogManagerTest {
    Order order;

    Client client;
    Store store;
    Store mockstore;
    Cookie cookie1;


    Cook cook;

    Inventory inventory;

    @Autowired
    StoreRegistration storeManager;
    @Autowired
    ClientHandler clientManager;

    @Autowired
    CookieFinder cookieFinder;

    @Autowired
    CookieRegistration cookieRegistration;

    @Autowired
    AgendaProcessor agendaProcessor;

    @BeforeEach
    public void setUp() throws InvalidPhoneNumberException, CookieException {
        client = new UnregisteredClient("0606060606");

        try {

            clientManager.register("15", "^mldp", "0707060106");
        } catch (RegistrationException ignored) {

        }
        cook = new Cook();

        mockstore = mock(Store.class);
        cookie1 = new SimpleCookieBuilder()
                .setName("chocolala")
                .setPrice(1.)
                .setCookingTime(15)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(new Dough(UUID.randomUUID(),"chocolate", 1))
                .setFlavour(new Flavour(UUID.randomUUID(),"chocolate", 1))
                .setToppings(List.of(new Topping(UUID.randomUUID(),"chocolat", 1.)))
                .build();
        inventory = mock(Inventory.class);
        store = StoreFactory.createStore(
                List.of(cook),
                "30 Rte des Colles, 06410 Biot",
                LocalTime.parse("08:00"),
                LocalTime.parse("20:00"),
                new Inventory(new ArrayList<>()), 4.2, new ArrayList<>()
        );
        client.getCart().setPickupTime(LocalTime.parse("10:00").atDate(LocalDate.now(agendaProcessor.getClock())));
        order = new Order(UUID.randomUUID(), client, cook, store, Duration.ofMinutes(20));
    }

    @Test
    public void testInitialisations() {
        assertEquals(order.getState().getOrderStatus(), OrderStatus.NOT_STARTED);
    }



    @Test
    public void acceptNewRecipe() throws CookieException {
        storeManager.addStore(store);
        List<Topping> toppingList = new ArrayList<>();
        Cookie cookie = new SimpleCookieBuilder()
                .setName("IncredibleCookie")
                .setPrice(10.0)
                .setCookingTime(10)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(new Dough(UUID.randomUUID(),"Yum", 2))
                .setFlavour(new Flavour(UUID.randomUUID(),"Yummy", 6))
                .setToppings(toppingList)
                .build();
        cookieRegistration.suggestRecipe(cookie);
        assertTrue(cookieFinder.getSuggestedRecipes().contains(cookie));
        cookieRegistration.acceptRecipe(cookie, 10.1);
        assertFalse(cookieFinder.getSuggestedRecipes().contains(cookie));
        assertTrue(cookieFinder.getRecipes().contains(cookie));
    }

    @Test
    public void refuseNewRecipe() throws CookieException {
        storeManager.addStore(store);
        List<Topping> toppingList = new ArrayList<>();
        Cookie cookie = new SimpleCookieBuilder()
                .setName("IncredibleCookie")
                .setPrice(10.0)
                .setCookingTime(10)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(new Dough(UUID.randomUUID(),"Yum", 2))
                .setFlavour(new Flavour(UUID.randomUUID(),"Yummy", 6))
                .setToppings(toppingList)
                .build();
        cookieRegistration.suggestRecipe(cookie);
        assertTrue(cookieFinder.getSuggestedRecipes().contains(cookie));
        cookieRegistration.declineRecipe(cookie);
        assertFalse(cookieFinder.getSuggestedRecipes().contains(cookie));
        assertFalse(cookieFinder.getRecipes().contains(cookie));
    }
}

