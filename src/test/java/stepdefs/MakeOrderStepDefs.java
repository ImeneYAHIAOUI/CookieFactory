package stepdefs;

import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.NotificationMessage;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.cod.COD;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.services.SMSService;
import fr.unice.polytech.services.StatusScheduler;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Store;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.time.LocalTime;

import static org.mockito.Mockito.*;

public class MakeOrderStepDefs {

    Client client;
    SMSService smsService;
    COD cod;
    Store store;
    private ByteArrayOutputStream outContent ;

    public void setUp() throws NoSuchFieldException, IllegalAccessException, CookieException {
        smsService = mock(SMSService.class);
        cod = COD.getInstance();
        cod.initializationCod();
        store = cod.getStores().get(0);
        // Mock the sms service singleton
        Field instance = SMSService.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(instance, smsService);
        // Mock the cod singleton
        Field codInstance = COD.class.getDeclaredField("INSTANCE");
        codInstance.setAccessible(true);
        codInstance.set(codInstance, cod);
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Given("a client")
    public void givenAClient() throws InvalidPhoneNumberException, NoSuchFieldException, IllegalAccessException, CookieException {
        setUp();
        client = new UnregisteredClient("0123456789");
    }

    @And("an order from this client")
    public void andAnOrderFromThisClient() throws InvalidPickupTimeException {
        cod.choosePickupTime(client.getCart(), store, LocalTime.parse("10:00"));
        cod.getOrders().add(new Order("1", client, new Cook(1), store));
    }

    @Then("the order has status {string}")
    public void thenTheOrderHasStatus(String status) {
        assert cod.getOrders().get(0).getStatus().toString().equals(status);
    }

    @When("the client cancels the order")
    public void whenTheClientCancelsTheOrder() throws OrderException, BadQuantityException {
        cod.cancelOrder(cod.getOrders().get(0));
    }

    @And("The order status is {string}")
    public void andTheOrderStatusIs(String status) {
        assert cod.getOrders().get(0).getStatus().toString().equals(status);
    }

    @When("the order status changes to {string}")
    public void whenTheOrderStatusChangesTo(String status) throws OrderException {
        StatusScheduler.getInstance().setStatus(cod.getOrders().get(0), OrderStatus.valueOf(status));
    }

    @Then("the client is notified")
    public void thenTheClientIsNotified() {
        verify(smsService, times(1)).notifyClient(client.getPhoneNumber(), NotificationMessage.COMMAND_READY.getMessage());
    }

    @After
    public void restoreStreams()
    {
        System.setOut(System.out);

    }
}
