package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderCancelled;
import fr.unice.polytech.entities.order.OrderNotStarted;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.StoreFactory;
import fr.unice.polytech.exception.InvalidInputException;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.exception.RegistrationException;
import fr.unice.polytech.interfaces.AgendaProcessor;
import fr.unice.polytech.interfaces.ClientFinder;
import fr.unice.polytech.interfaces.ClientHandler;
import fr.unice.polytech.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ClientManagerTest {

    private final String mail = "sourour.gazzeh@outlook.fr";
    private final String mdp = "mdp";
    private final String phoneNumber = "+33 6-67-99-58-95";
    Cook cook;
    Store store;
    @Autowired
    AgendaProcessor agendaProcessor;
    RegisteredClient registeredClient;
    List<Order> pastOrders;
    Order order1;
    Order order2;
    Order order3;
    @Autowired
    ClientFinder clientFinder;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientHandler client;

    @Test
    public void unknownCustomer() {
        clientRepository.deleteAll();
        Optional<Client> client = StreamSupport
                .stream(clientRepository.findAll().spliterator(), false)
                .filter(cust -> mail.equals(((RegisteredClient) cust).getMail()))
                .findAny();
        System.out.println(StreamSupport.stream(clientRepository.findAll().spliterator(), false).count());
        assertEquals(0, StreamSupport.stream(clientRepository.findAll().spliterator(), false).count());
        assertFalse(client.isPresent());
    }


    @Test
    public void registerCustomer() {
        assertDoesNotThrow(() -> client.register(mail, mdp, phoneNumber));
        Optional<Client> cl = StreamSupport.stream(clientRepository.findAll().spliterator(), false)
                                           .filter(cust -> mail.equals(((RegisteredClient) cust).getMail())).findAny();
        assertTrue(cl.isPresent());
        registeredClient = (RegisteredClient) cl.get();
        assertEquals(phoneNumber, registeredClient.getPhoneNumber());
        assertTrue(registeredClient.isRegistered());
        assertThrows(RegistrationException.class, () -> client.register(mail, mdp, phoneNumber));
    }

    @Test
    public void checkPhoneNumberWithValidNumber() {
        assertDoesNotThrow(() -> {
            client.checkPhoneNumber("0606060606");
        });
        assertDoesNotThrow(() -> {
            client.checkPhoneNumber("06 06 06 06 06");
        });
        assertDoesNotThrow(() -> {
            client.checkPhoneNumber("06-06-06-06-06");
        });
        assertDoesNotThrow(() -> {
            client.checkPhoneNumber("+33606060606");
        });
        assertDoesNotThrow(() -> {
            client.checkPhoneNumber("+33 6 06 06 06 06");
        });
        assertDoesNotThrow(() -> {
            client.checkPhoneNumber("+33 6-06-06-06-06");
        });
    }

    @Test
    public void checkPhoneNumberWithInValidNumber() {
        assertThrows(InvalidPhoneNumberException.class, () ->
                client.checkPhoneNumber("06060606060")
        );
        assertThrows(InvalidPhoneNumberException.class, () ->
                client.checkPhoneNumber("060606060")
        );
        assertThrows(InvalidPhoneNumberException.class, () ->
                client.checkPhoneNumber("060606060a")
        );
        assertThrows(InvalidPhoneNumberException.class, () ->
                client.checkPhoneNumber("060606060*")
        );
        assertThrows(InvalidPhoneNumberException.class, () ->
                client.checkPhoneNumber("")
        );
        assertThrows(InvalidPhoneNumberException.class, () ->
                client.checkPhoneNumber(null)
        );
        assertThrows(InvalidPhoneNumberException.class, () ->
                client.checkPhoneNumber("  ")
        );
        assertThrows(InvalidPhoneNumberException.class, () ->
                client.checkPhoneNumber("+33 ")
        );
        assertThrows(InvalidPhoneNumberException.class, () ->
                client.checkPhoneNumber("+33")
        );
        assertThrows(InvalidPhoneNumberException.class, () ->
                client.checkPhoneNumber("+33--")
        );
        assertThrows(InvalidPhoneNumberException.class, () ->
                client.checkPhoneNumber("+3360606060")
        );
        assertThrows(InvalidPhoneNumberException.class, () ->
                client.checkPhoneNumber("+336060606060")
        );
        assertThrows(InvalidPhoneNumberException.class, () ->
                client.checkPhoneNumber("+3360606060*")
        );
    }

    @BeforeEach
    public void setup() {
        registeredClient = new RegisteredClient("****", "0606060606", mail);
        order1 = mock(Order.class);
        order2 = mock(Order.class);
        order3 = mock(Order.class);
        when(order1.getState()).thenReturn(new OrderNotStarted(order1));
        when(order2.getState()).thenReturn(new OrderNotStarted(order2));
        when(order3.getState()).thenReturn(new OrderNotStarted(order3));
        pastOrders = new ArrayList<>();
        pastOrders.add(order1);
        pastOrders.add(order2);
        pastOrders.add(order3);
        registeredClient.setPastOrders(pastOrders);
        cook = new Cook();

        store = StoreFactory.createStore(
                List.of(cook),
                "30 Rte des Colles, 06410 Biot",
                LocalTime.parse("08:00"),
                LocalTime.parse("20:00"),
                new Inventory(new ArrayList<>()), 4.2, new ArrayList<>()
        );
        registeredClient
                .getCart()
                .setPickupTime(LocalTime.parse("10:00").atDate(LocalDate.now(agendaProcessor.getClock())));
    }

    @Test
    public void testIsBanned() {
        Map<OrderStatus, Date> history1 = new HashMap<>();
        Map<OrderStatus, Date> history2 = new HashMap<>();
        Map<OrderStatus, Date> history3 = new HashMap<>();
        List<Order> OrderList = List.of(order2, order1);

        history1.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis() - 5 * 1000 * 60));
        history2.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis() - 15 * 1000 * 60));
        history3.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis() - 16 * 1000 * 60));

        when(order1.getState()).thenReturn(new OrderCancelled(order1));
        when(order2.getState()).thenReturn(new OrderCancelled(order2));
        when(order3.getState()).thenReturn(new OrderCancelled(order3));
        when(order1.getHistory()).thenReturn(history1);
        when(order2.getHistory()).thenReturn(history2);
        when(order3.getHistory()).thenReturn(history3);
        registeredClient.setPastOrders(OrderList);
        assertFalse(client.isBanned(registeredClient));
    }

    @Test
    public void testIsBanned2() {
        Map<OrderStatus, Date> history1 = new HashMap<>();
        Map<OrderStatus, Date> history2 = new HashMap<>();
        Map<OrderStatus, Date> history3 = new HashMap<>();
        List<Order> OrderList = List.of(order2, order1);

        history1.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis() - 5 * 1000 * 60));
        history2.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis() - 4 * 1000 * 60));
        history3.put(OrderStatus.CANCELLED, new Date(System.currentTimeMillis() - 16 * 1000 * 60));

        when(order1.getState()).thenReturn(new OrderCancelled(order1));
        when(order2.getState()).thenReturn(new OrderCancelled(order2));
        when(order3.getState()).thenReturn(new OrderCancelled(order3));


        when(order1.getHistory()).thenReturn(history1);
        when(order2.getHistory()).thenReturn(history2);
        when(order3.getHistory()).thenReturn(history3);

        registeredClient.setPastOrders(OrderList);

        assertTrue(client.isBanned(registeredClient));
    }

    @Test
    public void testIsBanned3() {
        assertFalse(client.isBanned(registeredClient));
    }

    @Test
    public void testGetClientPastOrders() {
        Order order2 = new Order(UUID.randomUUID(), registeredClient, cook, store, Duration.ofMinutes(20));
        Order order3 = new Order(UUID.randomUUID(), registeredClient, cook, store, Duration.ofMinutes(20));
        List<Order> orders = new ArrayList<>();
        orders.add(order2);
        orders.add(order3);
        RegisteredClient registeredClient = new RegisteredClient("0606060606", "^mldp", "0707060106");
        registeredClient.setPastOrders(orders);
        assertEquals(clientFinder.getClientPastOrders(registeredClient), orders);

    }

    @Test
    public void testLogIn() throws InvalidInputException, RegistrationException, InvalidPhoneNumberException {
        client.register("15", "^mldp", "0606060606");
        client.logIn("15", "^mldp");
        assertEquals("15", clientFinder.getConnectedClients().get(0).getMail());
        assertEquals("^mldp", clientFinder.getConnectedClients().get(0).getPassword());
        assertThrows(InvalidInputException.class, () -> client.logIn("15", "^mldp"),
                     "You are already connected "
        );
        assertThrows(InvalidInputException.class, () -> client.logIn("10", "^mldp"),
                     "ID not found. Please log in with another ID"
        );
        assertThrows(InvalidInputException.class, () -> client.logIn("15", "^mdp"),
                     "The password you entered is not valid. "
        );
    }

}
