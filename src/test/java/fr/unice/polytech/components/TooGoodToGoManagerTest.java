package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Cart;
import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.exception.OrderStateException;
import fr.unice.polytech.interfaces.CookieChoice;
import fr.unice.polytech.interfaces.OrderUpdater;
import fr.unice.polytech.repositories.TooGoodToGoBagsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TooGoodToGoManagerTest {
    Order order;
    Client client;
    Cook cook;
    Store store;
    @Autowired
    TooGoodToGoBagsRepository tooGoodToGoBagsRepository;
    @Autowired
    OrderUpdater orderUpdater;
    @Autowired
    TooGoodToGoManager tooGoodToGoManager;

    @BeforeEach
    public void setup() {
        tooGoodToGoBagsRepository.deleteAll();
        client = mock(Client.class);
        Cart cart = new Cart();
        when(client.getCart()).thenReturn(cart);
        cart.setPickupTime(LocalDateTime.now(Clock.systemDefaultZone()));

        store = mock(Store.class);
        UUID uuid = UUID.randomUUID();
        order = new Order(uuid, client, cook, store, Duration.ofMinutes(15));
        orderUpdater.addOrder(order);
    }

    @Test
    public void convertOrderTest() throws OrderException, OrderStateException {
        assertEquals(0, tooGoodToGoBagsRepository.count());
        orderUpdater.setStatus(order, OrderStatus.IN_PROGRESS);
        orderUpdater.setStatus(order, OrderStatus.READY);
        orderUpdater.setStatus(order, OrderStatus.OBSOLETE);
        tooGoodToGoManager.convertOrders(List.of(order));
        assertEquals(1, tooGoodToGoBagsRepository.count());
      //  assertTrue(tooGoodToGoBagsRepository.existsById(order.getId())); <-Ne peut pas marcher car les bags ont un UUID random
    }

    @Test
    public void convertOrderTest1() {
        assertEquals(0, tooGoodToGoBagsRepository.count());
        tooGoodToGoManager.convertOrders(null);
        assertEquals(0, tooGoodToGoBagsRepository.count());
    }

    @Test
    public void convertOrderTest2() throws OrderStateException {
        assertEquals(0, tooGoodToGoBagsRepository.count());
        order.setState(OrderStatus.IN_PROGRESS);
        order.setState(OrderStatus.READY);
        order.setState(OrderStatus.OBSOLETE);
        tooGoodToGoManager.convertOrders(List.of(order));
        assertEquals(1, tooGoodToGoBagsRepository.count());
    }

    @Test
    public void checkObsoleteOrdersTest() throws OrderException, OrderStateException {
        assertEquals(0, tooGoodToGoBagsRepository.count());
        order.setState(OrderStatus.IN_PROGRESS);
        order.setState(OrderStatus.READY);
        orderUpdater.setStatus(order, OrderStatus.OBSOLETE);
        Collection<Order> orders = tooGoodToGoManager.checkForObsoleteOrders();
        tooGoodToGoManager.convertOrders(orders);
        assertEquals(1, tooGoodToGoBagsRepository.count());
    }

    @Test
    public void checkObsoleteOrdersTest1() {
        assertEquals(0, tooGoodToGoBagsRepository.count());
        tooGoodToGoManager.checkForObsoleteOrders();
        assertEquals(0, tooGoodToGoBagsRepository.count());
    }

    @Test
    public void checkObsoleteOrdersTest2() {
        assertEquals(0, tooGoodToGoBagsRepository.count());
        tooGoodToGoManager.checkForObsoleteOrders();
        assertEquals(0, tooGoodToGoBagsRepository.count());
    }

    @Test
    public void getTooGoodToGoBagsTest() throws OrderException, OrderStateException {
        assertEquals(0, tooGoodToGoBagsRepository.count());
        order.setState(OrderStatus.IN_PROGRESS);
        order.setState(OrderStatus.READY);
        orderUpdater.setStatus(order, OrderStatus.OBSOLETE);
        Collection<Order> orders = tooGoodToGoManager.checkForObsoleteOrders();
        tooGoodToGoManager.convertOrders(orders);
        assertEquals(1, tooGoodToGoBagsRepository.count());
       // assertTrue(tooGoodToGoManager.getBags().contains(new TooGoodToGoBag(order)));
        //ne peut pas marcher car chaque bag a un UUID random non égal à l'ID de l'order
    }

    @Test
    public void getTooGoodToGoBagsTest1() {
        assertEquals(0, tooGoodToGoBagsRepository.count());
        tooGoodToGoManager.checkForObsoleteOrders();
        assertEquals(0, tooGoodToGoBagsRepository.count());
        assertEquals(0, tooGoodToGoManager.getBags(store).size());
    }

    @Test
    public void getTooGoodToGoBagsTest2() {
        assertEquals(0, tooGoodToGoBagsRepository.count());
        tooGoodToGoManager.checkForObsoleteOrders();
        assertEquals(0, tooGoodToGoBagsRepository.count());
        assertEquals(0, tooGoodToGoManager.getBags(store).size());
    }
}
