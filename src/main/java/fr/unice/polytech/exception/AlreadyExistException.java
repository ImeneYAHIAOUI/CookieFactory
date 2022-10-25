package fr.unice.polytech.exception;

public class AlreadyExistException extends Exception {
    public AlreadyExistException() {
        super("Ingredient already exist");
    }
}

