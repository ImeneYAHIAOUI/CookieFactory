package fr.unice.polytech.cucumber.order;


import fr.unice.polytech.components.Agenda;
import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.UnregisteredClient;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.StoreFactory;
import fr.unice.polytech.exception.InvalidPickupTimeException;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.exception.OrderStateException;
import fr.unice.polytech.interfaces.AgendaProcessor;
import fr.unice.polytech.interfaces.CartHandler;
import fr.unice.polytech.interfaces.CookieChoice;
import fr.unice.polytech.interfaces.OrderUpdater;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OrderStepDefs {

    private final Client client;
    private final Cook cook;
    private final OrderUpdater orderUpdater;
    @Autowired
    private final AgendaProcessor agendaProcessor;
    private final Store store;
    private Order order;


    public OrderStepDefs(OrderUpdater orderUpdater, CartHandler cartManager) {
        this.orderUpdater = orderUpdater;
        client = new UnregisteredClient("0606060606");
        cook = new Cook();
        store = StoreFactory.createStore(List.of(cook), "30 Rte des Colles, 06410 Biot",
                                         LocalTime.parse("08:00"), LocalTime.parse("20:00"),
                                         new Inventory(new ArrayList<>()), 5.0, new ArrayList<>()
        );
        this.agendaProcessor = new Agenda(cartManager);
        LocalDateTime date = LocalDateTime.of(2022, 12,17, 8, 0);
        ZoneId zoneId = ZoneId.systemDefault();
        agendaProcessor.setClock(Clock.fixed(date.atZone(zoneId).toInstant(), zoneId));
    }

    @Given("an order")
    public void givenAnOrder() throws InvalidPickupTimeException {
        agendaProcessor.choosePickupTime(client.getCart(), store, LocalTime.parse("10:00"));
        UUID id=UUID.randomUUID();

        order = new Order(id, client, cook, store, Duration.ofMinutes(20));
    }

    @When("order status is {string}")
    public void whenOrderStatusIs(String status) throws OrderStateException {
        OrderStatus EnumStatus = OrderStatus.valueOf(status);
        if (!EnumStatus.equals(order.getState().getOrderStatus())) {
            if(!EnumStatus.equals(OrderStatus.IN_PROGRESS) && !EnumStatus.equals(OrderStatus.CANCELLED) && !EnumStatus.equals(OrderStatus.PAYED) ) {
                order.setState(OrderStatus.IN_PROGRESS);
                if (!EnumStatus.equals(OrderStatus.READY))
                    order.setState(OrderStatus.READY);
            }
            order.setState(EnumStatus);

        }
    }


    @Then("the order can be set to {string}")
    public void thenTheOrderCanBeSetTo(String status) {
        assertDoesNotThrow(() -> orderUpdater.setStatus(order, OrderStatus.valueOf(status)));
    }

    @Then("the order can not be set to {string}")
    public void thenTheOrderCanNotBeSetTo(String status) {
        assertThrows(OrderStateException.class, () -> orderUpdater.setStatus(order, OrderStatus.valueOf(status)));
    }

    @Then("the order can not be found and canceled")
    public void thenTheOrderCanNotBeFound() {
        assertThrows(OrderException.class, () -> orderUpdater.cancelOrder(UUID.randomUUID()));
    }

}
