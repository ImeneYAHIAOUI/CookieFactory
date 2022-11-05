package fr.unice.polytech.cookie;

import fr.unice.polytech.COD;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.RegisteredClient;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.recipe.*;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.Store;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @And("A client with phone number {string}")
    public void givenAClient(String number) {
        client = new UnregisteredClient(number);
    }

    @Given("a cod to store data")
    public void andGiven() {
        cod = new COD();
        codIngredients.add(new Topping("chocolate chips", 1));
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


        store = new Store(new ArrayList<>(), new ArrayList<>(), "address", LocalTime.parse("08:30"), LocalTime.parse("16:00"), id, inventory);
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
    public void AndRegisteredClientCancelOrder() {

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
    public void AndRegisteredClientCancel2Ordermore10() {

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
    public void AndRegisteredClientCancel2Ordermore8() {

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
    public void AndRegisteredClienthasntcanceledOrders() {


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
}


















