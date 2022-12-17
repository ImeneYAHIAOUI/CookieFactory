package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Cart;
import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.exception.OrderStateException;
import fr.unice.polytech.interfaces.*;
import fr.unice.polytech.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderManagerTest {
    Order order;
    Client client;
    RegisteredClient registeredClient;
    Cook cook;
    Store mockstore;
    Cookie cookie1;
    @Autowired
    private CookieChoice cookieChoice;
    @Autowired
    private OrderFinder orderFinder;
    @Autowired
    private OrderUpdater orderUpdater;
    @Autowired
    @MockBean
    private ClientHandler clientManager;
    @Autowired
    @MockBean
    private StoreProcessor storeManager;
    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() throws CookieException {
        orderRepository.deleteAll();
        client = mock(Client.class);
        cook = mock(Cook.class);
        Cart cart = new Cart();
        when(client.getCart()).thenReturn(cart);
        cart.setPickupTime(LocalDateTime.now(Clock.systemDefaultZone()));

        Store store = mock(Store.class);
        UUID uuid = UUID.randomUUID();
        order = new Order(uuid, client, cook, store, Duration.ofMinutes(15));


        cook = new Cook();

        mockstore = mock(Store.class);

        registeredClient = mock(RegisteredClient.class);

cookie1 = new SimpleCookieBuilder()
        .setName("chocolala")
        .setPrice(1.)
        .setCookingTime(15)
        .setCooking(Cooking.CHEWY)
        .setMix(Mix.MIXED)
        .setDough(new Dough(UUID.randomUUID(),"chocolate", 1))
        .setFlavour(new Flavour(UUID.randomUUID(),"chocolate", 1))
        .setToppings(List.of(new Topping(UUID.randomUUID(),"chocolat", 1.)))
        .build();

    }

    @Test
    public void testSetStatus() throws OrderException, OrderStateException {
        assertTrue(order.getHistory().containsKey(OrderStatus.NOT_STARTED));
        assertEquals(order.getState().getOrderStatus(), OrderStatus.NOT_STARTED);
        orderUpdater.setStatus(order, OrderStatus.PAYED);
        assertTrue(order.getHistory().containsKey(OrderStatus.PAYED));
        assertEquals(order.getState().getOrderStatus(), OrderStatus.PAYED);
        assertThrows(OrderStateException.class, () -> orderUpdater.setStatus(order, OrderStatus.PAYED));
        orderUpdater.setStatus(order, OrderStatus.CANCELLED);
        assertEquals(order.getState().getOrderStatus(), OrderStatus.CANCELLED);
        assertTrue(order.getHistory().containsKey(OrderStatus.PAYED));
        assertThrows(OrderStateException.class, () -> orderUpdater.setStatus(order, OrderStatus.IN_PROGRESS));
        assertFalse(order.getHistory().containsKey(OrderStatus.IN_PROGRESS));
    }

    @Test
    public void testSetStatusCancelOrder() throws OrderException, OrderStateException {
        orderUpdater.setStatus(order, OrderStatus.IN_PROGRESS);
        assertThrows(OrderStateException.class, () -> orderUpdater.setStatus(order, OrderStatus.CANCELLED));
        assertFalse(order.getHistory().containsKey(OrderStatus.CANCELLED));
    }

    @Test
    public void testSetStatusCancelOrder2() throws OrderException, OrderStateException {
        orderUpdater.setStatus(order, OrderStatus.CANCELLED);
        assertEquals(order.getState().getOrderStatus(), OrderStatus.CANCELLED);
        assertTrue(order.getHistory().containsKey(OrderStatus.CANCELLED));
    }
    @Test
    public void testChooseCookieBannedClient()
    {
        when(clientManager.isBanned(registeredClient)).thenReturn(false,true);
        when(registeredClient.getCart()).thenReturn(new Cart(),new Cart());
        when(storeManager.getMaxCookieAmount(cookie1,mockstore)).thenReturn(1,1);
        assertDoesNotThrow(() -> cookieChoice.chooseCookie(registeredClient, mockstore, cookie1, 1));
        assertThrows(OrderException.class,() -> cookieChoice.chooseCookie(registeredClient, mockstore, cookie1, 1));
    }
    @Test
    public void testChooseCookieNotEnoughCookie()
    {
        when(clientManager.isBanned(registeredClient)).thenReturn(false,false);
        when(registeredClient.getCart()).thenReturn(new Cart(),new Cart());
        when(storeManager.getMaxCookieAmount(cookie1,mockstore)).thenReturn(1,0);
        assertDoesNotThrow(() -> cookieChoice.chooseCookie(registeredClient, mockstore, cookie1, 1));
        assertThrows(CookieException.class,() -> cookieChoice.chooseCookie(registeredClient, mockstore, cookie1, 1));
    }
    @Test
    public void testChooseCookieNonAvailableCookie()
    {
        when(clientManager.isBanned(registeredClient)).thenReturn(false,false);
        when(registeredClient.getCart()).thenReturn(new Cart(),new Cart());
        when(storeManager.getMaxCookieAmount(cookie1,mockstore)).thenReturn(1,0);
        assertDoesNotThrow(() -> cookieChoice.chooseCookie(registeredClient, mockstore, cookie1, 1));
        assertThrows(CookieException.class,() -> cookieChoice.chooseCookie(registeredClient, mockstore, cookie1, 1));
    }
    @Test
    public void searchOrderThatDoesntExist() {
        assertThrows(OrderException.class, () -> orderFinder.getOrder(UUID.randomUUID()));
    }
    @Test
    public void searchOrderThatExist() throws OrderException {
        orderUpdater.addOrder(order);
        assertEquals(order, orderFinder.getOrder(order.getId()));
    }
}
