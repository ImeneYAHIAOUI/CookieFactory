package fr.unice.polytech.order;



import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.store.Cook;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    Client client = new UnregisteredClient(0606060606);
    Cook cook = new Cook(1234);
    Order order;

    public OrderTest()  {}

    @Given("an order with id {string}")
    public void givenAnOrder(String id)
    {
        order = new Order(id, client, cook);
    }

    @When("order status is {string}")
    public void whenOrderStatusIs(String status) throws OrderException {

        OrderStatus EnumStatus = StringToOrderStatus(status);
        if(! EnumStatus.equals(order.getStatus())) {
            order.setStatus(EnumStatus);
        }
    }



    @Then("the order can be set to {string}")
    public void thenTheOrderCanBeSetTo(String status)
    {
        OrderStatus EnumStatus = StringToOrderStatus(status);
        assertDoesNotThrow(() -> order.setStatus(EnumStatus));
    }
    @Then("the order can not be set to {string}")
    public void thenTheOrderCanNotBeSetTo(String status)
    {
        OrderStatus EnumStatus = StringToOrderStatus(status);
        assertThrows(OrderException.class, () -> order.setStatus(EnumStatus));
    }

    public OrderStatus StringToOrderStatus(String status)
    {
        switch (status)
        {
            case "NOT_STARTED":
                return OrderStatus.NOT_STARTED;
            case "IN_PROGRESS":
                return OrderStatus.IN_PROGRESS;
            case "READY":
                return OrderStatus.READY;
            case "CANCELLED":
                return OrderStatus.CANCELLED;
            case "COMPLETED":
                return OrderStatus.COMPLETED;
            case "OBSOLETE":
                return OrderStatus.OBSOLETE;
            case "PAYED":
                return OrderStatus.PAYED;
            default:
                return null;
        }
    }

}
