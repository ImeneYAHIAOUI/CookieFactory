package stepdefs;

import fr.unice.polytech.cod.COD;
import fr.unice.polytech.exception.BadQuantityException;
import fr.unice.polytech.recipe.*;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.Store;
import fr.unice.polytech.store.StoreFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class StoreStepDefs {
    public final List<Cook> cooks = new ArrayList<>();
    public final List<Cookie> recipes = new ArrayList<>();
    public LocalTime openingTime;
    public LocalTime closingTime;
    public final Inventory inventory = new Inventory(new ArrayList<>());
    Store store;
    COD cod;
    int id;
    Ingredient ingredient;
    int quantity;
    String tmp;
    int quantityToSubtract;

    public StoreStepDefs() {
    }

    @Given("a store with address {string}")
    public void givenAStore(String address) {
        this.store = StoreFactory.createStore(cooks, recipes, address, LocalTime.parse("08:00"),
                LocalTime.parse("20:00"),
                id, inventory, 4.0, new ArrayList<>());
    }

    @And("a cod with the store")
    public void andGivenCODStore() {
        this.cod = COD.getInstance();
        this.cod.getStores().add(store);
    }

    @And("NewOpeningTime with time {string}")
    public void andGivenNewOpeningTime(String newOpeningTime) {
        this.openingTime = LocalTime.parse(newOpeningTime);
    }

    @And("NewClosingTime with time {string}")
    public void andGivenNewClosingTime(String newClosingTime) {
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
    public void givenANewIngredient(String name) {
        tmp = name;
    }

    @And("A price of {double}")
    public void andGivenPrice(double price) {
        ingredient = new Dough(tmp, price);
    }

    @And("a quantity of {int}")
    public void andGivenAQuantity(int quantity) {
        this.quantity = quantity;
    }

    @And("a quantity to subtract of {int}")
    public void andGivenAQuantityToSubtract(int quantityToSubtract) {
        this.quantityToSubtract = quantityToSubtract;
    }

    @When("As a Store Manager I can add the new product in the store")
    public void thenAsAStoreManagerICanAddTheNewProductInTheStore() throws BadQuantityException {
        int storeIndex = this.cod.getStores().indexOf(this.store);
        this.cod.getStores().get(storeIndex).addIngredients(ingredient, quantity);
    }

    @Then("The new product is in the inventory's store")
    public void thenTheNewProductIsInTheInventoryStore() {
        int storeIndex = this.cod.getStores().indexOf(this.store);
        assert this.cod.getStores().get(storeIndex).getInventory().hasIngredient(ingredient);
    }

    @When("As a Store Manager I add a product that already exist in the store")
    public void addProductAlreadyInInventory() throws BadQuantityException {
        int storeIndex = this.cod.getStores().indexOf(this.store);
        this.cod.getStores().get(storeIndex).addIngredients(ingredient, quantity);
    }

    @Then("The amount is added in the inventory and is now {int}")
    public void thenAnAlreadyExist(int new_quantity) throws BadQuantityException {
        int storeIndex = this.cod.getStores().indexOf(this.store);
        this.cod.getStores().get(storeIndex).addIngredients(ingredient, quantity);
        assertEquals(this.cod.getStores().get(storeIndex).getInventory().get(ingredient).intValue(), new_quantity);
    }


    @When("As a Store Manager I subtract that quantity to the product")
    public void asAStoreManagerISubtractThatQuantityToTheProduct() throws BadQuantityException {
        int storeIndex = this.cod.getStores().indexOf(this.store);
        this.cod.getStores().get(storeIndex).addIngredients(ingredient, quantity);
    }

    @Then("An Error appears because I can't have negative quantity")
    public void anErrorAppearsBecauseICanTHaveNegativeQuantity() {
        boolean result = false;
        int storeIndex = this.cod.getStores().indexOf(this.store);
        try {
            this.cod.getStores().get(storeIndex).getInventory().decreaseIngredientQuantity(ingredient, quantityToSubtract);
        } catch (BadQuantityException e) {
            result = true;
        }
        assertTrue(result);
    }

    @Then("No error appears because I don't have negative quantity")
    public void noErrorAppearsBecauseIDonTHaveNegativeQuantity() {
        boolean result = false;
        int storeIndex = this.cod.getStores().indexOf(this.store);
        try {
            this.cod.getStores().get(storeIndex).getInventory().decreaseIngredientQuantity(ingredient, quantityToSubtract);
        } catch (BadQuantityException e) {
            result = true;
        }
        assertFalse(result);
    }

}
