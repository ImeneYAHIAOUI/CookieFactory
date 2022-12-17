package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Cart;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.TimeSlot;
import fr.unice.polytech.exception.InvalidPickupTimeException;
import fr.unice.polytech.interfaces.AgendaProcessor;
import fr.unice.polytech.interfaces.CartHandler;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

//Class handling the pickup times
@Component
public class Agenda implements AgendaProcessor {

    @Getter
    @Setter
    private Clock clock;
    private final CartHandler cartManager;

    @Autowired
    public Agenda(CartHandler cartManager) {
        this.cartManager = cartManager;
        clock = Clock.systemDefaultZone();
    }

    @Override
    public List<LocalTime> getPossiblePickupTimes(Cart cart, Store store) {
        List<LocalTime> possiblePickupTimes = new ArrayList<>();
        // Acceptable earliest cooking start time is 10 minutes after now.
        // This is to account for the time it takes to place the order.
        LocalTime now = LocalTime.now(clock);
        LocalTime cookingStartTime = now.plusMinutes(15 + (now.getMinute() % 5)).truncatedTo(ChronoUnit.MINUTES);
        TimeSlot potentialTimeSlot = new TimeSlot(cookingStartTime, cartManager.totalCookingTime(cart), this.clock);
        while (potentialTimeSlot.getEnd().isBefore(store.getClosingTime().atDate(LocalDate.now(clock)))) {
            if (store.getCooks().stream().anyMatch(cook -> cook.canTakeTimeSlot(potentialTimeSlot))) {
                possiblePickupTimes.add(LocalTime.from(potentialTimeSlot.getEnd()));
            }
            // New pickup time every 5 minutes
            potentialTimeSlot.slideBy(Duration.ofMinutes(5));
        }
        return possiblePickupTimes;
    }

    @Override
    public List<LocalDateTime> getPossiblePickupTimesForADate(Cart cart, LocalDate date, Store store) throws InvalidPickupTimeException {
        if(date.isAfter(LocalDate.now(clock))) {
            List<LocalDateTime> possiblePickupTimes = new ArrayList<>();
            // Acceptable earliest cooking start time is 10 minutes after now.
            // This is to account for the time it takes to place the order.
            LocalTime cookingStartTime = store.getOpeningTime().plusMinutes(15 + (store.getOpeningTime().getMinute() % 5)).truncatedTo(ChronoUnit.MINUTES);
            TimeSlot potentialTimeSlot = new TimeSlot(cookingStartTime, cartManager.totalCookingTime(cart), date);
            while (potentialTimeSlot.getEnd().isBefore(store.getClosingTime().atDate(date))) {
                if (store.getCooks().stream().anyMatch(cook -> cook.canTakeTimeSlot(potentialTimeSlot))) {
                    possiblePickupTimes.add(LocalDateTime.from(potentialTimeSlot.getEnd()));
                }
                // New pickup time every 5 minutes
                potentialTimeSlot.slideBy(Duration.ofMinutes(5));
            }
            return possiblePickupTimes;
        }
        throw new InvalidPickupTimeException("The date must be after today");
    }

    /**
     * Sets the desired pickup time on the given cart
     *
     * @param cart       the cart to set the pickup time on
     * @param store the store where the order will be picked up
     * @param pickupTime the desired pickup time
     */

    @Override
    public void choosePickupTime(Cart cart, Store store, LocalTime pickupTime) throws InvalidPickupTimeException {
        List<LocalTime> validPickupTimes = getPossiblePickupTimes(cart, store);
        if (!validPickupTimes.contains(pickupTime)) {
            throw new InvalidPickupTimeException(pickupTime);
        }
        cart.setPickupTime(pickupTime.atDate(LocalDate.now()));
    }

    /**
     * Sets the desired pickup time on the given cart for a given date
     * @param cart the cart to set the pickup time on
     * @param store the store where the order will be picked up
     * @param pickupTime the desired pickup time
     */

    @Override
    public void  choosePickupTimeAtAnotherDate(Cart cart, Store store, LocalDateTime pickupTime) throws InvalidPickupTimeException {
        List<LocalDateTime> validPickupTimes = getPossiblePickupTimesForADate(cart, pickupTime.toLocalDate(), store);
        if (!validPickupTimes.contains(pickupTime)) {
            throw new InvalidPickupTimeException(pickupTime.toLocalTime());
        }
        cart.setPickupTime(pickupTime);
    }

}
