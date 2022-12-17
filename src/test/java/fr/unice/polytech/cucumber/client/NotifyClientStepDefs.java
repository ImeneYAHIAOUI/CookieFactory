package fr.unice.polytech.cucumber.client;

import fr.unice.polytech.components.OrderManager;
import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.NotificationMessage;
import fr.unice.polytech.entities.client.UnregisteredClient;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.StoreFactory;
import fr.unice.polytech.exception.OrderStateException;
import fr.unice.polytech.interfaces.AgendaProcessor;
import fr.unice.polytech.interfaces.NotificationHandler;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
public class NotifyClientStepDefs {

    @Autowired
    OrderManager orderManager;
    @Autowired
    private AgendaProcessor agendaProcessor;
    @Autowired
    private NotificationHandler notificationHandler;
    private Client client;
    @Spy
    private Order order;

    @Given("a client with an order ready for pickup")
    public void setUp() throws OrderStateException {
        client = new UnregisteredClient("0606060606");
        LocalTime openingTime = LocalTime.of(10, 0);
        LocalTime closingTime = LocalTime.of(20, 0);
        LocalTime pickupTime = LocalTime.of(12, 0);
        client.getCart().setPickupTime(pickupTime.atDate(LocalDate.now(agendaProcessor.getClock())));
        UUID id = UUID.randomUUID();
        order = new Order(
                id,
                client,
                new Cook(),
                StoreFactory.createStore(
                        new ArrayList<>(),
                        "address",
                        openingTime,
                        closingTime,
                        new Inventory(new ArrayList<>()),
                        0.2,
                        new ArrayList<>()
                ),
                Duration.ofMinutes(20)
        );
        orderManager.setStatus(order, OrderStatus.IN_PROGRESS);
        orderManager.setStatus(order, OrderStatus.READY);
    }

    @Given("a client")
    public void aClientWithPhoneNumber() {
        client = new UnregisteredClient("0606060606");
    }

    @And("the order is ready")
    public void anOrderIsReady() throws OrderStateException {
        orderManager.setStatus(order, OrderStatus.READY);
    }

    @Then("the client gets notified and doesn't receive more notifications")
    public void theClientShouldBeNotified() {
        verify(notificationHandler, times(1)).notifyClient(order, NotificationMessage.COMMAND_READY);
        assertEquals(OrderStatus.COMPLETED, order.getState().getOrderStatus());
    }


    @When("the client never picks up the order")
    public void theClientDoesntPickUpTheOrder() throws OrderStateException {
        orderManager.setStatus(order, OrderStatus.OBSOLETE);
    }

    @Then("the client gets notified and the order is obsolete")
    public void theClientGetsNotifiedAndTheOrderIsObsolete() {
        assertEquals(OrderStatus.OBSOLETE, order.getState().getOrderStatus());
    }

    @When("the client picks up the order")
    public void theClientPicksUpOrderOneHourAfter() throws OrderStateException {
        order.setState(OrderStatus.COMPLETED);
    }
}
