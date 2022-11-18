package fr.unice.polytech.services;

import fr.unice.polytech.client.Cart;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatusSchedulerTest {
    Order order;
    Client client;
    Cook cook;
    StatusScheduler statusScheduler;

    @BeforeEach
    public void setUp() {
        client = mock(Client.class);
        cook = mock(Cook.class);
        Cart cart = new Cart();
        cart.setPickupTime(LocalTime.now());
        when(client.getCart()).thenReturn(cart);
        Store store = mock(Store.class);
        order = new Order("1", client, cook, store);
        statusScheduler = StatusScheduler.getInstance();
        statusScheduler.emptySchedulerQueue();
    }

    @Test
    public void testSetStatus() throws OrderException {
        assertTrue(order.getHistory().containsKey(OrderStatus.NOT_STARTED));
        assertEquals(order.getStatus(), OrderStatus.NOT_STARTED);
        statusScheduler.setStatus(order, OrderStatus.PAYED);
        assertTrue(order.getHistory().containsKey(OrderStatus.PAYED));
        assertEquals(order.getStatus(), OrderStatus.PAYED);
        assertThrows(OrderException.class, () -> statusScheduler.setStatus(order, OrderStatus.PAYED));
        statusScheduler.setStatus(order, OrderStatus.CANCELLED);
        assertEquals(order.getStatus(), OrderStatus.CANCELLED);
        assertTrue(order.getHistory().containsKey(OrderStatus.PAYED));
        assertThrows(OrderException.class, () -> statusScheduler.setStatus(order, OrderStatus.IN_PROGRESS));
        assertFalse(order.getHistory().containsKey(OrderStatus.IN_PROGRESS));
    }

    @Test
    public void testSetStatusCancelOrder() throws OrderException {
        statusScheduler.setStatus(order, OrderStatus.IN_PROGRESS);
        assertThrows(OrderException.class, () -> statusScheduler.setStatus(order, OrderStatus.CANCELLED));
        assertFalse(order.getHistory().containsKey(OrderStatus.CANCELLED));
    }

    @Test
    public void testSetStatusCancelOrder2() throws OrderException {
        statusScheduler.setStatus(order, OrderStatus.CANCELLED);
        assertEquals(order.getStatus(), OrderStatus.CANCELLED);
        assertTrue(order.getHistory().containsKey(OrderStatus.CANCELLED));
    }
}
