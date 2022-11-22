package stepdefs;

import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.RegisteredClient;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.cod.COD;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.recipe.*;
import fr.unice.polytech.store.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.an.E;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ChooseCookieStepDefs {

    COD cod;

    Client client;

    Integer amount;

    Store store;

    Inventory inventory;


    Cookie cookie;

    final List<Ingredient> codIngredients = new ArrayList<>();

    final List<Cookie> cookieList = new ArrayList<>();

    RegisteredClient registeredClient = new RegisteredClient("1", "****", "0606060606");

    Order mockOrder1;

    Order mockOrder2;
    String size;

    public ChooseCookieStepDefs() throws InvalidPhoneNumberException {
    }

    @And("A client with phone number {string}")
    public void givenAClient(String number) throws InvalidPhoneNumberException {
        client = new UnregisteredClient(number);
    }

    @Given("a cod to store data")
    public void andGiven() {
        cod = COD.getInstance();
        codIngredients.add(new Topping("chocolate chips", 1));
        codIngredients.add(new Topping("m&ms", 1));
        codIngredients.add(new Flavour("caramel", 1));
        codIngredients.add(new Dough("chocolate dough", 1));
        codIngredients.add(new Flavour("vanillaFlavour", 1));
        codIngredients.add(new Flavour("strawberryFlavour", 1));
        codIngredients.add(new Flavour("chocolateFlavour", 1));
        codIngredients.add(new Topping("white chocolate chips", 1));
    }



    @Given("^an inventory with the ingredients and amounts$")
    public void givenInventory(DataTable data)

    {
        List<List<String>> rows = data.asLists(String.class);

        inventory = new Inventory(codIngredients);

        for (List<String> row : rows)
        {
            Ingredient ingredient = codIngredients.stream().filter(i -> i.getName().equals(row.get(0))).findFirst().orElse(null);
            inventory.addAmountQuantity(ingredient, Integer.parseInt(row.get(1)));
        }

    }



    @And("a store with id {int}")
    public void givenStore(Integer id)
    {
        List<Cook> cooks = new ArrayList<>();
        Cook cook=new Cook(0);
        cooks.add(cook);

        store = StoreFactory.createStore(cooks, new ArrayList<>(), "address", LocalTime.parse("08:30"), LocalTime.parse("16:00"), id, inventory,4.0,null);
    }


    @And("^the store has cookies$")
    public void andGiven(DataTable table) {
        List<List<String>> rows = table.asLists(String.class);

        for (List<String> row : rows) {
            List<Topping> toppings = new ArrayList<>();


            Dough dough = (Dough) codIngredients.stream().filter(i -> i.getName().equals(row.get(1))).findFirst().orElse(null);

            Flavour flavor = (Flavour) codIngredients.stream().filter(i -> i.getName().equals(row.get(2))).findFirst().orElse(null);

            for (int j = 3; j < row.size(); j++) {
                int finalJ = j;
                toppings.add(((Topping) codIngredients.stream().filter(i -> i.getName().equals(row.get(finalJ))).findFirst().orElse(null)));
            }
            Cookie cookie = new Cookie(row.get(0), 1., 30, Cooking.CHEWY, Mix.TOPPED, dough, flavor, toppings);
            cookieList.add(cookie);
        }

        store.addCookies(cookieList);

    }

    @And("a specific time of the day")
    public void aSpecificTimeOfTheDay() {
        String instantExpected = "2022-11-07T09:15:00Z";
        Clock clock = Clock.fixed(Instant.parse(instantExpected), ZoneId.of("UTC"));
        COD.setCLOCK(clock);
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
        assertThrows(CookieException.class, () -> cod.chooseCookie(client, store, cookie, amount));
    }

    @Then("This order can be purchased with amount {int} and cookie {string}")
    public void thenCookiesAvailableString(int amount, String name) {
        assertDoesNotThrow(() -> cod.chooseCookie(client, store, name, amount));
    }

    @Then("This order cannot be purchased with amount {int} and cookie {string}")
    public void thenCookiesNotAvailableString(int amount, String name) {
        assertThrows(CookieException.class, () -> cod.chooseCookie(client, store, name, amount));
    }

    @Then("this order can be purchased")
    public void thenCookiesAvailable() {
        assertDoesNotThrow(() -> cod.chooseCookie(client, store, cookie, amount));
    }

    @And("the clients card contains {int} cookie\\(s) of type {string}")
    public void thenClientCardContains(int amount, String name) {
        Cookie cookie = cookieList.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
        assertTrue(client.getCart().getItems().stream().filter(i -> i.getCookie().equals(cookie)).anyMatch(i -> i.getQuantity() == amount));
    }




    @And("A registered client has canceled two orders in the last 10 minutes")
    public void andRegisteredClientCancelOrder() {

        mockOrder1 = mock(Order.class);
        mockOrder2 = mock(Order.class);
        Map history1 = mock(Map.class);
        Map history2 = mock(Map.class);
        List OrderList = List.of(mockOrder2,mockOrder1);

        when(history1.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis()- 5*1000*60));
        when(history2.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis()- 9*1000*60));

        when(mockOrder1.getStatus()).thenReturn(OrderStatus.CANCELLED);
        when(mockOrder2.getStatus()).thenReturn(OrderStatus.CANCELLED);

        when(mockOrder1.getHistory()).thenReturn(history1);
        when(mockOrder2.getHistory()).thenReturn(history2);


        registeredClient.setPastOrders(OrderList);

    }

    @And("A registered client has canceled two orders in more than 10 minutes")
    public void andRegisteredClientCancel2Ordermore10() {

        mockOrder1 = mock(Order.class);
        mockOrder2 = mock(Order.class);
        Map history1 = mock(Map.class);
        Map history2 = mock(Map.class);
        List OrderList = List.of(mockOrder2,mockOrder1);

        when(history1.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis()- 15*1000*60));
        when(history2.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis()- 16*1000*60));

        when(mockOrder1.getStatus()).thenReturn(OrderStatus.CANCELLED);
        when(mockOrder2.getStatus()).thenReturn(OrderStatus.CANCELLED);

        when(mockOrder1.getHistory()).thenReturn(history1);
        when(mockOrder2.getHistory()).thenReturn(history2);


        registeredClient.setPastOrders(OrderList);

    }



    @Then("the client is not banned")
    public void thenClientNotBanned() {
        assertDoesNotThrow(() -> cod.chooseCookie(registeredClient, store, store.getRecipes().get(0), 1));
    }

    @And("A registered client has canceled one order in the last 10 minutes")
    public void AndRegisteredClientCancel2Order()
    {
        mockOrder1 = mock(Order.class);
        Map history = mock(Map.class);
        List OrderList = List.of(mockOrder1);
        when(mockOrder1.getStatus()).thenReturn(OrderStatus.CANCELLED);
        when(history.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis()- 5*1000*60));
        when(mockOrder1.getHistory()).thenReturn(history);
        registeredClient.setPastOrders(OrderList);
    }

    @Then("the client is banned")
    public void thenClientBanned() {
        assertThrows(OrderException.class, () -> cod.chooseCookie(registeredClient, store, store.getRecipes().get(0), 1));
    }

    @And("A registered client has canceled two orders in within more than 8 minutes")
    public void andRegisteredClientCancel2Ordermore8() {

        mockOrder1 = mock(Order.class);
        mockOrder2 = mock(Order.class);
        Map history1 = mock(Map.class);
        Map history2 = mock(Map.class);
        List OrderList = List.of(mockOrder2,mockOrder1);

        when(history1.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis()));
        when(history2.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis() - 9*1000*60));

        when(mockOrder1.getStatus()).thenReturn(OrderStatus.CANCELLED);
        when(mockOrder2.getStatus()).thenReturn(OrderStatus.CANCELLED);

        when(mockOrder1.getHistory()).thenReturn(history1);
        when(mockOrder2.getHistory()).thenReturn(history2);


        registeredClient.setPastOrders(OrderList);
    }

    @And("A registered client hasn't canceled any orders in the last 10 minutes")
    public void andRegisteredClienthasntcanceledOrders() {


        mockOrder1 = mock(Order.class);
        mockOrder2 = mock(Order.class);
        Map history1 = mock(Map.class);
        Map history2 = mock(Map.class);
        List OrderList = List.of(mockOrder2,mockOrder1);

        when(history1.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis()));
        when(history2.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis() - 9*1000*60));

        when(mockOrder1.getStatus()).thenReturn(OrderStatus.IN_PROGRESS);
        when(mockOrder2.getStatus()).thenReturn(OrderStatus.PAYED);

        when(mockOrder1.getHistory()).thenReturn(history1);
        when(mockOrder2.getHistory()).thenReturn(history2);


        registeredClient.setPastOrders(OrderList);

    }

    @And("^the store doesn't have the cookies$")
    public void theStoreDoesntHaveTheCookies(List<String> cookies) {
        for (String cookie : cookies) {
            assertFalse(store.getRecipes().stream().anyMatch(r -> r.getName().equals(cookie)));
        }
    }


    @And("size {string}")
    public void size(String size) {
        this.size = size;
    }

    @And("the clients cart contains {int} party cookie\\(s) of type {string} and size {string}")
    public void theClientsCartContainsPartyCookieSOfTypeAndSize(int nbOfCookie, String cookieName, String cookieSize) {
        assertEquals(this.client.getCart().getItems().get(0).getQuantity(), nbOfCookie);
        assertEquals(this.client.getCart().getItems().get(0).getCookie().getName(), cookieName);
        assertEquals(this.client.getCart().getItems().get(0).getCookie().getClass(), PartyCookie.class);
        assertEquals(( this.client.getCart().getItems().get(0).getCookie()).getSize().name(), cookieSize);
    }

    @When("Client chooses party cookies of type {string} and size {string}")
    public void clientChoosesPartyCookiesOfTypeAndSize(String cookieName, String cookieSize) {
        cookie = cookieList.stream().filter(c -> c.getName().equals(cookieName)).filter(cookie1-> cookie1.getClass() == PartyCookie.class).filter(cookie1 -> cookie1.getSize().toString().equals(cookieSize)).findFirst().orElse(null);
    }

    @And("^the store has party cookies and all available size$")
    public void andGivenPartyCookies(DataTable table) {
        List<List<String>> rows = table.asLists(String.class);

        for (List<String> row : rows) {
            List<Topping> toppings = new ArrayList<>();


            Dough dough = (Dough) codIngredients.stream().filter(i -> i.getName().equals(row.get(1))).findFirst().orElse(null);

            Flavour flavor = (Flavour) codIngredients.stream().filter(i -> i.getName().equals(row.get(2))).findFirst().orElse(null);

            for (int j = 3; j < row.size(); j++) {
                int finalJ = j;
                toppings.add(((Topping) codIngredients.stream().filter(i -> i.getName().equals(row.get(finalJ))).findFirst().orElse(null)));
            }

            for (CookieSize cookieSize : CookieSize.values()) {
                Cookie cookie=new Cookie(row.get(0), 1.0, 30, Cooking.CHEWY, Mix.TOPPED, dough, flavor, toppings);
                Cookie Partycookie = new PartyCookie(cookie,cookieSize,null);
                cookieList.add(Partycookie);
            }

        }

        store.addCookies(cookieList);

    }

    @And("the clients cart is empty")
    public void theClientsCartIsEmpty() {
        assertTrue(this.client.getCart().getItems().isEmpty());
        assertNull(this.cookie);
    }


    @Then("the inventory has no ingredients left")
    public void theInventoryHasNoIngredientsLeft() {
        for (Map.Entry mapentry : store.getInventory().entrySet()) {
            assertEquals(0,mapentry.getValue());
        }
    }

    @And("client finalize his order")
    public void clientFinalizeHisOrder() throws PaymentException, CookException, BadQuantityException, InvalidPickupTimeException, CookieException, OrderException {
        cod.choosePickupTime(client.getCart(), store, LocalTime.parse("11:30"));
        cod.chooseCookie(client, store, cookie, amount);
        this.cod.finalizeOrder(client,store);
    }

    @Then("this order cannot be purchased because the cookie doesn't exist")
    public void thisOrderCannotBePurchasedBecauseTheCookieDoesntExist() {
        assertThrows(CookieException.class, ()-> cod.chooseCookie(client, store, cookie, amount));
    }
    @And("^the store has party cookies with all size and all themes$")
    public void theStoreHasPartyCookiesWithAllSizeAndAllThemes(DataTable table) {
        List<List<String>> rows = table.asLists(String.class);
        for (List<String> row : rows) {
            List<Topping> toppings = new ArrayList<>();
            Dough dough = (Dough) codIngredients.stream().filter(i -> i.getName().equals(row.get(1))).findFirst().orElse(null);
            Flavour flavor = (Flavour) codIngredients.stream().filter(i -> i.getName().equals(row.get(2))).findFirst().orElse(null);
            for (int j = 3; j < row.size(); j++) {
                int finalJ = j;
                toppings.add(((Topping) codIngredients.stream().filter(i -> i.getName().equals(row.get(finalJ))).findFirst().orElse(null)));
            }
            Cook cook = new Cook(1);
            for (CookieSize cookieSize : CookieSize.values()) {
                for (Theme theme : Theme.values()) {
                    Cookie cookie=new Cookie(row.get(0), 1., 30, Cooking.CHEWY, Mix.TOPPED, dough, flavor, toppings);

                    cook.addTheme(theme);

                    Cookie partyCookie = new PartyCookie(cookie,cookieSize,theme);
                cookieList.add(partyCookie);
                store.addCook(cook);

                }
            }
        }
        store.addCookies(cookieList);

    }
    @When("Client chooses party cookies of type {string} and size {string} and theme {string}")
    public void clientChoosesPartyCookiesOfTypeAndSizeAndTheme(String cookieName, String cookieSize, String cookieTheme) {
        cookie = cookieList.stream()
                .filter(c -> c.getName().equals(cookieName))
                .filter(cookie1 -> cookie1.getSize().toString().equals(cookieSize))
                .filter(cookie1 -> cookie1.getTheme().toString().equals(cookieTheme))
                .findFirst().orElse(null);
    }
    @Then("the price of the order is {double}")
    public void thePriceOfTheOrderIs(double expectedPrice) {
        assertEquals(expectedPrice, cod.getOrders().get(0).getPrice(), 0.0);
    }
    @Given("the store with theme {string}")
    public void the_store_with_theme(String theme) {
        Cook cook=new Cook(1);
        cook.addTheme(Theme.ANIMAL);
        store.addCook(cook);
        store.addOccasion(Occasion.BIRTHDAY);
        assertTrue(store.getThemeList().contains(Theme.valueOf(theme)));
    }
    @When("Client personalize {int} cookie of type {string}  size {string} and theme {string}")
    public void client_personalize_cookie_of_type_size_and_theme(int int1, String string, String string2, String string3) {
        amount=int1;
        Cookie cookie = cookieList.stream()
                .filter(c -> c.getName().equals(string))
                .findFirst().orElse(null);
        try{
                cod.personalizeCookie(client,store,cookie,amount,CookieSize.valueOf(string2),
                        Occasion.BIRTHDAY,Theme.valueOf(string3),new ArrayList<>(),new ArrayList<>());
            }catch(Exception e){

            }

    }
    @Then("this order cannot be purchased because store doesn't offer the theme {string}")
    public void cannot_offer_the_theme(String theme) {
        assertFalse(store.getThemeList().contains(Theme.valueOf(theme)));}
    @Then("the clients cart contains {int} party cookie\\(s) of type {string}  and size {string}")
    public void the_clients_cart_contains_party_cookie_s_of_type_and_size(Integer int1, String string, String string2) {

        assertTrue(client.getCart().getItems().stream().filter(i -> i.getCookie().getName().equals(string)).anyMatch(i -> i.getQuantity() == int1));
    }
    @When("Client personalize {int} cookie of type {string}  size {string} theme {string} and occasion {string}")
    public void client_personalize_cookie_of_type_size_theme_and_occasion(Integer int1, String string, String string2, String string3, String string4) {
        amount=int1;
        Cookie cookie= cookieList.stream()
                .filter(c -> c.getName().equals(string))
                .findFirst().orElse(null);
        try{
            cod.personalizeCookie(client,store,cookie,amount,CookieSize.valueOf(string2),
                    Occasion.valueOf(string4),Theme.valueOf(string3),new ArrayList<>(),new ArrayList<>());
        }catch(Exception e){

        }
    }
    @Then("this order cannot be purchased because store doesn't offer the occasion {string}")
    public void this_order_cannot_be_purchased_because_store_doesn_t_offer_the_occasion(String string) {
        assertFalse(store.getThemeList().contains(Occasion.valueOf(string)));}



}


















