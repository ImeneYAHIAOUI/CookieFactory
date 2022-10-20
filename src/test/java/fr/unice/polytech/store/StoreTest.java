package fr.unice.polytech.store;

import fr.unice.polytech.COD;
import fr.unice.polytech.recipe.Cookie;
import fr.unice.polytech.recipe.Ingredient;
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
    public LocalTime openingTime;
    public LocalTime closingTime;
    public Inventory inventory = new Inventory(new ArrayList<>());
    Store store;
    COD cod;
    int id ;
    Ingredient ingredient;
    int quantity;
    String tmp;

    public StoreTest()  {}

    @Given("a store with address {string}")
    public void givenAStore(String address)
    {
        this.store = new Store(cooks,recipes,address,LocalTime.parse("08:00"),LocalTime.parse("20:00"),id,inventory);
    }
    @And("a cod with the store")
    public void AndGivenCODStore()
    {
        this.cod = new COD();
        this.cod.getStores().add(store);
    }
    @And("NewOpeningTime with time {string}")
    public void AndGivenNewOpeningTime(String newOpeningTime)
    {
        this.openingTime = LocalTime.parse(newOpeningTime);
    }
    @And("NewClosingTime with time {string}")
    public void AndGivenNewClosingTime(String newClosingTime)
    {
        this.closingTime = LocalTime.parse(newClosingTime);
    }


    @When("The manager set on COD the store hours to the new ones")
    public void whenTheManagerSetOnCODTheStoreHoursToTheNewOnes()
    {
        this.cod.setHours(this.store,this.openingTime,this.closingTime);
    }

    @Then("The store hours are changed to the new ones")
    public void thenTheStoreHasTheGivenHoursOnCOD() {
        int storeIndex = this.cod.getStores().indexOf(this.store);
        if (this.openingTime.isBefore(this.closingTime)){
            assert this.cod.getStores().get(storeIndex).openingTime == this.openingTime;
            assert this.cod.getStores().get(storeIndex).closingTime == this.closingTime;
        }
        else{
            assert this.cod.getStores().get(storeIndex).openingTime == this.closingTime;
            assert this.cod.getStores().get(storeIndex).closingTime == this.openingTime;
        }

    }

    @Given("A new Ingredient with name {string}")
    public void givenANewIngredient(String name)
    {
        tmp = name;
    }
    @And("A price of {double}")
    public void AndGivenPrice(double price)
    {
        ingredient = new Ingredient(tmp,price);
    }
    @And("a quantity of {int}")
    public void AndGivenAQuantity(int quantity)
    {
        this.quantity = quantity;
    }
    @When("As a Store Manager I can add the new product in the store")
    public void thenAsAStoreManagerICanAddTheNewProductInTheStore()
    {
        int storeIndex = this.cod.getStores().indexOf(this.store);
        this.cod.getStores().get(storeIndex).addIngredients(ingredient,quantity);
    }
    @Then("The new product is in the inventory's store")
    public void thenTheNewProductIsInTheInventoryStore()
    {
        int storeIndex = this.cod.getStores().indexOf(this.store);
        assert this.cod.getStores().get(storeIndex).getInventory().hasIngredient(ingredient);
    }

}
