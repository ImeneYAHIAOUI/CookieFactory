package fr.unice.polytech.client;

import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisteredClientTest {

    RegisteredClient registeredClient;

    List<Order> pastOrders;

    Order order1;
    Order order2;
    Order order3;


    @BeforeEach
    public void setUp() throws InvalidPhoneNumberException {
        registeredClient = new RegisteredClient("1", "****", "0606060606");
        order1 = mock(Order.class);
        order2 = mock(Order.class);
        order3 = mock(Order.class);
        pastOrders = new ArrayList<>();
        pastOrders.add(order1);
        pastOrders.add(order2);
        pastOrders.add(order3);
        registeredClient.setPastOrders(pastOrders);
    }

    @Test
    public void testIsBanned() {
        Map history1 = mock(Map.class);
        Map history2 = mock(Map.class);
        Map history3 = mock(Map.class);
        List OrderList = List.of(order2, order1);

        when(history1.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis() - 5 * 1000 * 60));
        when(history2.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis() - 15 * 1000 * 60));
        when(history3.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis() - 16 * 1000 * 60));


        when(order1.getStatus()).thenReturn(OrderStatus.CANCELLED);
        when(order2.getStatus()).thenReturn(OrderStatus.CANCELLED);
        when(order3.getStatus()).thenReturn(OrderStatus.CANCELLED);


        when(order1.getHistory()).thenReturn(history1);
        when(order2.getHistory()).thenReturn(history2);
        when(order3.getHistory()).thenReturn(history3);

        registeredClient.setPastOrders(OrderList);

        assertFalse(registeredClient.isBanned());
    }

    @Test
    public void testIsBanned2() {
        Map history1 = mock(Map.class);
        Map history2 = mock(Map.class);
        Map history3 = mock(Map.class);
        List OrderList = List.of(order2, order1);

        when(history1.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis() - 5 * 1000 * 60));
        when(history2.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis() - 4 * 1000 * 60));
        when(history3.get(OrderStatus.CANCELLED)).thenReturn(new Date(System.currentTimeMillis() - 16 * 1000 * 60));


        when(order1.getStatus()).thenReturn(OrderStatus.CANCELLED);
        when(order2.getStatus()).thenReturn(OrderStatus.CANCELLED);
        when(order3.getStatus()).thenReturn(OrderStatus.CANCELLED);


        when(order1.getHistory()).thenReturn(history1);
        when(order2.getHistory()).thenReturn(history2);
        when(order3.getHistory()).thenReturn(history3);

        registeredClient.setPastOrders(OrderList);

        assertTrue(registeredClient.isBanned());

    }

    @Test
    public void testIsBanned3() {
        assertFalse(registeredClient.isBanned());
    }
}
