package fr.unice.polytech.store;


import fr.unice.polytech.entities.client.Cart;
import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.cod.COD;
import fr.unice.polytech.entities.recipe.*;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.StoreFactory;
import fr.unice.polytech.exception.AlreadyExistException;
import fr.unice.polytech.exception.BadQuantityException;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.entities.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StoreTest {

    Store store;
    Order order;

    Client client;

    Cookie cookie1;
    Cookie cookie2;
    Cookie cookie3;

    Inventory inventory;

    List<Dough> doughs;
    List<Flavour> flavours;
    List<Topping> toppings;

    Cook cook;
    @BeforeEach
    public void setUp() throws CookieException {
        client = mock(Client.class);
        cook = mock(Cook.class);
        when(client.getCart()).thenReturn(new Cart());
        client.getCart().setPickupTime(LocalTime.of(12, 0).atDate(LocalDate.now(COD.getCLOCK())));
        order = new Order("1", client, cook, store);
        doughs = List.of(new Dough("chocolate", 1), new Dough("strawberry", 1));
        flavours = List.of(new Flavour("chocolate", 1), new Flavour("strawberry", 1));
        toppings = List.of(new Topping("chocolat chips", 1.), new Topping("m&ms", 1.), new Topping("strawberry flakes", 1.));
        inventory = mock(Inventory.class);
        cookie1 = CookieFactory.createSimpleCookie("chocolala", 1., 15, Cooking.CHEWY, Mix.MIXED, doughs.get(0), flavours.get(0), List.of(toppings.get(0)));
        cookie2 = CookieFactory.createSimpleCookie("m&ms", 1., 15, Cooking.CHEWY, Mix.MIXED, doughs.get(0), flavours.get(0), List.of(toppings.get(1)));
        cookie3 = CookieFactory.createSimpleCookie("strawbarry", 1., 15, Cooking.CHEWY, Mix.MIXED, doughs.get(1), flavours.get(1), List.of(toppings.get(2)));
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(cookie1);
        cookies.add(cookie2);
        cookies.add(cookie3);
        store = StoreFactory.createStore(List.of(cook), cookies, "adresse", LocalTime.parse("10:00")
                , LocalTime.parse("18:00"),
                1, inventory, 7.0, new ArrayList<>());
    }

    @Test
    public void testgetMaxAmountOfCookies() {
        when(inventory.get(doughs.get(0))).thenReturn(4);
        when(inventory.get(flavours.get(0))).thenReturn(4);
        when(inventory.get(toppings.get(0))).thenReturn(4);
        when(inventory.get(toppings.get(1))).thenReturn(2);
        when(inventory.get(doughs.get(1))).thenReturn(3);
        when(inventory.get(flavours.get(1))).thenReturn(1);
        when(inventory.get(toppings.get(2))).thenReturn(5);

        assertEquals(store.getMaxCookieAmount(cookie1,1), 4);
        assertEquals(store.getMaxCookieAmount(cookie2,1), 2);
        assertEquals(store.getMaxCookieAmount(cookie3,1), 1);
    }

    @Test
    public void testRemoveCookies()
    {
        assertTrue(store.getRecipes().containsAll(List.of(cookie1, cookie2, cookie3)));
        store.removeCookies(doughs.get(0));
        assertFalse(store.getRecipes().contains(cookie1));
        assertFalse(store.getRecipes().contains(cookie2));
        assertTrue(store.getRecipes().contains(cookie3));
        store.removeCookies(doughs.get(1));
        assertFalse(store.getRecipes().contains(cookie3));
    }

    @Test
    public void testRemoveCookies2()
    {
        assertTrue(store.getRecipes().containsAll(List.of(cookie1, cookie2, cookie3)));
        store.removeCookies(toppings.get(0));
        assertFalse(store.getRecipes().contains(cookie1));
        assertTrue(store.getRecipes().contains(cookie2));
        assertTrue(store.getRecipes().contains(cookie3));
        store.removeCookies(flavours.get(1));
        assertTrue(store.getRecipes().contains(cookie2));
        assertFalse(store.getRecipes().contains(cookie3));
        store.removeCookies(toppings.get(1));
        assertFalse(store.getRecipes().contains(cookie2));

    }

    @Test
    public void newStoreClosingBeforeOpening(){
        Store store1 = new Store(new ArrayList<>(), new ArrayList<>(), "30 Rte des Colles, 06410 Biot", LocalTime.parse("20:00"), LocalTime.parse("08:00"), 1, new Inventory(new ArrayList<>()), 4.2, new ArrayList<>());
        assertEquals(LocalTime.parse("08:00"),store1.getOpeningTime());
        assertEquals(LocalTime.parse("20:00"),store1.getClosingTime());
    }

    @Test
    public void addNegativeIngredients(){
        assertThrows(BadQuantityException.class, () -> store.addIngredients(new Dough("chocolate", 1),-4));
    }

    @Test
    public void cannotAddCookieToStoreBecauseDoesntHaveDough() throws CookieException {
        Dough dough = new Dough("whiteChocolate", 1);
        Cookie cookie = CookieFactory.createSimpleCookie("vanilla", 1., 15, Cooking.CHEWY, Mix.MIXED, dough, flavours.get(0), List.of(toppings.get(0)));
        assertFalse(store.canAddCookieToStore(cookie));
    }

    @Test
    public void cannotAddCookieToStoreBecauseDoesntHaveFlavour() throws CookieException {
        Flavour flavour = new Flavour("whiteChocolate", 1);
        Cookie cookie = CookieFactory.createSimpleCookie("vanilla", 1., 15, Cooking.CHEWY, Mix.MIXED, doughs.get(0), flavour, List.of(toppings.get(0)));
        assertFalse(store.canAddCookieToStore(cookie));
    }

    @Test
    public void cannotAddCookieToStoreBecauseZeroChosenFlavour() throws AlreadyExistException, BadQuantityException, CookieException {
        Flavour flavour = new Flavour("whiteChocolate", 1);
        store.addIngredients(flavour, 0);
        Cookie cookie = CookieFactory.createSimpleCookie("vanilla", 1., 15, Cooking.CHEWY, Mix.MIXED, doughs.get(0), flavour, List.of(toppings.get(0)));
        assertFalse(store.canAddCookieToStore(cookie));
    }




}
