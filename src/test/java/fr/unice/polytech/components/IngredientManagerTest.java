package fr.unice.polytech.components;


import fr.unice.polytech.entities.recipe.enums.IngredientType;
import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.StoreFactory;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.IngredientTypeException;
import fr.unice.polytech.interfaces.CookieRegistration;
import fr.unice.polytech.interfaces.IngredientFinder;
import fr.unice.polytech.interfaces.IngredientRegistration;
import fr.unice.polytech.interfaces.StoreRegistration;
import fr.unice.polytech.repositories.IngredientRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IngredientManagerTest {
    @Autowired
    StoreRegistration storeManager;
    @Autowired
    IngredientRegistration ingredientRegistration;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    IngredientFinder ingredientFinder;
    @Autowired
    CookieRegistration cookieRegistration;
    private Store store;
    private String dough = "chocolate";

    @BeforeEach
    public void setup() throws CatalogException, IngredientTypeException, CookieException {
        List<Cook> cooks = new ArrayList<>();
        store = StoreFactory.createStore(
                cooks,
                "",
                LocalTime.parse("08:00"),
                LocalTime.parse("18:00"),
                new Inventory(new ArrayList<>()),
                9.9,
                new ArrayList<>()
        );
        storeManager.addStore(store);
        ingredientRepository.deleteAll();
        /*ingredientRegistration.addIngredient(flavour, 0.0, IngredientType.FLAVOUR);
        ingredientRegistration.addIngredient(topping1, 0.0, IngredientType.TOPPING);
        ingredientRegistration.addIngredient(topping2, 0.0, IngredientType.TOPPING);
        List<Topping> toppingList = new ArrayList<>();
        toppingList.add((Topping) ingredientFinder.getIngredientByName(topping1));
        toppingList.add((Topping) ingredientFinder.getIngredientByName(topping2));*/

    }

    @Test
    public void addIngredientTest() throws IngredientTypeException {
        ingredientRegistration.addIngredient(dough, 0.0, IngredientType.DOUGH);
        assertTrue(ingredientFinder.containsByName(dough));
        assertFalse(ingredientFinder.containsByName("dd"));
    }

    @Test
    public void addIngredientTest1() {
        assertThrows(IngredientTypeException.class, () -> ingredientRegistration.addIngredient(dough, 0.0, null));
        assertFalse(ingredientFinder.containsByName(dough));
    }

    @Test
    public void removeIngredientest() throws IngredientTypeException {
        ingredientRegistration.addIngredient(dough, 0.0, IngredientType.DOUGH);
        assertTrue(ingredientFinder.containsByName(dough));
        ingredientRegistration.removeIngredient(dough, IngredientType.DOUGH);
        assertFalse(ingredientFinder.containsByName(dough));

    }

    @Test
    public void removeIngredientest1() {
        assertFalse(ingredientFinder.containsByName(dough));
        ingredientRegistration.removeIngredient(dough, IngredientType.DOUGH);
        assertFalse(ingredientFinder.containsByName(dough));
    }

    @Test
    public void getIngredientByIdTest() {

        Assert.assertThrows(CatalogException.class, () -> {
            UUID id1;
            do {
                id1 = UUID.randomUUID();
            } while (ingredientFinder.containsById(id1));
            ingredientFinder.getIngredientById(id1);
        });
    }

    @Test
    public void getIngredientByIdTest1() throws CatalogException, IngredientTypeException {
        ingredientRegistration.addIngredient(dough, 0.0, IngredientType.DOUGH);
        assertTrue(ingredientFinder.containsByName(dough));
        Ingredient ingredient = ingredientFinder.getIngredientByName(dough);
        assertEquals(ingredient, ingredientFinder.getIngredientById(ingredient.getId()));

    }

    @Test
    public void getIngredientByNameTest() {
        assertThrows(CatalogException.class, () ->
                ingredientFinder.getIngredientByName(dough)
        );
    }

    @Test
    public void getIngredientByNameTest1() throws CatalogException, IngredientTypeException {
        ingredientRegistration.addIngredient(dough, 0.0, IngredientType.DOUGH);
        Ingredient ingredient = ingredientFinder.getIngredientByName(dough);
        assertTrue(ingredientFinder.containsById(ingredient.getId()));
        assertEquals(ingredient.getName(), dough);
    }
}
