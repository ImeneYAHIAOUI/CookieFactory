package fr.unice.polytech.exception;

public class CookException extends Exception {
    public CookException(String message) {
        super(message);
    }

    public CookException() {
        super("No cook available");
    }
}
