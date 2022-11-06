package fr.unice.polytech.exception;

import java.time.LocalTime;

public class InvalidPickupTimeException extends Throwable {
    public InvalidPickupTimeException(LocalTime pickupTime) {
        super("The pickup time " + pickupTime + " is not valid");
    }
}
