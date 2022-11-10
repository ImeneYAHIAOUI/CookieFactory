package fr.unice.polytech.client;

import fr.unice.polytech.COD;
import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.recipe.*;
import fr.unice.polytech.store.Cook;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RetrievePastOrdersStepDef {

    COD cod = new COD();

    RegisteredClient client;

    List<Order> orders = new ArrayList<>();

    List<Order> pastOrders = new ArrayList<>();

    @Given("a Registered client with id {string} and phone number {string}")
    public void givenARegisteredClient(String id, String number) {
        client = new RegisteredClient(id, "*******", number);
    }

    @When("^a the Client has theses past orders$")
    public void whenTheClientHasThesesPartOrders(DataTable data) {
        List<List<String>> rows = data.asLists(String.class);
        int i = 1;
        for (List<String> row : rows) {

            row.forEach(item -> {
                String[] itemSplit = item.split(" ");
                client.getCart().setTax(0.1);
                client.getCart().addItem(new Item(Integer.parseInt(itemSplit[1]), new Cookie(itemSplit[0], 1., 1, Cooking.CHEWY, Mix.MIXED, new Dough("dough", 1.), new Flavour("flavour", 1.), List.of(new Topping("topping", 1.)))));
            });

            Order order = mock(Order.class);
            when(order.getId()).thenReturn(String.valueOf(i));
            when(order.getClient()).thenReturn(client);
            when(order.getCook()).thenReturn(new Cook(i));
            when(order.getStore()).thenReturn(null);
        }
    }

    @And("They want to retrieve their past orders")
    public void andTheyWantToRetrieveTheirPastOrders() {
        pastOrders = cod.getClientPastOrders(client);
    }

    @Then("The client should have their past orders")
    public void thenTheClientShouldHaveTheirPastOrders() {
        for (int i = 0; i < orders.size(); i++) {
            assert(orders.get(i).equals(pastOrders.get(i)));
        }
    }
}



    







