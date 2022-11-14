package stepdefs;

import fr.unice.polytech.COD;
import fr.unice.polytech.exception.AlreadyExistException;
import fr.unice.polytech.exception.BadQuantityException;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.recipe.IngredientType;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.Store;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FillInventoryStepDefs {

    COD cod;
    Store store;
    @Given("cod with store and recipe")
    public void initialization(){
        cod = new COD();
        cod.initializationCod();
        List<Cook> cooks = new ArrayList<>();
        store = new Store(cooks,
                new ArrayList<>(),
                "",
                LocalTime.parse("08:00"),
                LocalTime.parse("18:00"),
                2,
                new Inventory(new ArrayList<>()),
                9.9,
                new ArrayList<>());
        cod.addStore(store);
    }

    @When("add Inventory ingredient {string} in amount {int}")
    public void addInventory(String name, int ingredient) throws CatalogException, AlreadyExistException, BadQuantityException {
        cod.addIngredientCatalog(name, 0, IngredientType.DOUGH);
        cod.addInventory(store, name, ingredient);
    }

    @Then("Ingredient {string} not in store")
    public void ingredientNotInStore(String name) throws CatalogException {
        cod.addIngredientCatalog(name, 0, IngredientType.DOUGH);
        assertFalse(store.getInventory().hasIngredient(cod.getIngredientCatalog(name)));
    }

    @Then("Ingredient {string} in store")
    public void ingredientInStore(String name) throws CatalogException {
        assertTrue(store.getInventory().hasIngredient(cod.getIngredientCatalog(name)));
    }

    @Then("Ingredient {string} in store in amount {int}")
    public void ingredientInStoreAmount(String name, int amount) throws CatalogException {
        assertEquals((int) store.getInventory().get(cod.getIngredientCatalog(name)), amount);
    }
}
