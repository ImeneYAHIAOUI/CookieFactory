package stepdefs;

import fr.unice.polytech.client.RegisteredClient;
import fr.unice.polytech.cod.COD;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.recipe.Dough;
import fr.unice.polytech.recipe.Flavour;
import fr.unice.polytech.recipe.Topping;
import fr.unice.polytech.services.PaymentService;
import fr.unice.polytech.store.Store;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class GetDiscountStepDefs {

    COD cod;
    RegisteredClient client;
    PaymentService paymentService;
    Store store;

    List<Order> pastOrders;

    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        cod = COD.getInstance();
        cod.initializationCod();
        paymentService = mock(PaymentService.class);
        Field paymentServiceInstance = PaymentService.class.getDeclaredField("INSTANCE");
        paymentServiceInstance.setAccessible(true);
        paymentServiceInstance.set(paymentServiceInstance, paymentService);
        store = cod.getStores().get(0);
        pastOrders = new ArrayList<>();
        store.getInventory().addIngredient(new Dough("chocolate", 1), 150);
        store.getInventory().addIngredient(new Flavour("chocolate", 1), 150);
        store.getInventory().addIngredient(new Topping("chocolate chips", 1), 150);
    }

    @Given("a registered client")
    public void givenARegisteredClient() throws InvalidPhoneNumberException, NoSuchFieldException, IllegalAccessException {
        setUp();
        RegisteredClient realClient = new RegisteredClient("id", "password", "0123456789");
        client = spy(realClient);
    }

    @When("the client makes an order of {int} cookies")
    public void whenTheClientMakesAnOrderOfNCookies(int nbCookies) throws PaymentException, CookException, BadQuantityException, InvalidPickupTimeException {
        client.getCart().setTax(0.1);
        client.getCart().addItem(new Item(nbCookies, cod.getRecipes().get(0)));
        cod.choosePickupTime(client.getCart(), store, LocalTime.parse("10:00"));
        cod.finalizeOrder(client, store);
    }

    @And("the client makes a second order of {int} cookies")
    public void andTheClientMakesASecondOrderOfNCookies(int nbCookies) throws PaymentException, CookException, BadQuantityException, InvalidPickupTimeException {
        client.getCart().addItem(new Item(nbCookies, cod.getRecipes().get(0)));
        cod.choosePickupTime(client.getCart(), store, LocalTime.parse("10:00"));
        cod.finalizeOrder(client, store);
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
