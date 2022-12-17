package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Cart;
import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.IngredientType;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.StoreFactory;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.interfaces.AgendaProcessor;
import fr.unice.polytech.interfaces.InventoryFiller;
import fr.unice.polytech.interfaces.IngredientFinder;
import fr.unice.polytech.interfaces.IngredientRegistration;
import fr.unice.polytech.repositories.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StoreManagerTest {
    Store store;
    Order order;

    Client client;

    Cookie cookie1;
    Cookie cookie2;
    Cookie cookie3;

    Inventory inventory;

    List<Dough> doughs;
    List<Flavour> flavours;
    List<Topping> toppings;
    @Autowired
    StoreManager storeManager;

    @Autowired
    AgendaProcessor agendaProcessor;

    @Autowired
    InventoryFiller inventoryFiller;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    IngredientFinder ingredientFinder;
    @Autowired
    IngredientRegistration ingredientManager;
    Cook cook;

    @BeforeEach
    public void setUp() throws CookieException, IngredientTypeException {
        ingredientManager.addIngredient("chocolate", 1., IngredientType.DOUGH);
        client = mock(Client.class);
        cook = mock(Cook.class);
        when(client.getCart()).thenReturn(new Cart());
        client.getCart().setPickupTime(LocalTime.of(12, 0).atDate(LocalDate.now(agendaProcessor.getClock())));
        UUID id = UUID.randomUUID();

        order = new Order(id, client, cook, store, Duration.ofMinutes(20));
        doughs = List.of(new Dough(UUID.randomUUID(),"chocolate", 1), new Dough(UUID.randomUUID(),"strawberry", 1));
        flavours = List.of(new Flavour(UUID.randomUUID(),"chocolate", 1), new Flavour(UUID.randomUUID(),"strawberry", 1));
        toppings = List.of(
                new Topping(UUID.randomUUID(),"chocolat chips", 1.),
                new Topping(UUID.randomUUID(),"m&ms", 1.),
                new Topping(UUID.randomUUID(),"strawberry flakes", 1.)
        );
        inventory = mock(Inventory.class);
        cookie1 = new SimpleCookieBuilder()
                .setName("chocolala")
                .setPrice(1.)
                .setCookingTime(15)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(doughs.get(0))
                .setFlavour(flavours.get(0))
                .setToppings(List.of(toppings.get(0)))
                .build();
        cookie2 = new SimpleCookieBuilder()
                .setName("m&ms")
                .setPrice(1.)
                .setCookingTime(15)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(doughs.get(0))
                .setFlavour(flavours.get(0))
                .setToppings(List.of(toppings.get(1)))
                .build();
        cookie3 = new SimpleCookieBuilder()
                .setName("strawbarry")
                .setPrice(1.)
                .setCookingTime(15)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(doughs.get(1))
                .setFlavour(flavours.get(1))
                .setToppings(List.of(toppings.get(2)))
                .build();
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(cookie1);
        cookies.add(cookie2);
        cookies.add(cookie3);
        store = StoreFactory.createStore(List.of(cook), "adresse", LocalTime.parse("10:00")
                , LocalTime.parse("18:00"),
                                         inventory, 7.0, new ArrayList<>()
        );
    }

    @Test
    public void testgetMaxAmountOfCookies() {
        when(inventory.get(doughs.get(0))).thenReturn(4);
        when(inventory.get(flavours.get(0))).thenReturn(4);
        when(inventory.get(toppings.get(0))).thenReturn(4);
        when(inventory.get(toppings.get(1))).thenReturn(2);
        when(inventory.get(doughs.get(1))).thenReturn(3);
        when(inventory.get(flavours.get(1))).thenReturn(1);
        when(inventory.get(toppings.get(2))).thenReturn(5);

        assertEquals(storeManager.getMaxCookieAmount(cookie1, store), 4);
        assertEquals(storeManager.getMaxCookieAmount(cookie2, store), 2);
        assertEquals(storeManager.getMaxCookieAmount(cookie3, store), 1);
    }


    @Test
    public void newStoreClosingBeforeOpening() {
        Store store1 = StoreFactory.createStore(
                new ArrayList<>(),
                "30 Rte des Colles, 06410 Biot",
                LocalTime.parse("20:00"),
                LocalTime.parse("08:00"),
                new Inventory(new ArrayList<>()),
                4.2,
                new ArrayList<>()
        );
        assertEquals(LocalTime.parse("08:00"), store1.getOpeningTime());
        assertEquals(LocalTime.parse("20:00"), store1.getClosingTime());
    }

    @Test
    public void addNegativeIngredients() {
        assertThrows(
                BadQuantityException.class,
                () -> inventoryFiller.addIngredients(ingredientFinder.getIngredientByName("chocolate"), -4, store)
        );
    }

    @Test
    public void cannotAddCookieToStoreBecauseDoesntHaveDough() throws CookieException {
        Dough dough = new Dough(UUID.randomUUID(),"whiteChocolate", 1);
        Cookie cookie = new SimpleCookieBuilder()
                .setName("vanilla")
                .setPrice(1.)
                .setCookingTime(15)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(dough)
                .setFlavour(flavours.get(0))
                .setToppings(List.of(toppings.get(0)))
                .build();
        assertFalse(storeManager.canAddCookieToStore(cookie, store));
    }

    @Test
    public void cannotAddCookieToStoreBecauseDoesntHaveFlavour() throws CookieException {
        Flavour flavour = new Flavour(UUID.randomUUID(),"whiteChocolate", 1);
        Cookie cookie = new SimpleCookieBuilder()
                .setName("vanilla")
                .setPrice(1.)
                .setCookingTime(15)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(doughs.get(0))
                .setFlavour(flavour)
                .setToppings(List.of(toppings.get(0)))
                .build();
        assertFalse(storeManager.canAddCookieToStore(cookie, store));
    }

    @Test
    public void cannotAddCookieToStoreBecauseZeroChosenFlavour()
            throws BadQuantityException, CookieException, CatalogException, IngredientTypeException
    {
        ingredientManager.addIngredient("whiteChocolate", 1,IngredientType.FLAVOUR);
        Flavour flavour = (Flavour) ingredientFinder.getIngredientByName("whiteChocolate");
        inventoryFiller.addIngredients(flavour, 0, store);
        Cookie cookie = new SimpleCookieBuilder()
                .setName("vanilla")
                .setPrice(1.)
                .setCookingTime(15)
                .setCooking(Cooking.CHEWY)
                .setMix(Mix.MIXED)
                .setDough(doughs.get(0))
                .setFlavour(flavour)
                .setToppings(List.of(toppings.get(0)))
                .build();
        assertFalse(storeManager.canAddCookieToStore(cookie, store));
    }

    @Test
    public void addCookToStore() {
        storeManager.addStore(store);
        storeManager.addCook(cook, store);
        assertEquals(2, store.getCooks().size());

    }

    @Test
    public void getStoreThatDoesntExist() {

        assertThrows(StoreException.class, () -> {
            UUID id1;
            do {
                id1 = UUID.randomUUID();
            } while (storeRepository.existsById(id1));
            storeManager.getStore(id1);
        });
    }
}
