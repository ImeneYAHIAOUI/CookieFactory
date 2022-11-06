package fr.unice.polytech.exception;

public class PickupTimeNotSetException extends Exception {
    public PickupTimeNotSetException() {
        super("Pickup time is not set on the cart");
    }
}
