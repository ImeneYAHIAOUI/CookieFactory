package stepdefs;

import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.cod.COD;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.exception.InvalidPickupTimeException;
import fr.unice.polytech.order.Item;
import fr.unice.polytech.recipe.Dough;
import fr.unice.polytech.recipe.Flavour;
import fr.unice.polytech.recipe.Topping;
import fr.unice.polytech.store.Store;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChoosePickupTimeStepDefs {
    COD cod;
    Client client;
    Store store;

    @Before(value = "@choose-pickup-time", order = 2)
    public void setUp() throws InvalidPhoneNumberException {
        cod = COD.getInstance();
        cod.initializationCod();
        client = new UnregisteredClient("0123456789");
        store = cod.getStores().get(0);
        store.getInventory().addIngredient(new Dough("chocolate", 1), 100);
        store.getInventory().addIngredient(new Flavour("chocolate", 1), 100);
        store.getInventory().addIngredient(new Topping("chocolate chips", 1), 100);
    }

    @Given("a client with a cart containing some items")
    public void aClientWithACartContainingSomeItems() {
        client.getCart().setTax(.1);
        client.getCart()
                .addItem(new Item(3, cod.getRecipes().get(0)));
    }

    @When("the client chooses a valid pickup time")
    public void theClientChoosesAValidPickupTime() throws InvalidPickupTimeException {
        List<LocalTime> possiblePickupTimes = cod.getPickupTimes(client.getCart(), store);
        cod.choosePickupTime(client.getCart(), store, possiblePickupTimes.get(0));
    }

    @Then("the pickup time is set to the client's cart")
    public void thePickupTimeIsSetToTheClientSCart() {
        assertNotNull(client.getCart().getPickupTime());
    }

    @When("the client chooses a pickup time that is not in the valid pickup time list")
    public void theClientChoosesAPickupTimeThatIsNotInTheValidPickupTimeList() {
        List<LocalTime> possiblePickupTimes = cod.getPickupTimes(client.getCart(), store);
        LocalTime invalidPickupTime = possiblePickupTimes.get(0).minusMinutes(1);
        assertThrows(InvalidPickupTimeException.class, () -> cod.choosePickupTime(client.getCart(), store, invalidPickupTime));
    }

    @Then("the pickup time is not set to the client's cart")
    public void thePickupTimeIsNotSetToTheClientSCart() {
        assertNull(client.getCart().getPickupTime());
    }
}
