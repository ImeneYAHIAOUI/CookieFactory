package stepdefs;


import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.cod.COD;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.exception.InvalidPickupTimeException;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.Store;
import fr.unice.polytech.store.StoreFactory;
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
    COD cod = COD.getInstance();
    Store store = StoreFactory.createStore(List.of(cook), List.of(), "30 Rte des Colles, 06410 Biot",
            LocalTime.parse("08:00"), LocalTime.parse("20:00"),
            1, new Inventory(new ArrayList<>()), 5.0, new ArrayList<>());

    public OrderStepDefs() throws InvalidPhoneNumberException {
    }

    @Given("an order with id {string}")
    public void givenAnOrder(String id) throws InvalidPickupTimeException {
        cod.choosePickupTime(client.getCart(), store, LocalTime.parse("10:00"));
        order = new Order(id, client, cook, store);
    }

    @When("order status is {string}")
    public void whenOrderStatusIs(String status) throws OrderException {
        OrderStatus EnumStatus = OrderStatus.valueOf(status);
        if (!EnumStatus.equals(order.getStatus())) {
            order.setStatus(EnumStatus);
        }
    }


    @Then("the order can be set to {string}")
    public void thenTheOrderCanBeSetTo(String status) {
        assertDoesNotThrow(() -> cod.setStatus(order, OrderStatus.valueOf(status)));
    }

    @Then("the order can not be set to {string}")
    public void thenTheOrderCanNotBeSetTo(String status) {
        assertThrows(OrderException.class, () -> cod.setStatus(order, OrderStatus.valueOf(status)));
    }

    @Then("the order {string} can not be found and canceled")
    public void thenTheOrderCanNotBeFound(String idOrder) {
        assertThrows(OrderException.class, () -> cod.cancelOrder(idOrder));
    }

}
