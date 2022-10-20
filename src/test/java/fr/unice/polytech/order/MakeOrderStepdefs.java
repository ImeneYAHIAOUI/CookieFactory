package fr.unice.polytech.order;

import fr.unice.polytech.COD;
import fr.unice.polytech.client.Client;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MakeOrderStepdefs {
    COD cod = new COD();
    Client client;

    @Given("a client")
    public void givenAClient() {
        client = new Client(1);
    }

    @And("an order from this client")
    public void andAnOrderFromThisClient() {
        cod.getOrders().add(new Order("1", client, null));
    }

    @Then("the order has status {string}")
    public void thenTheOrderHasStatus(String status) {
        assert cod.getOrders().get(0).getStatus().toString().equals(status);
    }

    @When("the client cancels the order")
    public void whenTheClientCancelsTheOrder() throws OrderException {
        cod.cancelOrder(cod.getOrders().get(0));
    }

    @And("The order status is {string}")
    public void andTheOrderStatusIs(String status) {
        assert cod.getOrders().get(0).getStatus().toString().equals(status);
    }
}
