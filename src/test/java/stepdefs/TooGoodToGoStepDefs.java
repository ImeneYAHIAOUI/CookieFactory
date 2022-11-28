package stepdefs;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.UnregisteredClient;
import fr.unice.polytech.cod.COD;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.services.StatusScheduler;
import fr.unice.polytech.services.TooGoodToGo;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.StoreFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TooGoodToGoStepDefs {
    COD cod;
    Store store;
    Order order;
    Timer timer;
    ScheduledThreadPoolExecutor tooGoodToGoExecutor;
    ScheduledThreadPoolExecutor statusSchedulerExecutor;
    List<TimerTask> timerTasks;
    TooGoodToGo tooGoodToGo;
    Runnable tooGoodToGoRunnable;
    Queue<Runnable> statusSchedulerRunnables;
    Client client;
    Cook cook;

    private void setup() throws InvalidPhoneNumberException, CookieException {
        statusSchedulerRunnables = new ArrayBlockingQueue<>(3);
        cod = COD.getInstance();
        cod.initializationCod();
        cook = mock(Cook.class);
        client = new UnregisteredClient("0123456789");
        client.getCart().setPickupTime(LocalTime.of(12, 0).atDate(LocalDate.now(COD.getCLOCK())));
        client.getCart().setTax(0.2);
        client.getCart().addItem(new Item(2, cod.getRecipes().get(0)));
        store = StoreFactory.createStore(
                List.of(cook),
                List.of(),
                "address",
                LocalTime.of(8, 0),
                LocalTime.of(20, 0),
                0,
                new Inventory(List.of()),
                0.1,
                List.of()
        );
        timer = mock(Timer.class);
        doAnswer(invocation -> {
            timerTasks.add(invocation.getArgument(0, TimerTask.class));
            return null;
        }).when(timer).schedule(any(TimerTask.class), anyLong());

        tooGoodToGoExecutor = mock(ScheduledThreadPoolExecutor.class);
        when(tooGoodToGoExecutor.getQueue()).thenReturn(new ArrayBlockingQueue<>(1));
        when(tooGoodToGoExecutor.scheduleAtFixedRate(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class))).thenAnswer(invocation -> {
            tooGoodToGoRunnable = invocation.getArgument(0, Runnable.class);
            return null;
        });

        statusSchedulerExecutor = mock(ScheduledThreadPoolExecutor.class);
        when(statusSchedulerExecutor.getQueue()).thenReturn(new ArrayBlockingQueue<>(10));
        when(statusSchedulerExecutor.schedule(any(Runnable.class), anyLong(), any(TimeUnit.class))).thenAnswer(invocation -> {
            statusSchedulerRunnables.add(invocation.getArgument(0, Runnable.class));
            return null;
        });

        tooGoodToGo = spy(new TooGoodToGo( tooGoodToGoExecutor));
        tooGoodToGo.initThreadPool(store);
    }

    @Given("a cod with a store having an order in progress")
    public void aCodWithAStoreHavingAnOrderInProgress() throws OrderException, InvalidPhoneNumberException, CookieException {
        setup();
        order = new Order("0", client, cook, store);
        order.setStatus(OrderStatus.IN_PROGRESS);
        cod.addOrder(order);
    }

    @When("the order becomes ready")
    public void theOrderIsReady() throws OrderException {
        StatusScheduler.getInstance(statusSchedulerExecutor).setStatus(order, OrderStatus.READY);
    }

    @And("the order is not picked up after 2 hours")
    public void theOrderIsNotPickedUpAfter2Hours() throws InterruptedException {
        while (!statusSchedulerRunnables.isEmpty()) {
            Thread thread = new Thread(statusSchedulerRunnables.poll());
            thread.start();
            thread.join();
        }
    }

    @Then("the order is obsolete")
    public void theOrderIsObsolete() {
        assertEquals(OrderStatus.OBSOLETE, order.getStatus());
    }

    @And("a toogoodtogo bag is created")
    public void aToogoodtogoBagIsCreated() throws InterruptedException {
        Thread thread = new Thread(tooGoodToGoRunnable);
        thread.start();
        thread.join();
        verify(tooGoodToGo).convertOrder(store,order);
        assertTrue(tooGoodToGo.getBagCount() > 0);
    }

    @Given("a cod with a store having no orders in progress")
    public void aCodWithAStoreHavingNoOrdersInProgress() throws InvalidPhoneNumberException, CookieException {
        setup();
    }

    @When("the toogoodtogo check is performed")
    public void theToogoodtogoCheckIsPerformed() throws InterruptedException {
        Thread thread = new Thread(tooGoodToGoRunnable);
        thread.start();
        thread.join();
    }

    @Then("no bag is created")
    public void noBagIsCreated() {
        assertEquals(0, tooGoodToGo.getBagCount());
    }

    @Given("a cod with a store having {int} obsolete orders and {int} {string}")
    public void aCodWithAStoreHavingObsoleteOrdersAndInProgress(int nbObsolete, int nbOrders2, String status2) throws InvalidPhoneNumberException, OrderException, CookieException {
        setup();
        for (int i = 0; i < nbObsolete; i++) {
            Order order = new Order(String.valueOf(i), client, cook, store);
            order.setStatus(OrderStatus.OBSOLETE);
            cod.addOrder(order);
        }
        for (int i = 0; i < nbOrders2; i++) {
            Order order = new Order(String.valueOf(i), client, cook, store);
            order.setStatus(OrderStatus.valueOf(status2));
            cod.addOrder(order);
        }
    }

    @Then("the {int} obsolete orders are converted")
    public void theOrdersAreConverted(int nbOrders) {
        verify(tooGoodToGo, times(nbOrders)).convertOrder(eq(store),any(Order.class));
    }

    @Given("a cod with no registered client")
    public void setupClients(){
        COD.reset();
        cod = COD.getInstance();
    }

    @When("Register a client")
    public void register() throws RegistrationException, InvalidPhoneNumberException {
        cod.register("id", "password", "0612345678");
        client = cod.getClients().get(0);
    }
    @When("Client wants to be notified")
    public void notified() throws ClientException {
        cod.addClientToGoodToGo(client, "flo@gmail.com", new ArrayList<>(),null);
    }
    @Then("No clients in the cod")
    public void clientEmpty(){
        assertTrue(cod.getClients().isEmpty());
    }

    @Then("Client in the cod")
    public void clientNotEmpty(){
        assertTrue(cod.getClients().contains(client));
    }

    @Then("No clients to notified for TooGoodToGo in the cod")
    public void clientToGoodToGoEmpty(){
        assertTrue(cod.getTooGooToGoClients().isEmpty());
    }

    @Then("Client in the list to be notified for TooGoodToGo in the cod")
    public void clientToGoodToGoNotEmpty(){
        assertTrue(cod.getTooGooToGoClients().contains(client));
    }
}
