package fr.unice.polytech.cucumber.store;

import fr.unice.polytech.components.StoreManager;
import fr.unice.polytech.entities.recipe.enums.IngredientType;
import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.exception.BadQuantityException;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.StoreFactory;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.exception.IngredientTypeException;
import fr.unice.polytech.interfaces.InventoryFiller;
import fr.unice.polytech.interfaces.IngredientFinder;
import fr.unice.polytech.interfaces.IngredientRegistration;
import fr.unice.polytech.interfaces.InventoryUpdater;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class StoreStepDefs {
    public final List<Cook> cooks = new ArrayList<>();
    public LocalTime openingTime;
    public LocalTime closingTime;
    public final Inventory inventory = new Inventory(new ArrayList<>());
    Store store;
    Ingredient ingredient;
    int quantity;
    String tmp;
    int quantityToSubtract;
    @Autowired
    StoreManager storeManager;
    @Autowired
    InventoryFiller inventoryManager;
    @Autowired
    InventoryUpdater inventoryUpdater;
    @Autowired
    IngredientFinder ingredientFinder;
    @Autowired
    IngredientRegistration ingredientManager;

    public StoreStepDefs() {
    }

    @Given("a store with address {string}")
    public void givenAStore(String address) {
        ingredientFinder.deleteAllIngredients();
        this.store = StoreFactory.createStore(cooks, address, LocalTime.parse("08:00"),
                LocalTime.parse("20:00"),
                inventory, 4.0, new ArrayList<>());
    }

    @And("a cod with the store")
    public void andGivenCODStore() {
        this.storeManager.addStore(store);
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
        storeManager.setHours(this.store,this.openingTime,this.closingTime);
    }

    @Then("The store hours are changed to the new ones")
    public void thenTheStoreHasTheGivenHoursOnCOD() {
        int storeIndex = this.storeManager.getStores().indexOf(this.store);
        if (this.openingTime.isBefore(this.closingTime)){
            assert this.storeManager.getStores().get(storeIndex).getOpeningTime() == this.openingTime;
            assert this.storeManager.getStores().get(storeIndex).getClosingTime() == this.closingTime;
        }
        else{
            assert this.storeManager.getStores().get(storeIndex).getOpeningTime() == this.closingTime;
            assert this.storeManager.getStores().get(storeIndex).getClosingTime() == this.openingTime;
        }

    }

    @Given("A new Ingredient with name {string}")
    public void givenANewIngredient(String name) {
        tmp = name;
    }

    @And("A price of {double}")
    public void andGivenPrice(double price) throws IngredientTypeException, CatalogException {
        ingredientManager.addIngredient(tmp, price, IngredientType.DOUGH);
        ingredient = ingredientFinder.getIngredientByName(tmp);
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
    public void thenAsAStoreManagerICanAddTheNewProductInTheStore()
            throws BadQuantityException, CatalogException
    {
        int storeIndex = this.storeManager.getStores().indexOf(this.store);
        inventoryManager.addIngredients(ingredientFinder.getIngredientByName(ingredient.getName()), quantity, this.storeManager.getStores().get(storeIndex));
    }

    @Then("The new product is in the inventory's store")
    public void thenTheNewProductIsInTheInventoryStore() throws CatalogException {
        int storeIndex = this.storeManager.getStores().indexOf(this.store);
        assertTrue(inventoryManager.hasIngredient(ingredientFinder.getIngredientByName(ingredient.getName()),this.storeManager.getStores().get(storeIndex).getInventory()));
    }

    @When("As a Store Manager I add a product that already exist in the store")
    public void addProductAlreadyInInventory() throws BadQuantityException, CatalogException {
        int storeIndex = this.storeManager.getStores().indexOf(this.store);
        inventoryManager.addIngredients(ingredientFinder.getIngredientByName(ingredient.getName()), quantity, this.storeManager.getStores().get(storeIndex));
    }

    @Then("The amount is added in the inventory and is now {int}")
    public void thenAnAlreadyExist(int new_quantity)
            throws BadQuantityException, CatalogException
    {
        int storeIndex = this.storeManager.getStores().indexOf(this.store);
        inventoryManager.addIngredients(ingredientFinder.getIngredientByName(ingredient.getName()), quantity, this.storeManager.getStores().get(storeIndex));
        assertEquals(storeManager.getStores().get(storeIndex).getInventory().get(ingredientFinder.getIngredientByName(ingredient.getName())).intValue(), new_quantity);
    }


    @When("As a Store Manager I subtract that quantity to the product")
    public void asAStoreManagerISubtractThatQuantityToTheProduct() throws BadQuantityException,
            CatalogException
    {
        int storeIndex = this.storeManager.getStores().indexOf(this.store);
        inventoryManager.addIngredients(ingredientFinder.getIngredientByName(ingredient.getName()), quantity, this.storeManager.getStores().get(storeIndex));
    }

    @Then("An Error appears because I can't have negative quantity")
    public void anErrorAppearsBecauseICanTHaveNegativeQuantity() {
        boolean result = false;
        int storeIndex = this.storeManager.getStores().indexOf(this.store);
        assertThrows(BadQuantityException.class, () -> inventoryUpdater.decreaseIngredientQuantity(ingredient, quantityToSubtract,this.storeManager.getStores().get(storeIndex).getInventory()));

    }

    @Then("No error appears because I don't have negative quantity")
    public void noErrorAppearsBecauseIDonTHaveNegativeQuantity() {
        int storeIndex = this.storeManager.getStores().indexOf(this.store);

        assertDoesNotThrow(() -> inventoryUpdater.decreaseIngredientQuantity(ingredient, quantityToSubtract,this.storeManager.getStores().get(storeIndex).getInventory()));

    }



}
