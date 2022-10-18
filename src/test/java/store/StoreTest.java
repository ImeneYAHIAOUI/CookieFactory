package store;

import fr.unice.polytech.COD;
import fr.unice.polytech.recipe.Cookie;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Store;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StoreTest {
    public List<Cook> cooks = new ArrayList<>();
    public List<Cookie> recipes = new ArrayList<>();
    public LocalTime openingTime = LocalTime.parse("08:00");
    public LocalTime closingTime = LocalTime.parse("20:00");
    Store store;
    COD cod;
    int id ;

    public StoreTest()  {}

    @Given("a store with address {string}")
    public void givenAStore(String address)
    {
        this.store = new Store(cooks,recipes,address,openingTime,closingTime,id);
    }
    @And("a cod with the store")
    public void AndGivenCODStore()
    {
        this.cod = new COD();
        this.cod.stores.add(store);
    }
    @And("NewOpeningTime with time {string}")
    public void AndGivenNewOpeningTime(String newOpeningTime)
    {
        this.openingTime = LocalTime.parse(newOpeningTime);
    }
    @And("NewClosingTime with time {string}")
    public void AndGivenNewClosingTime(String newClosingTime)
    {
        this.openingTime = LocalTime.parse(newClosingTime);
    }


    @When("The manager set on COD the store hours to the new ones")
    public void whenTheManagerSetOnCODTheStoreHoursToTheNewOnes()
    {
        this.cod.setHours(this.store,this.openingTime,this.closingTime);
    }

    @Then("The store hours are changed to the new ones")
    public void thenTheStoreHasTheGivenHoursOnCOD() {
        assert this.cod.stores.get(store.id).openingTime == this.openingTime;
        assert this.cod.stores.get(store.id).closingTime ==this.closingTime;
    }




}
