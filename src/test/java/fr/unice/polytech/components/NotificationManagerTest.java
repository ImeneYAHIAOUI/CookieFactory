package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.NotificationMessage;
import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.client.UnregisteredClient;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.exception.OrderStateException;
import fr.unice.polytech.interfaces.NotificationHandler;
import fr.unice.polytech.interfaces.OrderFinder;
import fr.unice.polytech.interfaces.OrderUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest
public class NotificationManagerTest {
    @SpyBean
    NotificationHandler notificationHandler;
    @Autowired
    OrderUpdater orderUpdater;
    Order order;
    Client client;

    @BeforeEach
    void setUp() {
        client = new UnregisteredClient("0606060606");
        client.getCart().setPickupTime(LocalDateTime.now());
        order = new Order(UUID.randomUUID(), client, new Cook(), null, Duration.ofMinutes(30));
    }

    @Test
    void testNotifyClient() throws OrderStateException, OrderException {
        orderUpdater.setStatus(order, OrderStatus.READY);
        verify(notificationHandler, times(1)).notifyClient(order, NotificationMessage.COMMAND_READY);
    }

}
