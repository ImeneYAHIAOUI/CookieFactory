package fr.unice.polytech.client;

import fr.unice.polytech.COD;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.recipe.*;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.Store;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void givenACODAndUnregisteredClient(String phoneNumber) {
        cod = new COD();
        client = new UnregisteredClient(phoneNumber);
        cooks.add(new Cook(0));
        store = new Store(cooks, recipes, "address", LocalTime.parse("08:00"), LocalTime.parse("20:00"), 1, inventory);
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
        orderID = cod.payOrder(client, store);
    }

    @Then("I should see a confirm message and my orderID is 0")
    public void iShouldSeeAConfirmMessageAndOrderIdIs0() {
        assertEquals(orderID, "0");
    }

    @Given("the cod already has some orders")
    public void theCodAlreadyHasSomeOrders() {
        Client client2 = new RegisteredClient("id", "mdp", "0123456789");
        Cook cook = new Cook(1);
        Order order = new Order("0", client2, cook,store);
        Order order2 = new Order("1", client2, cook,store);
        this.cod.addOrder(order);
        this.cod.addOrder(order2);
    }
    @Then("I should see a confirm message and get my orderID")
    public void iShouldSeeAConfirmMessageAndGetMyOrderID() throws OrderException {
        assertEquals(Integer.toString(cod.getOrders().size()-1),orderID);
        assertEquals(1., cod.getOrder(orderID).getPrice());
    }
}
