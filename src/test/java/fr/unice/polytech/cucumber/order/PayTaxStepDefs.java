package fr.unice.polytech.cucumber.order;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.RegisteredClient;
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
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;


public class PayTaxStepDefs {

    Client client;
    Order order;
    Store store;

    @Autowired
    OrderFinder orderFinder;
    @Autowired
    IngredientRegistration ingredientRegistration;
    @Autowired
    IngredientFinder ingredientFinder;
    @Autowired
    CookieRegistration cookieRegistration;
    @Autowired
    CookieFinder cookieFinder;
    @Autowired
    AgendaProcessor agendaProcessor;

    @Autowired
    StoreRegistration storeProcessor;
    @Autowired
    CartHandler cartHandler;
    Cookie cookie;
    @Autowired
    OrderUpdater orderUpdater;

    public void setUp() throws CookieException, CatalogException, IngredientTypeException {
        ingredientRegistration.addIngredient("chocolateD", 1., IngredientType.DOUGH);
        ingredientRegistration.addIngredient("chocolateF", 1., IngredientType.FLAVOUR);
        ingredientRegistration.addIngredient("chocolateT", 1., IngredientType.TOPPING);
        cookie= new SimpleCookieBuilder()
                .setName("chocolateD")
                .setPrice(0.0)
                .setCookingTime(15)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(new Dough(UUID.randomUUID(), "chocolate", 1))
                .setFlavour(new Flavour(UUID.randomUUID(), "chocolate", 1))
                .setToppings(List.of(new Topping(UUID.randomUUID(),"chocolat", 1.)))
                .build();
    }

    @Given("a client not registered in base")
    public void givenNRClient() throws CookieException, CatalogException, IngredientTypeException {
        setUp();
        client = new UnregisteredClient("0667995895");

    }
    @Given("a registered client in base")
    public void givenRClient() throws CookieException, CatalogException, IngredientTypeException,IngredientTypeException {
        setUp();
        client = new RegisteredClient("d","0123456789","s");
    }
   @And("a store with tax {double}")
    public void storeGiven(Double tax){
       store = storeProcessor.addStore(2, new Inventory(new ArrayList<>()), "", "08:00", "18:00", 10., new ArrayList<>());
        store.setTax(tax);
       client.getCart().setTax(tax);
   }
   @When("the client choose a cookie with price {double}")
    public void chooseCookie(Double price){
        cookie.setPrice(price);
       client.getCart().setPickupTime(LocalTime.parse("10:00").atDate(LocalDate.now(agendaProcessor.getClock())));

       cartHandler.addItem(new Item(1,cookie),client.getCart());

   }
    @Then("the client pay {double}")
    public void payORDER(double price)throws BadQuantityException, CookException, IngredientTypeException,OrderException{
        UUID id=orderUpdater.finalizeOrder(client,store);
        System.out.println(orderFinder.getOrder(id).getPrice());
        assertTrue(orderFinder.getOrder(id).getPrice()==price);
    }
}
