package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Cart;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.InvalidPickupTimeException;
import fr.unice.polytech.interfaces.AgendaProcessor;
import fr.unice.polytech.interfaces.CartHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AgendaTest {
    @Autowired
    AgendaProcessor agendaProcessor;
    @Autowired
    CartHandler cartHandler;
    Cart cart;
    Store store;
    Cook cook;
    @BeforeEach
    public void setup() {
        LocalDateTime date = LocalDateTime.of(2022, 12, 17, 8, 0);
        ZoneId zoneId = ZoneId.systemDefault();
        agendaProcessor.setClock(Clock.fixed(date.atZone(zoneId).toInstant(), zoneId));
        cart = new Cart();
        store = mock(Store.class);
        cook = new Cook();
        when(store.getOpeningTime()).thenReturn(LocalTime.parse("08:00"));
        when(store.getClosingTime()).thenReturn(LocalTime.parse("20:00"));

    }

    @Test
    public void getPossiblePickupTimes () {
        List<LocalTime> possiblePickupTimes = agendaProcessor.getPossiblePickupTimes(cart, store);
        assertEquals(possiblePickupTimes.size(), 0);
        when(store.getCooks()).thenReturn(List.of(cook));
        possiblePickupTimes = agendaProcessor.getPossiblePickupTimes(cart, store);
        assertNotEquals(0, possiblePickupTimes.size());
    }

    @Test
    public void getPossiblePickupTimesForADate() throws InvalidPickupTimeException {
        List<LocalDateTime> possiblePickupTimes = agendaProcessor.getPossiblePickupTimesForADate(cart, LocalDate.of(2022, 12, 18), store);
        assertEquals(0, possiblePickupTimes.size());
        when(store.getCooks()).thenReturn(List.of(cook));
        possiblePickupTimes = agendaProcessor.getPossiblePickupTimesForADate(cart, LocalDate.of(2022, 12, 18), store);
        assertNotEquals(0, possiblePickupTimes.size());
        assertThrows(InvalidPickupTimeException.class, () -> agendaProcessor.getPossiblePickupTimesForADate(cart, LocalDate.of(2022, 12, 17), store));
    }

    @Test
    public void choosePickupTime() throws InvalidPickupTimeException {
        when(store.getCooks()).thenReturn(List.of(cook));
        assertThrows(InvalidPickupTimeException.class, () -> agendaProcessor.choosePickupTime(cart, store,LocalTime.parse("07:00")));
        assertThrows(InvalidPickupTimeException.class, () -> agendaProcessor.choosePickupTime(cart, store,LocalTime.parse("21:00")));
        assertThrows(InvalidPickupTimeException.class, () -> agendaProcessor.choosePickupTime(cart, store,LocalTime.parse("08:00")));
        agendaProcessor.choosePickupTime(cart, store,LocalTime.parse("08:35"));
        assertEquals(LocalDateTime.parse("2022-12-17T08:35"), cart.getPickupTime());
    }

    @Test
    public void  choosePickupTimeAtAnotherDate() throws InvalidPickupTimeException {
        when(store.getCooks()).thenReturn(List.of(cook));
        assertThrows(InvalidPickupTimeException.class, () -> agendaProcessor.choosePickupTimeAtAnotherDate(cart, store, LocalDateTime.parse("2022-12-17T08:35")));
        assertThrows(InvalidPickupTimeException.class, () -> agendaProcessor.choosePickupTimeAtAnotherDate(cart, store,LocalDateTime.parse("2022-12-18T07:00")));
        assertThrows(InvalidPickupTimeException.class, () -> agendaProcessor.choosePickupTimeAtAnotherDate(cart, store,LocalDateTime.parse("2022-12-18T21:00")));
        assertThrows(InvalidPickupTimeException.class, () -> agendaProcessor.choosePickupTimeAtAnotherDate(cart, store,LocalDateTime.parse("2022-12-18T08:00")));
        agendaProcessor.choosePickupTimeAtAnotherDate(cart, store,LocalDateTime.parse("2022-12-18T08:35"));
        assertEquals(LocalDateTime.parse("2022-12-18T08:35"), cart.getPickupTime());
    }




}
