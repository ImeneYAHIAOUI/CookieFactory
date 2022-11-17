package stepdefs;

import fr.unice.polytech.COD;
import fr.unice.polytech.exception.BadQuantityException;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.exception.IngredientTypeException;
import fr.unice.polytech.order.Item;
import fr.unice.polytech.recipe.*;
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

public class InventoryStepDefs {

    COD cod;
    Store store;
    Cookie cookie;
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

    @And("a recipe with dough {string}, flavour {string} and toppings {string} and {string}")
    public void givenARecipe(String dough, String flavour, String topping1, String topping2) throws CatalogException, IngredientTypeException {
        this.cod.addIngredientCatalog(dough, 0.0, IngredientType.DOUGH);
        this.cod.addIngredientCatalog(flavour, 0.0, IngredientType.FLAVOUR);
        this.cod.addIngredientCatalog(topping1, 0.0, IngredientType.TOPPING);
        this.cod.addIngredientCatalog(topping2, 0.0, IngredientType.TOPPING);
        List<Topping> toppingList = new ArrayList<>();
        toppingList.add((Topping) this.cod.getIngredientCatalog(topping1));
        toppingList.add((Topping) this.cod.getIngredientCatalog(topping2));
        this.cookie = new Cookie("test",
                0.0,
                0,
                Cooking.CRUNCHY,
                Mix.MIXED,
                (Dough) this.cod.getIngredientCatalog(dough),
                (Flavour) this.cod.getIngredientCatalog(flavour),
                toppingList);
        this.cod.suggestRecipe(cookie);
        this.cod.acceptRecipe(cookie, 0.0);
    }

    @When("add Inventory ingredient {string} in amount {int}")
    public void addInventory(String name, int ingredient) throws CatalogException, BadQuantityException {
        cod.addInventory(store, name, ingredient);
    }

    @Then("Ingredient {string} not in store")
    public void ingredientNotInStore(String name) throws CatalogException {
        assertFalse(store.getInventory().hasIngredient(cod.getIngredientCatalog(name)));
    }

    @Then("Ingredient {string} in store")
    public void ingredientInStore(String name) throws CatalogException {
        assertTrue(store.getInventory().hasIngredient(cod.getIngredientCatalog(name)));
    }

    @Then("Ingredient {string} in store in amount {int}")
    public void ingredientInStoreAmount(String name, int amount) throws CatalogException {
        assertEquals(amount, (int) store.getInventory().get(cod.getIngredientCatalog(name)));
    }

    @When("someone command a recipe with {int} cookie")
    public void command(int i) throws BadQuantityException {
        this.cod.addCookieInOrder(i, cookie, store);
    }

    @When("someone cancel the order with {int} cookie")
    public void cancel(int i) throws BadQuantityException {
        List<Item> list = new ArrayList<>();
        list.add(new Item(i, cookie));
        COD.putBackIngredientsInInventory(list, store);
    }

    @Then("Exception if we try to command because the store doesn't have enough ingredients")
    public void commandWithException(){
        assertThrows(BadQuantityException.class, () -> this.cod.addCookieInOrder(1, cookie, store));
    }
}
