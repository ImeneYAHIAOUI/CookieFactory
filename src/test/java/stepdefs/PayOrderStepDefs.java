package stepdefs;

import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.cod.COD;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.recipe.*;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.Store;
import fr.unice.polytech.store.StoreFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PayOrderStepDefs {
    COD cod;
    Client client;
    Store store;
    String orderID;

    public final List<Cook> cooks = new ArrayList<>();
    public final List<Cookie> recipes = new ArrayList<>();

    public final Inventory inventory = new Inventory(new ArrayList<>());
    final List<Cookie> cookieList = new ArrayList<>();


    @Given("an empty cod and a unregistered client with phone number {string}")
    public void givenACODAndUnregisteredClient(String phoneNumber) throws InvalidPhoneNumberException {
        cod = COD.getInstance();
        Client realClient = new UnregisteredClient(phoneNumber);
        client = spy(realClient);
        cooks.add(new Cook(0));
        store = StoreFactory.createStore(cooks, recipes, "address",
                LocalTime.parse("08:00"), LocalTime.parse("20:00"),
                1, inventory, 7.0, new ArrayList<>());
        cod.addStore(store);

    }

    @And("the client's cart contains {int} cookies of type {string}")
    public void andTheClientSCartContainsCookiesOfType(int quantity, String cookieName) throws CookieException, OrderException, AlreadyExistException, BadQuantityException {
        List<Topping> toppings = new ArrayList<>();
        Dough dough = new Dough("Nice", 1);
        Flavour flavour = new Flavour("Chocolate",2);
        Topping topping = new Topping("Something", 1);
        toppings.add(topping);
        Cookie cookie = new Cookie(cookieName, 1., 30, Cooking.CHEWY, Mix.TOPPED, dough, flavour, toppings);
        cookieList.add(cookie);
        store.addCookies(cookieList);
        store.addIngredients(dough, 10);
        store.addIngredients(flavour, 10);
        store.addIngredients(topping, 11);
        cod.chooseCookie(client, store, cookie, quantity);
    }

    @When("I confirm my cart and pay my order")
    public void iConfirmMyCartAndPayMyOrder() throws BadQuantityException, CookException, StoreException, PaymentException {
        client.getCart().setPickupTime(LocalTime.parse("10:00"));
        orderID = cod.payOrder(client, store);
    }

    @Then("I should be notified that my order is paid")
    public void iShouldBeNotifiedThatMyOrderIsPaid() {
        verify(client).getNotified(any(Order.class), eq("Your order is paid"));
    }

    @Given("the cod already has some orders")
    public void theCodAlreadyHasSomeOrders() {
        Order order = mock(Order.class);
        when(order.getId()).thenReturn("1");
        Order order2 = mock(Order.class);
        when(order2.getId()).thenReturn("2");
        this.cod.addOrder(order);
        this.cod.addOrder(order2);
    }

    @Then("I should pay the right price and be notified")
    public void iShouldPayTheRightPriceAndBeNotified() throws OrderException {
        verify(client).getNotified(any(Order.class), eq("Your order is paid"));
        assertEquals(8., cod.getOrder(orderID).getPrice());
    }
}
