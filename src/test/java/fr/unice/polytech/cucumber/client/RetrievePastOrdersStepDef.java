package fr.unice.polytech.cucumber.client;

import fr.unice.polytech.components.ClientManager;
import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.ingredients.*;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.interfaces.CartHandler;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RetrievePastOrdersStepDef  {

    RegisteredClient client;
    List<Order> orders = new ArrayList<>();
    List<Order> pastOrders = new ArrayList<>();

    @Autowired
    CartHandler cartManager;
    @Autowired
    ClientManager clientManager;

   @Given("a Registered client with id {string} and phone number {string}")
    public void givenARegisteredClient(String mail, String number) throws InvalidPhoneNumberException {
       client = new RegisteredClient( mail, "*******", number);
    }

    @When("^a the Client has theses past orders$")
    public void whenTheClientHasThesesPartOrders(DataTable data) {
        List<List<String>> rows = data.asLists(String.class);
        for (List<String> row : rows) {

            row.forEach(item -> {
                String[] itemSplit = item.split(" ");
                client.getCart().setTax(0.1);
                try {
                    Cookie cookie = new SimpleCookieBuilder()
                            .setName(itemSplit[0])
                            .setPrice(1.)
                                    .setCookingTime(1)
                                    .setCooking(Cooking.CHEWY)
                                    .setMix(Mix.MIXED)
                                    .setDough(new Dough(UUID.randomUUID(),"dough", 1.))
                                    .setFlavour(new Flavour(UUID.randomUUID(),"flavour", 1.))
                                    .setToppings(List.of(new Topping(UUID.randomUUID(),"topping", 1.)))
                                    .build();
                    cartManager.addItem(new Item(Integer.parseInt(itemSplit[1]),cookie) ,client.getCart());
                } catch (CookieException e) {
                    throw new RuntimeException(e);
                }
            });

            Order order = mock(Order.class);
            when(order.getId()).thenReturn(UUID.randomUUID());
            when(order.getClient()).thenReturn(client);
            when(order.getCook()).thenReturn(new Cook());
            when(order.getStore()).thenReturn(null);
        }
    }

    @And("They want to retrieve their past orders")
    public void andTheyWantToRetrieveTheirPastOrders() {
        pastOrders = clientManager.getClientPastOrders(client);
    }

    @Then("The client should have their past orders")
    public void thenTheClientShouldHaveTheirPastOrders() {
        for (int i = 0; i < orders.size(); i++) {
            assert(orders.get(i).equals(pastOrders.get(i)));
        }
    }
}



    







