package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Cart;
import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.store.TimeSlot;
import fr.unice.polytech.exception.PickupTimeNotSetException;
import fr.unice.polytech.interfaces.CartHandler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class CartManager implements CartHandler {
    @Override
    public void addItem(Item item, Cart cart) {
        List<Item> items = cart.getItems();
        if (items.stream().anyMatch(item1 -> item1.getCookie() == item.getCookie())) {
            Item existingItem = items.stream().filter(item1 -> item1.getCookie() == item.getCookie()).findFirst().orElse(null);
            assert existingItem != null;
            existingItem.increaseQuantity(item.getQuantity());


        } else {
            items.add(item);
            cart.setItems(items);
        }
        calculateAccordingPrice(item.getQuantity()*item.getCookie().getPrice(),cart);

    }
    @Override
    public void removeItem(Item item, Cart cart,int cookieRemoved) {
        List<Item> items = cart.getItems();
        if (items.stream().anyMatch(item1 -> item1.getCookie() == item.getCookie())) {
            Item existingItem = items.stream().filter(item1 -> item1.getCookie() == item.getCookie()).findFirst().orElse(null);
            assert existingItem != null;
            if(existingItem.getQuantity()-cookieRemoved==0)
                    items.remove(existingItem);
            else
                existingItem.decreaseQuantity(cookieRemoved);
        }
        calculatedRemovedPrice(item.getQuantity()*item.getCookie().getPrice(),cart);
    }
    private void calculateAccordingPrice(Double price,Cart cart) {
        cart.setSubtotal(cart.getSubtotal()+price);
        cart.setTotal(cart.getSubtotal()*(1+(cart.getTax())/100));
    }
    private void calculatedRemovedPrice(Double price,Cart cart) {
        cart.setSubtotal(cart.getSubtotal()-price);
        cart.setTotal(cart.getSubtotal()*(1+(cart.getTax())/100));
    }

    @Override
    public TimeSlot getEstimatedTimeSlot(Cart cart) throws PickupTimeNotSetException {
        if (cart.getPickupTime() == null) {
            throw new PickupTimeNotSetException();
        }
        return new TimeSlot(cart.getPickupTime().minus(totalCookingTime(cart)), cart.getPickupTime());
    }

    @Override
    public Duration totalCookingTime(Cart cart) {
        int total = 0;
        for (Item i : cart.getItems()) {
            total += i.getCookie().getCookingTime();
        }
        // An occupied time slot for a cook should be a multiple of 15 minutes.
        total = 15 - (total % 15);
        return Duration.ofMinutes(total);
    }


    @Override
    public void clearCart(Cart cart) {
        List<Item> items = cart.getItems();
        items.clear();
        cart.setItems(items);
        cart.setSubtotal(0.0);
        cart.setTotal(0.0);
        cart.setTax(0.0);
    }

}
