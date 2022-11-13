package stepdefs;

import fr.unice.polytech.client.RegisteredClient;
import fr.unice.polytech.exception.CookException;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.exception.PickupTimeNotSetException;
import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.recipe.Cookie;
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

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TimeSlotAttributionStepDefs {
    Cook cook;
    Cookie cookie;
    Store store;
    RegisteredClient client;
    Order order;

    @Given("a cook with an empty timetable")
    public void givenEmptyCook() {
        cook = new Cook(0);
    }

    @And("a random recipe with cooking time of {int}")
    public void andRandomRecipeWithCookingTime(int cookieTime) {
        cookie = new Cookie("", 0.0, cookieTime, null, null, null, null, new ArrayList<>());
    }

    @And("a store with the recipe, the cook opening time {string} and ending time {string}")
    public void andStoreCookieCookWithHours(String openingHour, String endingHour) {
        List<Cook> cooks = new ArrayList<>();
        cooks.add(cook);
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(cookie);
        store = new Store(cooks, cookies, "", LocalTime.parse(openingHour),
                LocalTime.parse(endingHour),
                1, new Inventory(new ArrayList<>()),4.3, new ArrayList<>());
    }

    @When("a cart with {int} of the recipe")
    public void andCart(int i) throws InvalidPhoneNumberException {
        client = new RegisteredClient("", "", "0123456789");
        client.getCart().setTax(.1);
        client.getCart().addItem(new Item(i, cookie));
        client.getCart().setPickupTime(LocalTime.parse("10:00"));
    }

    @And("an order with {int} of the recipe")
    public void andOrder(int i) throws InvalidPhoneNumberException {
        client = new RegisteredClient("", "", "0123456789");
        client.getCart().setTax(.1);
        client.getCart().addItem(new Item(i, cookie));
        client.getCart().setPickupTime(LocalTime.parse("10:00"));
        order = new Order("0", client, cook, store);
    }

    @Then("The cook can't do the order")
    public void cookCant() throws PickupTimeNotSetException {
        assertFalse(cook.canTakeTimeSlot(client.getCart().getEstimatedTimeSlot()));
    }

    @Then("The cook can do the order")
    public void cookCan() throws PickupTimeNotSetException {
        assertTrue(cook.canTakeTimeSlot(client.getCart().getEstimatedTimeSlot()));
    }

    @Then("add order throw exception")
    public void addOrderException() {
        assertThrows(CookException.class, () -> cook.addOrder(order));
    }

    @Then("add order does not throw exception")
    public void addOrderWithoutException() {
        assertDoesNotThrow(() -> cook.addOrder(order));
    }

    @And("cook has exactly {int} time slot")
    public void andTimeSlot(int i) {
        assertEquals(cook.getWorkingTimeSlot().size(), i);
    }

    @And("TimeSlot has beginning time {string}")
    public void andTimeSlotBeginning(String beginning) {
        assertEquals(cook.getWorkingTimeSlot().firstKey().getBegin(), LocalTime.parse(beginning));
    }

    @And("TimeSlot has ending time {string}")
    public void andTimeSlotEnding(String ending) {
        assertEquals(cook.getWorkingTimeSlot().firstKey().getEnd(), LocalTime.parse(ending));
    }

    @When("cancel order")
    public void cancelOrder() {
        cook.cancelOrder(order);
    }

}
