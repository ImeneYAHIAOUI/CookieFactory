package fr.unice.polytech.cucumber.client;

import fr.unice.polytech.components.InventoryManager;
import fr.unice.polytech.components.TooGoodToGoManager;
import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.UnregisteredClient;
import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.StoreFactory;
import fr.unice.polytech.entities.tooGoodToGo.TooGoodToGoBag;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.interfaces.*;
import fr.unice.polytech.repositories.ClientRepository;
import fr.unice.polytech.repositories.OrderRepository;
import fr.unice.polytech.repositories.TooGoodToGoBagsRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TooGoodToGoStepDefs {

    Store store;
    Order order;
    Timer timer;
    ScheduledThreadPoolExecutor tooGoodToGoExecutor;
    ScheduledThreadPoolExecutor statusSchedulerExecutor;
    List<TimerTask> timerTasks;
    Runnable tooGoodToGoRunnable;
    Queue<Runnable> statusSchedulerRunnables;
    Client client;
    Cook cook;
    @Autowired
    CartHandler cartManager;
    @Autowired
    ClientHandler clientHandler;
    @Autowired
    AgendaProcessor agenda;
    @Autowired
    CookieChoice cookieChoice;
    @Autowired
    OrderUpdater orderUpdater;
    @Autowired
    TooGoodToGoBagsRepository tooGoodToGoBagsRepository;
    @Autowired
    TooGoodToGoManager tooGoodToGo;
    @Autowired
    ClientFinder clientFinder;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientHandler clientManager;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    InventoryManager inventoryManager;
    @Autowired
    StoreProcessor storeProcessor;
    @Autowired
    CookieFinder cookieFinder;

    private int nbCookiesObsoleteOrders;

    private void setup() throws CookieException {
        statusSchedulerExecutor = mock(ScheduledThreadPoolExecutor.class);
        statusSchedulerRunnables = new ArrayBlockingQueue<>(3);
        cook = mock(Cook.class);
        client = new UnregisteredClient("0123456789");
        client.getCart().setPickupTime(LocalTime.of(12, 0).atDate(LocalDate.now(agenda.getClock())));
        client.getCart().setTax(0.2);
        cartManager.addItem(
                new Item(2, new SimpleCookieBuilder()
                        .setName("chocolala")
                        .setPrice(1.)
                        .setCookingTime(2)
                        .setCooking(Cooking.CHEWY)
                        .setMix(Mix.MIXED)
                        .setDough(new Dough(UUID.randomUUID(), "chocolate", 1.))
                        .setFlavour(new Flavour(UUID.randomUUID(), "chocolate", 1.))
                        .build()),
                client.getCart()
        );
        store = StoreFactory.createStore(
                List.of(cook),
                "address",
                LocalTime.of(8, 0),
                LocalTime.of(20, 0),
                new Inventory(List.of()),
                0.1,
                List.of()
        );
        timer = mock(Timer.class);
        doAnswer(invocation -> {
            timerTasks.add(invocation.getArgument(0, TimerTask.class));
            return null;
        }).when(timer).schedule(any(TimerTask.class), anyLong());

        tooGoodToGoExecutor = tooGoodToGo.getExecutor();
        when(tooGoodToGoExecutor.getQueue()).thenReturn(new ArrayBlockingQueue<>(1));
        when(tooGoodToGoExecutor.scheduleAtFixedRate(
                any(Runnable.class),
                anyLong(),
                anyLong(),
                any(TimeUnit.class)
        )).thenAnswer(invocation -> {
            tooGoodToGoRunnable = invocation.getArgument(0, Runnable.class);
            return null;
        });

        when(statusSchedulerExecutor.getQueue()).thenReturn(new ArrayBlockingQueue<>(10));
        when(statusSchedulerExecutor.schedule(any(Runnable.class), anyLong(), any(TimeUnit.class))).thenAnswer(
                invocation -> {
                    statusSchedulerRunnables.add(invocation.getArgument(0, Runnable.class));
                    return null;
                });

        tooGoodToGo.initScheduler(store);
        nbCookiesObsoleteOrders = 0;
        orderRepository.deleteAll();
        tooGoodToGoBagsRepository.deleteAll();
    }

    @Given("a cod with a store having an order in progress")
    public void aCodWithAStoreHavingAnOrderInProgress() throws CookieException, OrderStateException {
        setup();
        UUID id=UUID.randomUUID();

        order =  new Order(id,client, cook,store, Duration.ofMillis(0));
        order.setState(OrderStatus.IN_PROGRESS);
        orderUpdater.addOrder(order);
    }

    @When("the order becomes ready")
    public void theOrderIsReady() throws OrderException, OrderStateException {
        orderUpdater.setStatus(order, OrderStatus.READY);
    }

    @And("the order is not picked up after 2 hours")
    public void theOrderIsNotPickedUpAfter2Hours() throws InterruptedException {
        while (!statusSchedulerRunnables.isEmpty()) {
            Thread thread = new Thread(statusSchedulerRunnables.poll());
            thread.start();
            thread.join();
        }
    }

    @When("the order is obsolete")
    public void theOrderIsObsolete() throws OrderStateException, OrderException {
        orderUpdater.setStatus(order, OrderStatus.READY);
        orderUpdater.setStatus(order, OrderStatus.OBSOLETE);
    }

    @And("a toogoodtogo bag is created")
    public void aToogoodtogoBagIsCreated() throws InterruptedException {
        Thread thread = new Thread(tooGoodToGoRunnable);
        thread.start();
        thread.join();
        int nbBags = 0;
        for (TooGoodToGoBag bag : tooGoodToGoBagsRepository.findAll()) {
            if (bag.getStore().equals(store)) {
                nbBags++;
            }
        }
        assertTrue(nbBags >= 1);
    }

    @Given("a cod with a store having no orders in progress")
    public void aCodWithAStoreHavingNoOrdersInProgress() throws CookieException {
        setup();
        tooGoodToGoBagsRepository.deleteAll();

    }

    @When("the toogoodtogo check is performed")
    public void theToogoodtogoCheckIsPerformed() {
        tooGoodToGo.performOrderRecycling(store);
    }

    @Then("no bag is created")
    public void noBagIsCreated() {
        assertEquals(0, tooGoodToGoBagsRepository.count() );
    }

    @Given("a cod with a store having {int} obsolete orders and {int} {string}")
    public void aCodWithAStoreHavingObsoleteOrdersAndInProgress(int nbObsolete, int nbOrders2, String status2)
            throws CookieException, OrderStateException, OrderException {
        setup();
        for (int i = 0; i < nbObsolete; i++) {
            UUID id = UUID.randomUUID();

            Order order = new Order(id, client, cook, store, Duration.ofMillis(0));
            orderUpdater.addOrder(order);
            orderUpdater.setStatus(order, OrderStatus.IN_PROGRESS);
            orderUpdater.setStatus(order, OrderStatus.READY);
            orderUpdater.setStatus(order, OrderStatus.OBSOLETE);
            nbCookiesObsoleteOrders += order
                    .getItems()
                    .stream()
                    .reduce(0, (acc, item) -> acc + item.getQuantity(), Integer::sum);
        }
        for (int i = 0; i < nbOrders2; i++) {
            UUID id=UUID.randomUUID();

            Order order = new Order(id,client, cook, store,Duration.ofMinutes(15));
            if (OrderStatus.valueOf(status2) == OrderStatus.READY)
                order.setState(OrderStatus.valueOf(status2));
            orderUpdater.addOrder(order);
        }
    }

    @Given("There is no clients in the cod")
    public void noClientsInCOD(){
        clientRepository.deleteAll();
    }

    @Then("the {int} obsolete orders are converted")
    public void theOrdersAreConverted(int nbOrders) {
        int nbCookies = 0;
        for (TooGoodToGoBag bag : tooGoodToGoBagsRepository.findAll()) {
            nbCookies += bag.getCookies().size();
        }
        assertEquals(nbCookiesObsoleteOrders, nbCookies);
    }

    @When("Register a client")
    public void register() throws RegistrationException, InvalidPhoneNumberException {
        clientHandler.register("id", "password", "0612345678");
        client = clientFinder.getRegisteredClients().get(0);
    }
    @When("Client wants to be notified")
    public void notified() throws ClientException {
        clientHandler.addToGoodToGo(client, "flo@gmail.com", new ArrayList<>(), null);
    }
    @Then("No clients in the cod")
    public void clientEmpty(){
        assertTrue(clientFinder.getRegisteredClients().isEmpty());
    }

    @Then("Client in the cod")
    public void clientNotEmpty(){
        assertTrue(clientFinder.getRegisteredClients().contains(client));
    }

    @Then("No clients to notified for TooGoodToGo in the cod")
    public void clientToGoodToGoEmpty(){
        assertTrue(clientFinder.getTooGoodToGoClients().isEmpty());
    }
    @And("There is no clients to notified for TooGoodToGo in the cod")
    public void noClientToGoodToGo(){
        clientManager.clearTooGoodToGoClients();
    }

    @Then("Client in the list to be notified for TooGoodToGo in the cod")
    public void clientToGoodToGoNotEmpty(){
        assertTrue(clientFinder.getTooGoodToGoClients().contains(client));
    }
}
