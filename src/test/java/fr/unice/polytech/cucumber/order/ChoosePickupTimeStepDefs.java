package fr.unice.polytech.cucumber.order;

import fr.unice.polytech.components.Agenda;
import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.UnregisteredClient;
import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.IngredientType;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.IngredientTypeException;
import fr.unice.polytech.exception.InvalidPickupTimeException;
import fr.unice.polytech.interfaces.*;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChoosePickupTimeStepDefs {

    Client client;
    Store store;
    @Autowired
    CartHandler cartManager;
    @Autowired
    StoreFinder storeFinder;
    @Autowired
    StoreRegistration storeProcessor;
    AgendaProcessor agendaProcessor;
    @Autowired
    CookieFinder cookieFinder;
    @Autowired
    CookieRegistration cookieRegistration;
    @Autowired
    IngredientRegistration ingredientRegistration;
    @Autowired
    IngredientFinder ingredientFinder;

    @Before(value = "@choose-pickup-time", order = 2)
    public void setUp() throws CookieException, CatalogException, IngredientTypeException {
        agendaProcessor = new Agenda(cartManager);
        LocalDateTime date = LocalDateTime.of(2022, 12, 17, 8, 0);
        ZoneId zoneId = ZoneId.systemDefault();
        agendaProcessor.setClock(Clock.fixed(date.atZone(zoneId).toInstant(), zoneId));
        client = new UnregisteredClient("0123456789");
        storeProcessor.addStore(2, new Inventory(new ArrayList<>()), "", "08:00", "18:00", 10., new ArrayList<>());
        store = storeFinder.getStores().get(0);
        ingredientRegistration.addIngredient("chocolateD", 1., IngredientType.DOUGH);
        ingredientRegistration.addIngredient("chocolateF", 1., IngredientType.FLAVOUR);
        ingredientRegistration.addIngredient("chocolateT", 1., IngredientType.TOPPING);
        cookieRegistration.suggestRecipe(
                "",
                0,
                0,
                Cooking.CHEWY,
                Mix.TOPPED,
                ingredientFinder.getIngredientByName("chocolateD"),
                ingredientFinder.getIngredientByName("chocolateF"),
                List.of(
                        ingredientFinder.getIngredientByName("chocolateT"))
        );
        cookieRegistration.acceptRecipe(cookieFinder.getSuggestedRecipes().get(0), 10.);
    }

    @Given("a client with a cart containing some items")
    public void aClientWithACartContainingSomeItems() {
        client.getCart().setTax(.1);
        cartManager.addItem(new Item(3, cookieFinder.getRecipes().get(0)), client.getCart());
    }

    @When("the client chooses a valid pickup time")
    public void theClientChoosesAValidPickupTime() throws InvalidPickupTimeException {
        List<LocalTime> possiblePickupTimes = agendaProcessor.getPossiblePickupTimes(client.getCart(), store);
        agendaProcessor.choosePickupTime(client.getCart(), store, possiblePickupTimes.get(0));
    }

    @When("the client chooses a valid pickup time for the date {string}")
    public void theClientChoosesAValidPickupTime(String date) throws InvalidPickupTimeException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        List<LocalDateTime> possiblePickupTimes = agendaProcessor.getPossiblePickupTimesForADate(
                client.getCart(),
                localDate,
                store
        );
        agendaProcessor.choosePickupTimeAtAnotherDate(client.getCart(), store, possiblePickupTimes.get(0));
    }

    @Then("the pickup time is set to the client's cart")
    public void thePickupTimeIsSetToTheClientSCart() {
        assertNotNull(client.getCart().getPickupTime());
    }

    @When("the client chooses a pickup time that is not in the valid pickup time list")
    public void theClientChoosesAPickupTimeThatIsNotInTheValidPickupTimeList() {
        List<LocalTime> possiblePickupTimes = agendaProcessor.getPossiblePickupTimes(client.getCart(), store);
        LocalTime invalidPickupTime = possiblePickupTimes.get(0).minusMinutes(1);
        assertThrows(
                InvalidPickupTimeException.class,
                () -> agendaProcessor.choosePickupTime(client.getCart(), store, invalidPickupTime)
        );
    }

    @When("the client chooses a pickup time that is not in the valid pickup time list for the date {string}")
    public void theClientChoosesAPickupTimeThatIsNotInTheValidPickupTimeListForTheDate(String date)
            throws InvalidPickupTimeException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        List<LocalDateTime> possiblePickupTimes = agendaProcessor.getPossiblePickupTimesForADate(
                client.getCart(),
                localDate,
                store
        );
        LocalDateTime invalidPickupTime = possiblePickupTimes.get(0).minusMinutes(1);
        assertThrows(
                InvalidPickupTimeException.class,
                () -> agendaProcessor.choosePickupTimeAtAnotherDate(client.getCart(), store, invalidPickupTime)
        );
    }

    @When("the client chooses an invalid date")
    public void theClientChoosesAnInvalidDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate = LocalDate.parse("01/01/2020", formatter);
        assertThrows(
                InvalidPickupTimeException.class,
                () -> agendaProcessor.getPossiblePickupTimesForADate(client.getCart(), localDate, store)
        );
    }

    @Then("the pickup time is not set to the client's cart")
    public void thePickupTimeIsNotSetToTheClientSCart() {
        assertNull(client.getCart().getPickupTime());
    }
}
