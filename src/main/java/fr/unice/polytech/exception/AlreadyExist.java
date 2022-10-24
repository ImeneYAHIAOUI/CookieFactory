package fr.unice.polytech.exception;

public class AlreadyExist extends Exception {
    public AlreadyExist() {
        super("Ingredient already exist");
    }
}

