package stepdefs;

import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.cod.COD;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.services.StatusScheduler;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.StoreFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Spy;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


public class NotifyClientStepDefs {

    @Spy
    ScheduledThreadPoolExecutor mockedExecutor = spy(new ScheduledThreadPoolExecutor(1));
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    Client client;
    @Spy
    Order order;
    COD cod;

    @Given("a client with phone number {string}")
    public void aClientWithPhoneNumber(String phoneNumber) throws InvalidPhoneNumberException {
        cod = COD.getInstance();
        client = new UnregisteredClient(phoneNumber);
    }

    @And("the order is ready")
    public void anOrderIsReady() throws OrderException {
        LocalTime openingTime = LocalTime.of(10, 0);
        LocalTime closingTime = LocalTime.of(20, 0);
        LocalTime pickupTime = LocalTime.of(12, 0);
        client.getCart().setPickupTime(pickupTime);
        Order order2 = new Order("1", client, new Cook(1), StoreFactory.createStore(new ArrayList<>(), new ArrayList<>(), "address", openingTime, closingTime, 1, new Inventory(new ArrayList<>()), 0.2, new ArrayList<>()));
        order = spy(order2);
        doAnswer(invocation -> {
            when(order.getStatus()).thenReturn(invocation.getArgument(0));
            return null;
        }).when(order).setStatus(any(OrderStatus.class));

        doAnswer(invocation -> {
            executor.execute(invocation.getArgument(0));
            return null;
        }).when(mockedExecutor).schedule(any(Runnable.class), anyLong(),any(TimeUnit.class));

    }

    @Then("the client gets notified and doesn't receive more notifications")
    public void theClientShouldBeNotified() throws InterruptedException {
        StatusScheduler.getInstance(mockedExecutor).statusSchedulerTask(order);
        verify(mockedExecutor, times(1)).schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
        Thread.sleep(10);
        assertEquals(OrderStatus.COMPLETED,order.getStatus());
    }


    @When("the client never picks up the order")
    public void theClientDoesntPickUpTheOrder() {
        when(order.getStatus()).thenReturn(OrderStatus.READY);
    }

    @Then("the client gets notified and the order is obsolete")
    public void theClientGetsNotifiedAndTheOrderIsObsolete() throws InterruptedException {
        StatusScheduler.getInstance(mockedExecutor).statusSchedulerTask(order);
        Thread.sleep(10);
        assertEquals(OrderStatus.OBSOLETE, order.getStatus());
    }

    @When("the client picks up the order")
    public void theClientPicksUpOrderOneHourAfter() {
        when(order.getStatus()).thenReturn(OrderStatus.COMPLETED);
    }
}
