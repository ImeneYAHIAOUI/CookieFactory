package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.client.Cart;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.InvalidPickupTimeException;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface AgendaProcessor {

    void choosePickupTime(Cart cart, Store store, LocalTime pickupTime) throws InvalidPickupTimeException;
    void choosePickupTimeAtAnotherDate(Cart cart, Store store, LocalDateTime pickupTime) throws InvalidPickupTimeException;
    List<LocalDateTime> getPossiblePickupTimesForADate(Cart cart, LocalDate date, Store store) throws InvalidPickupTimeException;
    List<LocalTime> getPossiblePickupTimes(Cart cart, Store store);
    Clock getClock();
    void setClock(Clock fixed);
}
