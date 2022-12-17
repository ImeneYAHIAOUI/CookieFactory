package fr.unice.polytech.cucumber.order;

import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.IngredientType;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.interfaces.*;
import fr.unice.polytech.services.PaymentService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class GetDiscountStepDefs{

    RegisteredClient client;
    PaymentService paymentService;
    Store store;

    @Autowired
    CartHandler cartManager;
    @Autowired
    private InventoryFiller inventoryManager;
    @Autowired
    InventoryUpdater inventoryUpdater;
    @Autowired
    StoreRegistration storeProcessor;
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
    OrderUpdater orderUpdater;

    public void setUp() throws NoSuchFieldException, IllegalAccessException, CookieException, CatalogException, IngredientTypeException, BadQuantityException {
        LocalDateTime date = LocalDateTime.of(2022, 12,17, 8, 0);
        ZoneId zoneId = ZoneId.systemDefault();
        agendaProcessor.setClock(Clock.fixed(date.atZone(zoneId).toInstant(), zoneId));
        paymentService = mock(PaymentService.class);
        Field paymentServiceInstance = PaymentService.class.getDeclaredField("INSTANCE");
        paymentServiceInstance.setAccessible(true);
        paymentServiceInstance.set(paymentServiceInstance, paymentService);
        store = storeProcessor.addStore(2, new Inventory(new ArrayList<>()), "", "00:00", "23:00", 10., new ArrayList<>());
        ingredientRegistration.addIngredient("chocolateD", 1., IngredientType.DOUGH);
        ingredientRegistration.addIngredient("chocolateF", 1., IngredientType.FLAVOUR);
        ingredientRegistration.addIngredient("chocolateT", 1., IngredientType.TOPPING);
        cookieRegistration.suggestRecipe("", 0, 0, Cooking.CHEWY, Mix.TOPPED,
                ingredientFinder.getIngredientByName("chocolateD"), ingredientFinder.getIngredientByName("chocolateF"), List.of((Topping) ingredientFinder.getIngredientByName("chocolateT")));
        cookieRegistration.acceptRecipe(cookieFinder.getSuggestedRecipes().get(0), 10.);
        inventoryManager.addIngredients(ingredientFinder.getIngredientByName("chocolateD"), 100, store);
        inventoryManager.addIngredients(ingredientFinder.getIngredientByName("chocolateF"), 100, store);
        inventoryManager.addIngredients(ingredientFinder.getIngredientByName("chocolateT"), 100, store);
        doNothing().when(inventoryUpdater).decreaseIngredientQuantity(any(Ingredient.class), anyInt(), any(Inventory.class));

    }

    @Given("a registered client")
    public void givenARegisteredClient() throws NoSuchFieldException, IllegalAccessException, CookieException, CatalogException, IngredientTypeException, BadQuantityException {
       setUp();
        RegisteredClient realClient = new RegisteredClient("id", "password", "0123456789");
        client = spy(realClient);
    }

    @When("the client makes an order of {int} cookies")
    public void whenTheClientMakesAnOrderOfNCookies(int nbCookies)
            throws CookException, BadQuantityException, InvalidPickupTimeException, IngredientTypeException
    {
      client.getCart().setTax(0.1);

        cartManager.addItem(new Item(nbCookies, cookieFinder.getRecipes().get(0)), client.getCart());
        agendaProcessor.choosePickupTime(client.getCart(), store, LocalTime.parse("10:00"));
        orderUpdater.finalizeOrder(client, store);
    }

    @And("the client makes a second order of {int} cookies")
    public void andTheClientMakesASecondOrderOfNCookies(int nbCookies)
            throws CookException, BadQuantityException, InvalidPickupTimeException, IngredientTypeException
    {
        cartManager.addItem(new Item(nbCookies, cookieFinder.getRecipes().get(0)), client.getCart());
        agendaProcessor.choosePickupTime(client.getCart(), store, LocalTime.parse("10:00"));
        orderUpdater.finalizeOrder(client, store);
    }

    @Then("the client has a discount of 10%")
    public void thenTheClientHasADiscountOf10() {
        assertTrue(client.isEligibleForDiscount());
    }

    @Then("the client does not have a discount")
    public void theClientDoesNotHaveADiscount() {
        assertFalse(client.isEligibleForDiscount());
    }

}
