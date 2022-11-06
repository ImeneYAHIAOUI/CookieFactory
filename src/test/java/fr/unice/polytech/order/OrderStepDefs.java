package fr.unice.polytech.order;


import fr.unice.polytech.COD;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.exception.InvalidPickupTimeException;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.Store;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderStepDefs {

    final Client client = new UnregisteredClient("0606060606");
    final Cook cook = new Cook(1234);
    Order order;
    COD cod = new COD();
    Store store = new Store(List.of(cook),List.of(), "30 Rte des Colles, 06410 Biot", LocalTime.parse("08:00"), LocalTime.parse("20:00"), 1, new Inventory(new ArrayList<>()));

    public OrderStepDefs() {
    }

    @Given("an order with id {string}")
    public void givenAnOrder(String id) throws InvalidPickupTimeException {
        cod.choosePickupTime(client.getCart(), store, LocalTime.parse("10:00"));
        order = new Order(id, client, cook, store);
    }

    @When("order status is {string}")
    public void whenOrderStatusIs(String status) throws OrderException {

        OrderStatus EnumStatus = stringToOrderStatus(status);
        if(! EnumStatus.equals(order.getStatus())) {
            cod.setStatus(order,EnumStatus);
        }
    }



    @Then("the order can be set to {string}")
    public void thenTheOrderCanBeSetTo(String status)
    {
        OrderStatus EnumStatus = stringToOrderStatus(status);
        assertDoesNotThrow(() -> cod.setStatus(order,EnumStatus));
    }

    @Then("the order can not be set to {string}")
    public void thenTheOrderCanNotBeSetTo(String status) {
        OrderStatus EnumStatus = stringToOrderStatus(status);
        assertThrows(OrderException.class, () -> cod.setStatus(order,EnumStatus));
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
