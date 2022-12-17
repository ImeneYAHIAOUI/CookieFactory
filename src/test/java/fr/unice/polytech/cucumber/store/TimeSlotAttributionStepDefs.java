package fr.unice.polytech.cucumber.store;

import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.store.*;
import fr.unice.polytech.exception.CookException;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.PickupTimeNotSetException;
import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.interfaces.AgendaProcessor;
import fr.unice.polytech.interfaces.CartHandler;
import fr.unice.polytech.interfaces.CookieRegistration;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TimeSlotAttributionStepDefs {
    Cook cook;
    Cookie cookie;
    Store store;
    RegisteredClient client;
    Order order;
    @Autowired
    CartHandler cartManager;
    @Autowired
    AgendaProcessor agendaProcessor;
    @Autowired
    CookieRegistration cookieRegistration;

    @Given("a cook with an empty timetable")
    public void givenEmptyCook() {
        cook = new Cook();
    }

    @Given("a cook with a timetable with a timeTable occupied from {string} to {string} on the date {string}")
    public void givenCookWithATimeTableOccupiedFromToOnTheDate(String start, String end, String date) {
        cook = new Cook();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        LocalDateTime startDateTime = LocalTime.parse(start).atDate(localDate);
        LocalDateTime endDateTime = LocalTime.parse(end).atDate(localDate);
        TimeSlot timeSlot = new TimeSlot(startDateTime, endDateTime);
        client = new RegisteredClient("", "", "0123456789");
        client.getCart().setTax(.1);

        cartManager.addItem(new Item(1, cookie),client.getCart());
        client.getCart().setPickupTime(LocalTime.parse("10:00").atDate(localDate));
        UUID id=UUID.randomUUID();

        order = new Order(id,client, cook, store, Duration.ofMinutes(client.getCart().getItems().get(0).getCookie().getCookingTime()));
        cook.getWorkingTimeSlot().put(timeSlot,order);
    }

    @Given("a cook with a timetable with a timeTable occupied from {string} to {string}")
    public void givenCookWithATimeTableOccupied(String start, String end) {
        cook = new Cook();
        LocalDateTime startDateTime = LocalTime.parse(start).atDate(LocalDate.now(agendaProcessor.getClock()));
        LocalDateTime endDateTime = LocalTime.parse(end).atDate(LocalDate.now(agendaProcessor.getClock()));
        TimeSlot timeSlot = new TimeSlot(startDateTime, endDateTime);
        client = new RegisteredClient("", "", "0123456789");
        client.getCart().setTax(.1);
        cartManager.addItem(new Item(1, cookie),client.getCart());
        client.getCart().setPickupTime(LocalTime.parse("10:00").atDate(LocalDate.now(agendaProcessor.getClock())));
        UUID id=UUID.randomUUID();

        order = new Order(id,client, cook, store, Duration.ofMinutes(client.getCart().getItems().get(0).getCookie().getCookingTime()));
        cook.getWorkingTimeSlot().put(timeSlot,order);
    }

    @And("a random recipe with cooking time of {int}")
    public void andRandomRecipeWithCookingTime(int cookieTime) throws CookieException {
        cookie = new SimpleCookieBuilder().setName("").setPrice(0.0).setCookingTime(cookieTime).build();
    }

    @And("a store with the recipe, the cook opening time {string} and ending time {string}")
    public void andStoreCookieCookWithHours(String openingHour, String endingHour) {
        List<Cook> cooks = new ArrayList<>();
        cooks.add(cook);
        List<Cookie> cookies = new ArrayList<>();
        cookieRegistration.suggestRecipe(cookie);
        cookieRegistration.acceptRecipe(cookie, 10.);
        cookies.add(cookie);
        store = StoreFactory.createStore(cooks, "", LocalTime.parse(openingHour),
                LocalTime.parse(endingHour),
                new Inventory(new ArrayList<>()), 4.3, new ArrayList<>());
    }

    @When("a cart with {int} of the recipe")
    public void andCart(int i) {
        client = new RegisteredClient("", "", "0123456789");
        client.getCart().setTax(.1);
        cartManager.addItem(new Item(i, cookie),client.getCart());
        client.getCart().setPickupTime(LocalTime.parse("10:00").atDate(LocalDate.now(agendaProcessor.getClock())));
    }

    @And("an order with {int} of the recipe")
    public void andOrder(int i) {
        client = new RegisteredClient("", "", "0123456789");
        client.getCart().setTax(.1);
        cartManager.addItem(new Item(i, cookie),client.getCart());
        client.getCart().setPickupTime(LocalTime.parse("10:00").atDate(LocalDate.now(agendaProcessor.getClock())));
        UUID id=UUID.randomUUID();

        order = new Order(id,client, cook, store, Duration.ofMinutes(client.getCart().getItems().get(0).getCookie().getCookingTime()));
    }

    @Then("The cook can't do the order")
    public void cookCant() throws PickupTimeNotSetException {
        assertFalse(cook.canTakeTimeSlot(cartManager.getEstimatedTimeSlot(client.getCart())));
    }

    @Then("The cook can do the order")
    public void cookCan() throws PickupTimeNotSetException {
        assertTrue(cook.canTakeTimeSlot(cartManager.getEstimatedTimeSlot(client.getCart())));
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

    @When("a cart with {int} of the recipe for the date {string}")
    public void andCart(int i, String date) {
        client = new RegisteredClient("", "", "0123456789");
        client.getCart().setTax(.1);
        cartManager.addItem(new Item(i, cookie),client.getCart());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        client.getCart().setPickupTime(LocalTime.parse("10:00").atDate(localDate));
    }

    @And("an order with {int} of the recipe for the date {string}")
    public void andOrder(int i,String date) {
        client = new RegisteredClient("", "", "0123456789");
        client.getCart().setTax(.1);
        cartManager.addItem(new Item(i, cookie),client.getCart());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        client.getCart().setPickupTime(LocalTime.parse("10:00").atDate(localDate));
        UUID id=UUID.randomUUID();

        order = new Order(id, client, cook, store, Duration.ofMinutes(client.getCart().getItems().get(0).getCookie().getCookingTime()));
    }
}
