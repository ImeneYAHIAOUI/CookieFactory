package fr.unice.polytech.cucumber.store;

import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.IngredientType;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.ingredients.*;
import fr.unice.polytech.exception.BadQuantityException;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.IngredientTypeException;
import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.StoreFactory;
import fr.unice.polytech.interfaces.*;
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


public class InventoryStepDefs {

    Store store;
    Cookie cookie;
    @Autowired
    InventoryFiller inventoryManager;
    @Autowired
    InventoryUpdater inventoryUpdater;

    @Autowired
    StoreRegistration storeManager;
    @Autowired
    IngredientRegistration ingredientRegistration;
    @Autowired
    IngredientFinder ingredientFinder;
    @Autowired
    OrderUpdater orderUpdater;
    @Autowired
    CookieRegistration cookieRegistration;

    @Given("cod with store and recipe")
    public void initialization() {
        List<Cook> cooks = new ArrayList<>();
        store = StoreFactory.createStore(cooks,
                "",
                LocalTime.parse("08:00"),
                LocalTime.parse("18:00"),
                new Inventory(new ArrayList<>()),
                9.9,
                new ArrayList<>());
        storeManager.addStore(store);
    }

    @And("a recipe with dough {string}, flavour {string} and toppings {string} and {string}")
    public void givenARecipe(String dough, String flavour, String topping1, String topping2) throws CatalogException, IngredientTypeException, CookieException {
        ingredientRegistration.addIngredient(dough, 0.0, IngredientType.DOUGH);
        ingredientRegistration.addIngredient(flavour, 0.0, IngredientType.FLAVOUR);
        ingredientRegistration.addIngredient(topping1, 0.0, IngredientType.TOPPING);
        ingredientRegistration.addIngredient(topping2, 0.0, IngredientType.TOPPING);
        List<Topping> toppingList = new ArrayList<>();
        toppingList.add((Topping) ingredientFinder.getIngredientByName(topping1));
        toppingList.add((Topping) ingredientFinder.getIngredientByName(topping2));
        Dough d=(Dough) ingredientFinder.getIngredientByName(dough);

        this.cookie = new SimpleCookieBuilder()
                .setName("test")
                .setPrice(0.0)
                .setCookingTime(0)
                .setCooking(Cooking.CRUNCHY)
                .setMix(Mix.MIXED)
                .setDough(d)
                .setFlavour((Flavour) ingredientFinder.getIngredientByName(flavour))
                .setToppings(toppingList)
                .build();
        this.cookieRegistration.suggestRecipe(cookie);
        this.cookieRegistration.acceptRecipe(cookie, 0.0);

    }

    @When("add Inventory ingredient {string} in amount {int}")
    public void addInventory(String name, int ingredient) throws CatalogException, BadQuantityException {
        inventoryManager.addIngredients( ingredientFinder.getIngredientByName(name), ingredient,store);
    }

    @Then("Ingredient {string} not in store")
    public void ingredientNotInStore(String name) throws CatalogException {
        assertFalse(inventoryManager.hasIngredient(ingredientFinder.getIngredientByName(name), store.getInventory()));
    }

    @Then("Ingredient {string} in store")
    public void ingredientInStore(String name) throws CatalogException {
        assertTrue(inventoryManager.hasIngredient(ingredientFinder.getIngredientByName(name), store.getInventory()));
    }

    @Then("Ingredient {string} in store in amount {int}")
    public void ingredientInStoreAmount(String name, int amount) throws CatalogException {
        assertEquals(amount, store.getInventory().get(ingredientFinder.getIngredientByName(name)).intValue());
    }

    @When("someone command a recipe with {int} cookie")
    public void command(int i) throws BadQuantityException, IngredientTypeException {
        this.orderUpdater.removeIngredientsFromInventory(i, cookie, store);
    }

    @When("someone cancel the order with {int} cookie")
    public void cancel(int i) throws BadQuantityException, CatalogException {
        List<Item> list = new ArrayList<>();
        list.add(new Item(i, cookie));
        inventoryUpdater.putBackIngredientsInInventory(list, store);
    }

    @Then("Exception if we try to command because the store doesn't have enough ingredients")
    public void commandWithException(){
        assertThrows(BadQuantityException.class, () -> this.orderUpdater.removeIngredientsFromInventory(1, cookie, store));
    }
}
