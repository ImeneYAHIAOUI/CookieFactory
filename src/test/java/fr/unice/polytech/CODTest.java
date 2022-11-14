package fr.unice.polytech;

import fr.unice.polytech.client.Cart;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.RegisteredClient;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.recipe.*;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CODTest {

    COD cod;
    Order order;

    Client client;

    RegisteredClient registeredClient;

    Cook cook;

    Store store;

    Store mockstore;

    Cookie cookie1;

    Inventory inventory;

    @BeforeEach
    public void setUp() throws InvalidPhoneNumberException {
        cod = new COD();
        client = new UnregisteredClient("0606060606");

        try {

            cod.register("15", "^mldp", "0707060106");
        } catch (RegistrationException ignored) {

        }
        cook = new Cook(1);

        mockstore = mock(Store.class);

        registeredClient = mock(RegisteredClient.class);

        cookie1 = new Cookie("chocolala",1.,15,Cooking.CHEWY,Mix.MIXED, new Dough("chocolate",1),new Flavour("chocolate",1),List.of(new Topping("chocolat",1.)));

        inventory = mock(Inventory.class);
        store = new Store(
                List.of(cook),
                List.of(new Cookie("chocolala", 1., 15, Cooking.CHEWY, Mix.MIXED, new Dough("chocolate", 1), new Flavour("chocolate", 1), List.of(new Topping("chocolate chips", 1)))),
                "30 Rte des Colles, 06410 Biot",
                LocalTime.parse("08:00"),
                LocalTime.parse("20:00"),
                1,
                new Inventory(new ArrayList<>()),4.2,new ArrayList<>()
        );
        client.getCart().setPickupTime(LocalTime.parse("10:00"));
        order = new Order("1", client, cook, store);

    }

    @Test
    public void testInitialstatus() {
        assertEquals(order.getStatus(), OrderStatus.NOT_STARTED);
    }

    @Test
    public void testSetStatus1() {
        assertDoesNotThrow(() ->cod.setStatus(order, OrderStatus.PAYED));
        assertEquals(order.getStatus(), OrderStatus.PAYED);
        assertThrows(OrderException.class,() ->cod.setStatus(order, OrderStatus.PAYED));
        assertDoesNotThrow(() ->cod.setStatus(order, OrderStatus.IN_PROGRESS));
        assertEquals(order.getStatus(), OrderStatus.IN_PROGRESS);
        assertThrows(OrderException.class,() ->cod.setStatus(order, OrderStatus.CANCELLED));
    }

    @Test
    public void testSetStatus2() {
        assertDoesNotThrow(() ->cod.setStatus(order, OrderStatus.CANCELLED));
        assertEquals(order.getStatus(), OrderStatus.CANCELLED);
        assertThrows(OrderException.class,() ->cod.setStatus(order, OrderStatus.PAYED));
    }

    @Test
    public void testChooseCookieBannedClient()
    {
        when(registeredClient.isBanned()).thenReturn(false,true);
        when(registeredClient.getCart()).thenReturn(new Cart(),new Cart());
        when(mockstore.getRecipes()).thenReturn(List.of(cookie1),List.of(cookie1));
        when(mockstore.getMaxCookieAmount(cookie1,1)).thenReturn(1,1);
        assertDoesNotThrow(() ->cod.chooseCookie(registeredClient,mockstore,cookie1,1));
        assertThrows(OrderException.class,() ->cod.chooseCookie(registeredClient,mockstore,cookie1,1));
    }

    @Test
    public void testChooseCookieNotEnoughCookie()
    {
        when(registeredClient.isBanned()).thenReturn(false,false);
        when(registeredClient.getCart()).thenReturn(new Cart(),new Cart());
        when(mockstore.getRecipes()).thenReturn(List.of(cookie1),List.of(cookie1));
        when(mockstore.getMaxCookieAmount(cookie1,1)).thenReturn(1,0);
        assertDoesNotThrow(() ->cod.chooseCookie(registeredClient,mockstore,cookie1,1));
        assertThrows(CookieException.class,() ->cod.chooseCookie(registeredClient,mockstore,cookie1,1));
    }

    @Test
    public void testChooseCookieNonAvailableCookie()
    {
        when(registeredClient.isBanned()).thenReturn(false,false);
        when(registeredClient.getCart()).thenReturn(new Cart(),new Cart());
        when(mockstore.getRecipes()).thenReturn(List.of(cookie1),List.of());
        when(mockstore.getMaxCookieAmount(cookie1,1)).thenReturn(1,0);
        assertDoesNotThrow(() ->cod.chooseCookie(registeredClient,mockstore,cookie1,1));
        assertThrows(CookieException.class,() ->cod.chooseCookie(registeredClient,mockstore,cookie1,1));
    }

    @Test
    public void testGetClientPastOrders()
    {
        Order order2 = new Order("1",client,cook,store);
        Order order3 = new Order("1",client,cook,store);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);
        orders.add(order3);
        when(registeredClient.getPastOrders()).thenReturn(orders);
        assertEquals(cod.getClientPastOrders(registeredClient),orders);

    }




    @Test
    public void testLogIn() throws InvalidInputException {
        cod.logIn("15", "^mldp");
        assertEquals("15", cod.getConnectedClients().get(0).getId());
        assertEquals("^mldp", cod.getConnectedClients().get(0).getPassword());
        assertThrows(InvalidInputException.class, () -> cod.logIn("15", "^mldp"),
                "Your are already connected ");
        assertThrows(InvalidInputException.class, () -> cod.logIn("10", "^mldp"),
                "ID not found. Please log in with another ID");
        assertThrows(InvalidInputException.class, () -> cod.logIn("15", "^mdp"),
                "The password you entered is not valid. ");
    }

    @Test
    public void searchOrderThatDoesntExist() {
        assertThrows(OrderException.class, () -> cod.getOrder("10"));
    }
    @Test
    public void searchOrderThatExist() throws OrderException {
        cod.addOrder(order);
        assertEquals(order, cod.getOrder("1"));
    }
    @Test
    public void addCookToStore() throws StoreException {
        cod.addStore(store);
        cod.addCook(store.getId());
        assertEquals(2, store.getCooks().size());
    }

    @Test
    public void getStoreThatDoesntExist() {
        assertThrows(StoreException.class, () -> cod.getStore(10));
    }

    @Test
    public void acceptNewRecipe() {
        cod.addStore(store);
        List<Topping> toppingList = new ArrayList<>();
        Cookie cookie = new Cookie("IncredibleCookie",10.0,10,Cooking.CHEWY,Mix.MIXED,new Dough("Yum",2),new Flavour("Yummy",6),toppingList);
        cod.suggestRecipe(cookie);
        cod.acceptRecipe(cookie,10.1);
        assertEquals(1,store.getRecipes().size());
    }

    @Test
    public void addIngredientToCatalog() throws CatalogException {
        cod.addIngredientCatalog("Sugar", 10.0,IngredientType.FLAVOUR);
        assertEquals(IngredientType.FLAVOUR, cod.getIngredientCatalog("Sugar").getIngredientType());
    }
    @Test
    public void addIngredientAlreadyInCatalog() throws CatalogException {
        cod.addIngredientCatalog("Orange", 10.0,IngredientType.FLAVOUR);
        assertThrows(CatalogException.class, () -> cod.addIngredientCatalog("Orange", 10.0,IngredientType.FLAVOUR));
    }


}
