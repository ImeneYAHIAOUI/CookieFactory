package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.client.Cart;
import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.store.TimeSlot;
import fr.unice.polytech.exception.PickupTimeNotSetException;

import java.time.Duration;

public interface CartHandler {
    void addItem(Item item, Cart cart);
    public void removeItem(Item item, Cart cart,int cookieRemoved);

    void clearCart(Cart cart);
    TimeSlot getEstimatedTimeSlot(Cart cart) throws PickupTimeNotSetException;

    Duration totalCookingTime(Cart cart);

}
