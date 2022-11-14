package fr.unice.polytech.client;

import fr.unice.polytech.exception.PickupTimeNotSetException;
import fr.unice.polytech.order.Item;
import fr.unice.polytech.recipe.CookieSize;
import fr.unice.polytech.recipe.PartyCookie;
import fr.unice.polytech.store.TimeSlot;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {
    private final List<Item> items;
    private LocalTime pickupTime;
    @Getter
    private Double subtotal;
    @Getter
    @Setter
    private Double tax;
    @Getter
    private Double total;
    public Cart() {
        this.items = new ArrayList<>();
        pickupTime = null;
        subtotal=0.0;
        total=0.0;
    }

    public void addItem(Item item) {
        if (items.stream().anyMatch(item1 -> item1.getCookie() == item.getCookie())) {
            Item existingItem = items.stream().filter(item1 -> item1.getCookie() == item.getCookie()).findFirst().orElse(null);
            assert existingItem != null;
            existingItem.increaseQuantity(item.getQuantity());

        } else {
            items.add(item);
        }
        calculateAccordingPrice(item);
    }

    private void calculateAccordingPrice(Item item) {
        subtotal+= item.getCookie().getPrice()* item.getQuantity();
        int factor = calculateFactorDependingOnSize(item.getCookie().getSize());
        total =subtotal*(1+tax)*factor;
    }

    /**
     * Returns the time slot that the order corresponding
     * to this cart should occupy
     *
     * @return a time slot
     * @throws PickupTimeNotSetException if the pickup time is not set on this cart
     */
    public TimeSlot getEstimatedTimeSlot() throws PickupTimeNotSetException {
        if (pickupTime == null) {
            throw new PickupTimeNotSetException();
        }
        return new TimeSlot(pickupTime.minus(totalCookingTime()), pickupTime);
    }

    /**
     * Returns the amount of time needed to prepare the order
     * This takes into account the fact that time is divided into 15 minutes slots
     *
     * @return the amount of time needed to prepare the order rounded up to the nearest 15 minutes
     */
    public Duration totalCookingTime() {
        int total = 0;
        for (Item i : items) {
            total += i.getCookie().getCookingTime();
        }
        // An occupied time slot for a cook should be a multiple of 15 minutes.
        total = 15 - (total % 15);
        return Duration.ofMinutes(total);
    }

    public void emptyItems() {
        items.clear();
        subtotal=0.0;
        total=0.0;
        tax=0.0;
    }

    private int calculateFactorDependingOnSize(CookieSize cookieSize){
        int factor;
        factor = switch (cookieSize) {
            case L -> 4;
            case XL -> 5;
            case XXL -> 6;
            case  BASIC -> 1;
        };
        return factor;
    }
}
