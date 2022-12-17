package fr.unice.polytech.cucumber.client;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.UnregisteredClient;
import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.IngredientType;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.StoreFactory;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.interfaces.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


public class PayOrderStepDefs  {
    Client client;
    Store store;
    UUID orderID;
    @Autowired
    StoreRegistration storeManager;
    @Autowired
    IngredientFinder ingredientFinder;
    @Autowired
    CookieChoice orderManager;
    @Autowired
    OrderFinder orderFinder;
    @Autowired
    OrderUpdater orderUpdater;
    @Autowired
    InventoryFiller inventoryFiller;
    @Autowired
    AgendaProcessor agendaProcessor;
    @Autowired
    IngredientRegistration ingredientRegistration;
    @Autowired
    CookieRegistration cookieRegistration;

    public final List<Cook> cooks = new ArrayList<>();

    public final Inventory inventory = new Inventory(new ArrayList<>());
    final List<Cookie> cookieList = new ArrayList<>();


    @Given("an empty cod and a unregistered client with phone number {string}")
    public void givenACODAndUnregisteredClient(String phoneNumber) {
        Client realClient = new UnregisteredClient(phoneNumber);
        client = spy(realClient);
        cooks.add(new Cook());
        store = StoreFactory.createStore(cooks, "address",
                LocalTime.parse("08:00"), LocalTime.parse("20:00"),
                inventory, 7.0, new ArrayList<>());
        storeManager.addStore(store);

    }

    @And("the client's cart contains {int} cookies of type {string}")
    public void andTheClientSCartContainsCookiesOfType(int quantity, String cookieName) throws CookieException, OrderException, BadQuantityException, CatalogException, IngredientTypeException {
        List<Topping> toppings = new ArrayList<>();

        Dough dough = new Dough(UUID.randomUUID(),"Nice", 1);
        Flavour flavour = new Flavour(UUID.randomUUID(),"Chocolate",2);
        Topping topping = new Topping(UUID.randomUUID(),"Something", 1);
        toppings.add(topping);
        Cookie cookie = new SimpleCookieBuilder()
                .setName(cookieName)
                .setPrice(1.)
                        .setCookingTime(30)
                        .setCooking(Cooking.CHEWY)
                        .setMix(Mix.TOPPED)
                        .setDough(dough)
                        .setFlavour(flavour)
                        .setToppings(toppings)
                        .build();
        cookieList.add(cookie);
        ingredientRegistration.addIngredient("Nice", 1., IngredientType.DOUGH);
        ingredientRegistration.addIngredient("Chocolate", 1., IngredientType.FLAVOUR);
        ingredientRegistration.addIngredient("Something", 1., IngredientType.TOPPING);
        cookieRegistration.suggestRecipe(cookie);
        cookieRegistration.acceptRecipe(cookie, 8.);
        inventoryFiller.addIngredients(ingredientFinder.getIngredientByName("Nice"), 10, store);
        inventoryFiller.addIngredients(ingredientFinder.getIngredientByName("Chocolate"), 10, store);
        inventoryFiller.addIngredients(ingredientFinder.getIngredientByName("Something"), 11, store);
        store = StoreFactory.createStore(cooks, "address",
                LocalTime.parse("08:00"), LocalTime.parse("20:00"),
                inventory, 10.0, new ArrayList<>());
        orderManager.chooseCookie(client, store, cookie, quantity);
    }

    @When("I confirm my cart and pay my order")
    public void iConfirmMyCartAndPayMyOrder() throws BadQuantityException, CookException, IngredientTypeException {
        client.getCart().setPickupTime(LocalTime.parse("10:00").atDate(LocalDate.now(agendaProcessor.getClock())));
        orderID = orderUpdater.finalizeOrder(client, store); //Same as pay order
    }

    @Then("I should be notified that my order is paid")
    public void iShouldBeNotifiedThatMyOrderIsPaid() {
        verify(client).getNotified(any(Order.class), eq("Your order is paid"));
    }

    @Given("the cod already has some orders")
    public void theCodAlreadyHasSomeOrders() {
        Order order = mock(Order.class);
        when(order.getId()).thenReturn(UUID.randomUUID());
        Order order2 = mock(Order.class);
        when(order2.getId()).thenReturn(UUID.randomUUID());
        this.orderUpdater.addOrder(order);
        this.orderUpdater.addOrder(order2);
    }

    @Then("I should pay the right price and be notified")
    public void iShouldPayTheRightPriceAndBeNotified() throws OrderException {
        verify(client).getNotified(any(Order.class), eq("Your order is paid"));
        Order o = orderFinder.getOrder(orderID);
        for (Item i: o.getItems()) {
            System.out.println(i.getCookie().getPrice()+" "+i.getQuantity());
        }
        assertEquals(8.0, orderFinder.getOrder(orderID).getPrice());
    }
}
