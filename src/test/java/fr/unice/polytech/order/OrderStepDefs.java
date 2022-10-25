package fr.unice.polytech.order;


import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.store.Cook;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderStepDefs {

    final Client client = new UnregisteredClient(0606060606);
    final Cook cook = new Cook(1234);
    Order order;

    public OrderStepDefs() {
    }

    @Given("an order with id {string}")
    public void givenAnOrder(String id) {
        order = new Order(id, client, cook);
    }

    @When("order status is {string}")
    public void whenOrderStatusIs(String status) throws OrderException {

        OrderStatus EnumStatus = stringToOrderStatus(status);
        if(! EnumStatus.equals(order.getStatus())) {
            order.setStatus(EnumStatus);
        }
    }



    @Then("the order can be set to {string}")
    public void thenTheOrderCanBeSetTo(String status)
    {
        OrderStatus EnumStatus = stringToOrderStatus(status);
        assertDoesNotThrow(() -> order.setStatus(EnumStatus));
    }

    @Then("the order can not be set to {string}")
    public void thenTheOrderCanNotBeSetTo(String status) {
        OrderStatus EnumStatus = stringToOrderStatus(status);
        assertThrows(OrderException.class, () -> order.setStatus(EnumStatus));
    }

    public OrderStatus stringToOrderStatus(String status) {
        return switch (status) {
            case "NOT_STARTED" -> OrderStatus.NOT_STARTED;
            case "IN_PROGRESS" -> OrderStatus.IN_PROGRESS;
            case "READY" -> OrderStatus.READY;
            case "CANCELLED" -> OrderStatus.CANCELLED;
            case "COMPLETED" -> OrderStatus.COMPLETED;
            case "OBSOLETE" -> OrderStatus.OBSOLETE;
            case "PAYED" -> OrderStatus.PAYED;
            default -> null;
        };
    }

}
