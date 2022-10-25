package fr.unice.polytech.order;

import fr.unice.polytech.COD;
import fr.unice.polytech.SMSService;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.exception.OrderException;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class MakeOrderStepDefs {

    Client client;
    SMSService smsService;
    COD cod;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        smsService = mock(SMSService.class);
        cod = new COD();
        // Mock the sms service singleton
        Field instance = SMSService.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, smsService);
    }

    @Given("a client")
    public void givenAClient() {
        client = new UnregisteredClient(1);
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

    @When("the order status changes to {string}")
    public void whenTheOrderStatusChangesTo(String status) throws OrderException {
        cod.getOrders().get(0).setStatus(OrderStatus.valueOf(status));
    }

    @Then("the client is notified")
    public void thenTheClientIsNotified() {
        verify(smsService, times(1)).notifyClient(client.getPhoneNumber());
    }
}
