package fr.unice.polytech.cucumber.cookie;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.client.UnregisteredClient;
import fr.unice.polytech.entities.order.*;
import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.cookies.PartyCookie;
import fr.unice.polytech.entities.recipe.cookies.PartyCookieWithBaseRecipeBuilder;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.recipe.enums.CookieSize;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.enums.Theme;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.entities.store.*;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.interfaces.*;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;


public class ChooseCookieStepDefs {

    final List<Ingredient> codIngredients = new ArrayList<>();
    final List<Cookie> cookieList = new ArrayList<>();
    Client client;
    Integer amount;
    Store store;
    Inventory inventory;
    Cookie cookie;
    PartyCookie partyCookie;
    CookieSize cookieSize;
    RegisteredClient registeredClient = new RegisteredClient("****", "0606060606", "");
    Order mockOrder1;
    Order mockOrder2;
    String size;

    @Autowired
    StoreRegistration storeManager;
    @Autowired
    StoreProcessor storeProcessor;
    @Autowired
    InventoryFiller inventoryManager;
    @Autowired
    CookieRegistration cookieRegistration;
    @Autowired
    CookieChoice cookieChoice;
    @Autowired
    OrderFinder orderFinder;
    @Autowired
    OrderUpdater orderUpdater;
    @Autowired
    AgendaProcessor agendaProcessor;

    public ChooseCookieStepDefs() {
    }

    @And("A client with phone number {string}")
    public void givenAClient(String number) {
        client = new UnregisteredClient(number);
    }

    @Given("a cod to store data")
    public void andGiven() {
        codIngredients.add(new Topping(UUID.randomUUID(),"chocolate chips", 1));
        codIngredients.add(new Topping(UUID.randomUUID(),"m&ms", 1));
        codIngredients.add(new Flavour(UUID.randomUUID(),"caramel", 1));
        codIngredients.add(new Dough(UUID.randomUUID(),"chocolate dough", 1));
        codIngredients.add(new Flavour(UUID.randomUUID(),"vanillaFlavour", 1));
        codIngredients.add(new Flavour(UUID.randomUUID(),"strawberryFlavour", 1));
        codIngredients.add(new Flavour(UUID.randomUUID(),"chocolateFlavour", 1));
        codIngredients.add(new Topping(UUID.randomUUID(),"white chocolate chips", 1));
    }


    @Given("^an inventory with the ingredients and amounts$")
    public void givenInventory(DataTable data)

    {
        List<List<String>> rows = data.asLists(String.class);

        inventory = new Inventory(codIngredients);

        for (List<String> row : rows) {
            Ingredient ingredient = codIngredients
                    .stream()
                    .filter(i -> i.getName().equals(row.get(0)))
                    .findFirst()
                    .orElse(null);
            inventoryManager.addAmountQuantity(ingredient, Integer.parseInt(row.get(1)), inventory);
        }

    }


    @And("A store")
    public void givenStore()
    {
        List<Cook> cooks = new ArrayList<>();
        Cook cook = new Cook();
        cooks.add(cook);

        store = StoreFactory.createStore(
                cooks,
                "address",
                LocalTime.parse("08:30"),
                LocalTime.parse("16:00"),
                inventory,
                4.0,
                null
        );
    }


    @And("^the store has cookies$")
    public void andGiven(DataTable table) throws CookieException {
        List<List<String>> rows = table.asLists(String.class);

        for (List<String> row : rows) {
            List<Topping> toppings = new ArrayList<>();
            Dough dough = (Dough) codIngredients
                    .stream()
                    .filter(i -> i.getName().equals(row.get(1)))
                    .findFirst()
                    .orElse(null);
            Flavour flavor = (Flavour) codIngredients
                    .stream()
                    .filter(i -> i.getName().equals(row.get(2)))
                    .findFirst()
                    .orElse(null);
            for (int j = 3; j < row.size(); j++) {
                int finalJ = j;
                toppings.add(((Topping) codIngredients
                        .stream()
                        .filter(i -> i.getName().equals(row.get(finalJ)))
                        .findFirst()
                        .orElse(null)));
            }
            Cookie cookie = new SimpleCookieBuilder()
                    .setName(row.get(0))
                    .setPrice(1.)
                    .setCookingTime(30)
                    .setCooking(Cooking.CHEWY)
                    .setMix(Mix.TOPPED)
                    .setDough(dough)
                    .setFlavour(flavor)
                    .setToppings(toppings)
                    .build();
            cookieList.add(cookie);
            cookieRegistration.suggestRecipe(cookie);
            cookieRegistration.acceptRecipe(cookie,1.);
        }
    }

    @When("Client chooses cookies of type {string}")
    public void whenChooseCookie(String name) {
        cookie = cookieList.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
    }

    @And("amount {int}")
    public void whenChooseAmount(int i) {
        amount = i;
    }

    @Then("this order cannot be purchased")
    public void thenCookiesNotAvailable() {
        assertThrows(CookieException.class, () -> cookieChoice.chooseCookie(client, store, cookie, amount));
    }

    @Then("This order can be purchased with amount {int} and cookie {string}")
    public void thenCookiesAvailableString(int amount, String name) {
        assertDoesNotThrow(() -> cookieChoice.chooseCookie(client, store, name, amount));
    }

    @Then("This order cannot be purchased with amount {int} and cookie {string}")
    public void thenCookiesNotAvailableString(int amount, String name) {
        assertThrows(CookieException.class, () -> cookieChoice.chooseCookie(client, store, name, amount));
    }

    @Then("this order can be purchased")
    public void thenCookiesAvailable() {
        assertDoesNotThrow(() -> cookieChoice.chooseCookie(client, store, cookie, amount));
    }

    @And("the Party cookie has personalized from the cookie")
    public void thePartyCookieHasPersonalizedFromTheCookie() {
        partyCookie = new PartyCookieWithBaseRecipeBuilder()
                .setBaseCookie(cookie)
                .setSize(cookieSize)
                .build();
    }

    @Then("this party cookie can be purchased")
    public void thenPartyCookiesAvailable() {
        assertDoesNotThrow(() -> cookieChoice.chooseCookie(client, store, partyCookie, amount));
    }

    @And("the clients card contains {int} cookie\\(s) of type {string}")
    public void thenClientCardContains(int amount, String name) {
        Cookie cookie = cookieList.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
        assertTrue(client
                           .getCart()
                           .getItems()
                           .stream()
                           .filter(i -> i.getCookie().equals(cookie))
                           .anyMatch(i -> i.getQuantity() == amount));
    }


    @And("A registered client has canceled two orders in the last 10 minutes")
    public void andRegisteredClientCancelOrder() {

        mockOrder1 = mock(Order.class);
        mockOrder2 = mock(Order.class);
        Map<OrderStatus, Date> history1 = spy(new HashMap<>());
        Map<OrderStatus, Date> history2 = spy(new HashMap<>());
        List<Order> OrderList = List.of(mockOrder2, mockOrder1);

        history1.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis() - 5 * 1000 * 60));
        history2.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis() - 9 * 1000 * 60));
        when(mockOrder1.getState()).thenReturn(new OrderCancelled(mockOrder1));
        when(mockOrder2.getState()).thenReturn(new OrderCancelled(mockOrder2));

        when(mockOrder1.getHistory()).thenReturn(history1);
        when(mockOrder2.getHistory()).thenReturn(history2);


        registeredClient.setPastOrders(OrderList);

    }

    @And("A registered client has canceled two orders in more than 10 minutes")
    public void andRegisteredClientCancel2Ordermore10() {

        mockOrder1 = mock(Order.class);
        mockOrder2 = mock(Order.class);
        Map<OrderStatus, Date> history1 = new HashMap<>();
        Map<OrderStatus, Date> history2 = new HashMap<>();
        List<Order> OrderList = List.of(mockOrder2, mockOrder1);

        history1.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis() - 15 * 1000 * 60));
        history2.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis() - 16 * 1000 * 60));
        when(mockOrder1.getState()).thenReturn(new OrderCancelled(mockOrder1));
        when(mockOrder2.getState()).thenReturn(new OrderCancelled(mockOrder2));

        when(mockOrder1.getHistory()).thenReturn(history1);
        when(mockOrder2.getHistory()).thenReturn(history2);


        registeredClient.setPastOrders(OrderList);

    }


    @Then("the client is not banned")
    public void thenClientNotBanned() {
        assertDoesNotThrow(() -> cookieChoice.chooseCookie(registeredClient, store, cookieList.get(0), 1));
    }

    @And("A registered client has canceled one order in the last 10 minutes")
    public void andRegisteredClientCancel2Order() {
        mockOrder1 = mock(Order.class);
        Map<OrderStatus, Date> history = new HashMap<>();
        List<Order> OrderList = List.of(mockOrder1);
        when(mockOrder1.getState()).thenReturn(new OrderCancelled(mockOrder1));
        history.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis() - 5 * 1000 * 60));
        when(mockOrder1.getHistory()).thenReturn(history);
        registeredClient.setPastOrders(OrderList);
    }

    @Then("the client is banned")
    public void thenClientBanned() {
        assertThrows(OrderException.class, () -> cookieChoice.chooseCookie(registeredClient, store, cookieList.get(0), 1));
    }

    @And("A registered client has canceled two orders in within more than 8 minutes")
    public void andRegisteredClientCancel2Ordermore8() {

        mockOrder1 = mock(Order.class);
        mockOrder2 = mock(Order.class);
        Map<OrderStatus, Date> history1 = new HashMap<>();
        Map<OrderStatus, Date> history2 = new HashMap<>();
        List<Order> OrderList = List.of(mockOrder2, mockOrder1);

        history1.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis()));
        history2.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis() - 9 * 1000 * 60));
        when(mockOrder1.getState()).thenReturn(new OrderCancelled(mockOrder1));
        when(mockOrder2.getState()).thenReturn(new OrderCancelled(mockOrder2));

        when(mockOrder1.getHistory()).thenReturn(history1);
        when(mockOrder2.getHistory()).thenReturn(history2);


        registeredClient.setPastOrders(OrderList);
    }

    @And("A registered client hasn't canceled any orders in the last 10 minutes")
    public void andRegisteredClienthasntcanceledOrders() {


        mockOrder1 = mock(Order.class);
        mockOrder2 = mock(Order.class);
        Map<OrderStatus, Date> history1 = new HashMap<>();
        Map<OrderStatus, Date> history2 = new HashMap<>();
        List<Order> OrderList = List.of(mockOrder2, mockOrder1);

        history1.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis()));
        history2.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis() - 9 * 1000 * 60));
        when(mockOrder1.getState()).thenReturn(new OrderInProgress(mockOrder1));
        when(mockOrder2.getState()).thenReturn(new OrderPayed(mockOrder2));

        when(mockOrder1.getHistory()).thenReturn(history1);
        when(mockOrder2.getHistory()).thenReturn(history2);


        registeredClient.setPastOrders(OrderList);

    }

    @And("^the store doesn't have the cookies$")
    public void theStoreDoesntHaveTheCookies(List<String> cookies) {
        for (String cookie : cookies) {
            Cookie c = cookieList.stream().filter(c1 -> c1.getName().equals(cookie)).findFirst().orElse(null);
            assertEquals(0, storeProcessor.getMaxCookieAmount(c, store));
        }
    }


    @And("size {string}")
    public void size(String size) {
        this.size = size;
    }

    @And("the clients cart contains {int} party cookie\\(s) of type {string} and size {string}")
    public void theClientsCartContainsPartyCookieSOfTypeAndSize(int nbOfCookie, String cookieName, String cookieSize) {
        assertEquals(this.client.getCart().getItems().get(0).getQuantity(), nbOfCookie);
        assertEquals(this.client.getCart().getItems().get(0).getCookie().getName(), "personalized " + cookieName);
        assertTrue(this.client.getCart().getItems().get(0).getCookie() instanceof PartyCookie);
        assertEquals((this.client.getCart().getItems().get(0).getCookie()).getSize().name(), cookieSize);
    }

    @When("Client chooses party cookies of type {string} and size {string}")
    public void clientChoosesPartyCookiesOfTypeAndSize(String cookieName, String size) {
        cookie = cookieList.stream().filter(c -> c.getName().equals(cookieName)).findFirst().orElse(null);
        cookieSize = switch (size) {
            case "L" -> CookieSize.L;
            case "XL" -> CookieSize.XL;
            case "XXL" -> CookieSize.XXL;
            default -> throw new IllegalStateException("Unexpected value: " + cookieSize);
        };
    }

    @And("^the store has party cookies and all available size$")
    public void andGivenPartyCookies(DataTable table) throws CookieException {
        List<List<String>> rows = table.asLists(String.class);

        for (List<String> row : rows) {
            List<Topping> toppings = new ArrayList<>();


            Dough dough = (Dough) codIngredients
                    .stream()
                    .filter(i -> i.getName().equals(row.get(1)))
                    .findFirst()
                    .orElse(null);

            Flavour flavor = (Flavour) codIngredients
                    .stream()
                    .filter(i -> i.getName().equals(row.get(2)))
                    .findFirst()
                    .orElse(null);

            for (int j = 3; j < row.size(); j++) {
                int finalJ = j;
                toppings.add(((Topping) codIngredients
                        .stream()
                        .filter(i -> i.getName().equals(row.get(finalJ)))
                        .findFirst()
                        .orElse(null)));
            }

            for (CookieSize cookieSize : CookieSize.values()) {
                Cookie cookie = new SimpleCookieBuilder()
                        .setName(row.get(0))
                        .setSize(cookieSize)
                        .setPrice(1.0)
                        .setCookingTime(30)
                        .setCooking(Cooking.CHEWY)
                        .setMix(Mix.TOPPED)
                        .setDough(dough)
                        .setFlavour(flavor)
                        .setToppings(toppings)
                        .build();
                cookieList.add(cookie);
                cookieRegistration.suggestRecipe(cookie);
                cookieRegistration.acceptRecipe(cookie,1.);
            }

        }


    }

    @And("the clients cart is empty")
    public void theClientsCartIsEmpty() {
        assertTrue(this.client.getCart().getItems().isEmpty());
        assertNull(this.cookie);
    }


    @Then("the inventory has no ingredients left")
    public void theInventoryHasNoIngredientsLeft() {
        for (Map.Entry<Ingredient, Integer> mapentry : store.getInventory().entrySet()) {
            assertEquals(0, (int) mapentry.getValue());
        }
    }

    @And("client finalize his order")
    public void clientFinalizeHisOrder()
            throws CookException, BadQuantityException, InvalidPickupTimeException, CookieException,
            OrderException, IngredientTypeException
    {
        agendaProcessor.choosePickupTime(client.getCart(), store, LocalTime.parse("11:30"));
        cookieChoice.chooseCookie(client, store, cookie, amount);
        this.orderUpdater.finalizeOrder(client, store);
    }

    @And("client finalize his personalized order")
    public void clientFinalizeHisPersonalizedOrder()
            throws CookException, BadQuantityException, InvalidPickupTimeException, CookieException,
            OrderException, IngredientTypeException
    {
        agendaProcessor.choosePickupTime(client.getCart(), store, LocalTime.parse("11:30"));
        cookieChoice.chooseCookie(client, store, partyCookie, amount);
        this.orderUpdater.finalizeOrder(client, store);
    }

    @Then("this order cannot be created because the base recipe doesn't exist")
    public void thisOrderCannotBePurchasedBecauseTheCookieDoesntExist() {
        assertThrows(
                NullPointerException.class,
                () -> new PartyCookieWithBaseRecipeBuilder()
                        .setBaseCookie(cookie)
                        .setSize(cookieSize)
        );
    }

    @And("^the store has party cookies with all size and all themes$")
    public void theStoreHasPartyCookiesWithAllSizeAndAllThemes(DataTable table) throws CookieException {
        List<List<String>> rows = table.asLists(String.class);
        for (List<String> row : rows) {
            List<Topping> toppings = new ArrayList<>();
            Dough dough = (Dough) codIngredients
                    .stream()
                    .filter(i -> i.getName().equals(row.get(1)))
                    .findFirst()
                    .orElse(null);
            Flavour flavor = (Flavour) codIngredients
                    .stream()
                    .filter(i -> i.getName().equals(row.get(2)))
                    .findFirst()
                    .orElse(null);
            for (int j = 3; j < row.size(); j++) {
                int finalJ = j;
                toppings.add(((Topping) codIngredients
                        .stream()
                        .filter(i -> i.getName().equals(row.get(finalJ)))
                        .findFirst()
                        .orElse(null)));
            }
            Cook cook = new Cook();
            for (CookieSize cookieSize : CookieSize.values()) {
                for (Theme theme : Theme.values()) {
                    Cookie cookie = new SimpleCookieBuilder()
                            .setName(row.get(0))
                            .setPrice(1.0)
                            .setCookingTime(30)
                            .setCooking(Cooking.CHEWY)
                            .setMix(Mix.TOPPED)
                            .setDough(dough)
                            .setFlavour(flavor)
                            .setToppings(toppings)
                            .build();

                    cook.addTheme(theme);

                    Cookie partyCookie = new PartyCookieWithBaseRecipeBuilder()
                            .setBaseCookie(cookie)
                            .setSize(cookieSize)
                            .setTheme(theme)
                            .build();
                    cookieList.add(partyCookie);
                cookieRegistration.suggestRecipe(partyCookie);
                cookieRegistration.acceptRecipe(partyCookie,1.);
                    storeManager.addCook(cook, store);

                }
            }
        }
    }

    @When("Client chooses party cookies of type {string} and size {string} and theme {string}")
    public void clientChoosesPartyCookiesOfTypeAndSizeAndTheme(
            String cookieName,
            String cookieSize,
            String cookieTheme
    )
    {
        cookie = cookieList.stream()
                           .filter(c -> c.getName().equals(cookieName))
                           .filter(cookie1 -> cookie1 instanceof PartyCookie)
                           .filter(cookie1 -> cookie1.getSize().toString().equals(cookieSize))
                           .filter(cookie1 -> ((PartyCookie) cookie1).getTheme().toString().equals(cookieTheme))
                           .findFirst().orElse(null);
    }

    @Then("the price of the order is {double}")
    public void thePriceOfTheOrderIs(double expectedPrice) {
        assertEquals(expectedPrice, orderFinder.getOrders().get(0).getPrice(), 0.0);
    }

    @Given("the store with theme {string}")
    public void theStoreWithTheme(String theme) {
        Cook cook = new Cook();
        cook.addTheme(Theme.ANIMAL);
        storeManager.addCook(cook, store);
        storeManager.addOccasion(store, Occasion.BIRTHDAY);
        assertTrue(store.getThemeList().contains(Theme.valueOf(theme)));
    }

    @When("Client personalize {int} cookie of type {string}  size {string} and theme {string}")
    public void clientPersonalizeCookieOfTypeSizeAndTheme(int int1, String string, String string2, String string3)
            throws CookieException, ServiceNotAvailable, OrderException
    {
        amount = int1;
        Cookie cookie = cookieList.stream()
                                  .filter(c -> c.getName().equals(string))
                                  .findFirst().orElse(null);
        cookieChoice.personalizeCookieFromBaseRecipe(
                client,
                store,
                cookie,
                amount,
                CookieSize.valueOf(string2),
                Occasion.BIRTHDAY,
                Theme.valueOf(string3),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    @Then("this order cannot be purchased because store doesn't offer the theme {string}")
    public void cannotOfferTheTheme(String theme) {
        assertFalse(store.getThemeList().contains(Theme.valueOf(theme)));
    }

    @Then("the clients cart contains {int} party cookie\\(s) of type {string} ")
    public void theClientsCartContainsPartyCookieSOfTypeAndSize(Integer int1, String string) {
        assertTrue(client
                           .getCart()
                           .getItems()
                           .stream()
                           .filter(i -> i.getCookie().getName().equals(string))
                           .anyMatch(i -> i.getQuantity() == int1));
    }

    @When("Client personalize {int} cookie of type {string}  size {string} theme {string} and occasion {string}")
    public void clientPersonalizeCookieOfTypeSizeThemeAndOccasion(
            Integer int1,
            String string,
            String string2,
            String string3,
            String string4
    ) throws CookieException, ServiceNotAvailable, OrderException
    {
        amount = int1;
        Cookie cookie = cookieList.stream()
                                  .filter(c -> c.getName().equals(string))
                                  .findFirst().orElse(null);
        cookieChoice.personalizeCookieFromBaseRecipe(
                client,
                store,
                cookie,
                amount,
                CookieSize.valueOf(string2),
                Occasion.valueOf(string4),
                Theme.valueOf(string3),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    @Then("this order cannot be purchased because store doesn't offer the occasion {string}")
    public void thisOrderCannotBePurchasedBecauseStoreDoesnTOfferTheOccasion(String string) {
        assertFalse(store.getOccasionList().contains(Occasion.valueOf(string)));
    }

}


















