package fr.unice.polytech.store;

import fr.unice.polytech.client.RegisteredClient;
import fr.unice.polytech.exception.CookException;
import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.recipe.Cookie;
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
        store = new Store(cooks, cookies, "", LocalTime.parse(openingHour), LocalTime.parse(endingHour), 1, new Inventory(new ArrayList<>()));
    }

    @When("a cart with {int} of the recipe")
    public void andCart(int i) {
        client = new RegisteredClient("", "", "0123456789");
        client.getCart().addItem(new Item(i, cookie));
    }

    @And("an order with {int} of the recipe")
    public void andOrder(int i) {
        client = new RegisteredClient("", "", "0123456789");
        client.getCart().addItem(new Item(i, cookie));
        order = new Order("0", client, cook, store);
    }

    @Then("The cook can't do the order")
    public void cookCant() throws CookException {
        assertFalse(cook.canCook(client.getCart(), store));
    }

    @Then("The cook can do the order")
    public void cookCan() throws CookException {
        assertTrue(cook.canCook(client.getCart(), store));
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
        assertEquals(cook.getWorkingTimeSlot().get(0).getBegin(), LocalTime.parse(beginning));
    }

    @And("TimeSlot has ending time {string}")
    public void andTimeSlotEnding(String ending) {
        assertEquals(cook.getWorkingTimeSlot().get(0).getEnd(), LocalTime.parse(ending));
    }

    @When("cancel order")
    public void cancelOrder() {
        cook.cancelOrder(order);
    }

}
