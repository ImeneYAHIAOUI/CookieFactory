package fr.unice.polytech.components;

import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.recipe.ingredients.IngredientFactory;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.cookies.PartyCookie;
import fr.unice.polytech.entities.recipe.cookies.PartyCookieWithBaseRecipeBuilder;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.recipe.enums.*;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.StoreFactory;
import fr.unice.polytech.exception.BadQuantityException;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.IngredientTypeException;
import fr.unice.polytech.interfaces.InventoryFiller;
import fr.unice.polytech.interfaces.IngredientFinder;
import fr.unice.polytech.interfaces.IngredientRegistration;
import fr.unice.polytech.interfaces.InventoryUpdater;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class InventoryManagerTest {
    Store store;
    Ingredient ingredient;
    @Autowired
    InventoryFiller inventoryManager;
    @Autowired
    InventoryUpdater inventoryUpdater;
    @Autowired
    IngredientRegistration ingredientRegistration;
    @Autowired
    IngredientFinder ingredientFinder;
    IngredientFactory ingredientFactory;

    List<Item> items;
    @BeforeEach
    public void setUp() {
        store = StoreFactory.createStore(new ArrayList<>(),
                                         "",
                                         LocalTime.parse("08:00"),
                                         LocalTime.parse("18:00"),
                                         new Inventory(new ArrayList<>()),
                                         9.9,
                                         new ArrayList<>());
        ingredientFinder.deleteAllIngredients();
        ingredientFactory = new IngredientFactory();
        ingredient = new Dough(UUID.randomUUID(), "chocolate", 0.0);
    }


    @Test
    public void testAddIngredient() throws IngredientTypeException, CatalogException, BadQuantityException {
        assertThrows(CatalogException.class, () -> inventoryManager.addIngredients(ingredient,  10,store));
        ingredientRegistration.addIngredient("chocolate", 0.0, IngredientType.DOUGH);
        ingredient = ingredientFinder.getIngredientByName("chocolate");
        inventoryManager.addIngredients(ingredient, 10,store);
        assertEquals(10, store.getInventory().get(ingredient));
        inventoryManager.addIngredients(ingredient, 5,store);
        assertEquals(15, store.getInventory().get(ingredient));
        assertThrows(BadQuantityException.class, () -> inventoryManager.addIngredients(ingredient, -5,store));
    }

    @Test
    public void testRemoveIngredient() throws CatalogException, BadQuantityException, IngredientTypeException {
        assertThrows(BadQuantityException.class, () -> inventoryUpdater.decreaseIngredientQuantity(ingredient, 1,store.getInventory()));
        ingredientRegistration.addIngredient("chocolate", 0.0, IngredientType.DOUGH);
        ingredient = ingredientFinder.getIngredientByName("chocolate");
        assertThrows(BadQuantityException.class, () -> inventoryUpdater.decreaseIngredientQuantity(ingredient, 1,store.getInventory()));
        inventoryManager.addIngredients(ingredient, 10,store);
        inventoryUpdater.decreaseIngredientQuantity(ingredient, 5,store.getInventory());
        assertEquals(5, store.getInventory().get(ingredient));
        inventoryUpdater.decreaseIngredientQuantity(ingredient, 5,store.getInventory());
        assertEquals(0, store.getInventory().get(ingredient));
        assertThrows(BadQuantityException.class, () -> inventoryUpdater.decreaseIngredientQuantity(ingredient, 1,store.getInventory()));

    }

    @Test
    public void putBackIngredientsInInventory()
            throws CookieException, IngredientTypeException, CatalogException, BadQuantityException
    {
        ingredientRegistration.addIngredient("chocolate",  1.0, IngredientType.DOUGH);
        Dough dough = (Dough) ingredientFinder.getIngredientByName("chocolate");
        ingredientRegistration.addIngredient("chocolateFlavour",  1.0, IngredientType.FLAVOUR);
        Flavour flavour = (Flavour) ingredientFinder.getIngredientByName("chocolateFlavour");
        ingredientRegistration.addIngredient("chocolate chips",  1.0, IngredientType.TOPPING);
        Topping topping1 = (Topping) ingredientFinder.getIngredientByName("chocolate chips");
        ingredientRegistration.addIngredient("m&ms",  1.0, IngredientType.TOPPING);
        Topping topping2 = (Topping) ingredientFinder.getIngredientByName("m&ms");
        ingredientRegistration.addIngredient("strawberry flakes",  1.0, IngredientType.TOPPING);
        Topping topping3 = (Topping) ingredientFinder.getIngredientByName("strawberry flakes");
        List<Topping> toppings = List.of(topping1, topping2, topping3);
        Cookie cookie1 = new SimpleCookieBuilder()
                .setName("chocolala")
                .setPrice(1.)
                .setCookingTime(15)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(dough)
                .setFlavour(flavour)
                .setToppings(List.of(toppings.get(0)))
                .build();
        Cookie cookie2 = new SimpleCookieBuilder()
                .setName("m&ms")
                .setPrice(1.)
                .setCookingTime(15)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(dough)
                .setFlavour(flavour)
                .setToppings(List.of(toppings.get(1)))
                .build();
        Cookie cookie3 = new SimpleCookieBuilder()
                .setName("strawbarry")
                .setPrice(1.)
                .setCookingTime(15)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(dough)
                .setFlavour(flavour)
                .setToppings(List.of(toppings.get(2)))
                .build();
        items = List.of(new Item( 2,cookie1), new Item( 3,cookie2), new Item( 1,cookie3));
        inventoryUpdater.putBackIngredientsInInventory(items, store);
        assertEquals(6, store.getInventory().get(dough));
        assertEquals(6, store.getInventory().get(flavour));
        assertEquals(2, store.getInventory().get(toppings.get(0)));
        assertEquals(3, store.getInventory().get(toppings.get(1)));
        assertEquals(1, store.getInventory().get(toppings.get(2)));

        PartyCookieWithBaseRecipeBuilder partyCookieWithBaseRecipeBuilder = new PartyCookieWithBaseRecipeBuilder();
        PartyCookie partyCookie =  partyCookieWithBaseRecipeBuilder.setBaseCookie(cookie1).setSize(CookieSize.L).setTheme(Theme.ANIMAL).setPrice(1.0).build();
        items = List.of(new Item( 1,partyCookie));
        inventoryUpdater.putBackIngredientsInInventory(items, store);
        assertEquals(10, store.getInventory().get(dough));
        assertEquals(10, store.getInventory().get(flavour));
        assertEquals(6, store.getInventory().get(toppings.get(0)));
        assertEquals(3, store.getInventory().get(toppings.get(1)));
        assertEquals(1, store.getInventory().get(toppings.get(2)));
        ingredientFinder.deleteAllIngredients();
        assertThrows(CatalogException.class, () -> inventoryUpdater.putBackIngredientsInInventory(items, store));


    }

    @After
    public void tearDown() {
        ingredientFinder.deleteAllIngredients();
    }


}
