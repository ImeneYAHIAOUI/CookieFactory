package unitTests;

import fr.unice.polytech.COD;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.RegisteredClient;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.exception.InvalidInputException;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.exception.RegistrationException;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.recipe.*;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.Store;
import io.cucumber.java.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CODUnitTests {
    
    COD cod;
    Order order;

    Client client;

    Cook cook;

    Store store;


    @BeforeEach
    public void setUp() {
        cod = new COD();
        client = new UnregisteredClient(0606060606);
        try{

            cod.register("15","^mldp",0707060106);
        }catch(RegistrationException exp){

        }
        cook = new Cook(1);
        order = new Order("1",client,cook);

        store = new Store(
                List.of(cook),
                List.of(new Cookie("chocolala",1.,15., Cooking.CHEWY,Mix.MIXED,new Dough("chocolate",1),new Flavour("chocolate",1),List.of(new Topping("chocolate chips",1)))),
                "30 Rte des Colles, 06410 Biot",
                LocalTime.parse("08:00"),
                LocalTime.parse("20:00"),
                1,
                new Inventory(new ArrayList<>())
        );
    }

    @Test
    public void testInitialstatus() {
        assertEquals(order.getStatus(), OrderStatus.NOT_STARTED);
    }

    @Test
    public void testSetStatus1() {
        assertDoesNotThrow(() ->cod.setStatus(order, OrderStatus.PAYED));
        assertEquals(order.getStatus(), OrderStatus.PAYED);
        assertThrows(OrderException.class,() ->cod.setStatus(order, OrderStatus.PAYED));
        assertDoesNotThrow(() ->cod.setStatus(order, OrderStatus.IN_PROGRESS));
        assertEquals(order.getStatus(), OrderStatus.IN_PROGRESS);
        assertThrows(OrderException.class,() ->cod.setStatus(order, OrderStatus.CANCELLED));
    }

    @Test
    public void testSetStatus2() {
        assertDoesNotThrow(() ->cod.setStatus(order, OrderStatus.CANCELLED));
        assertEquals(order.getStatus(), OrderStatus.CANCELLED);
        assertThrows(OrderException.class,() ->cod.setStatus(order, OrderStatus.PAYED));
    }
    @Test
    public void testLogIn() throws InvalidInputException {
        cod.logIn("15" ,"^mldp");
        assertEquals("15", cod.getConnectedClients().get(0).getId());
        assertEquals("^mldp", cod.getConnectedClients().get(0).getPassword());
        assertThrows(InvalidInputException.class, ()-> cod.logIn("15" ,"^mldp"),
               "Your are already connected ");
        assertThrows(InvalidInputException.class, ()-> cod.logIn("10" ,"^mldp"),
                "ID not found. Please log in with another ID");
        assertThrows(InvalidInputException.class, ()-> cod.logIn("15" ,"^mdp"),
                "The password you entered is not valid. ");
    }

}
