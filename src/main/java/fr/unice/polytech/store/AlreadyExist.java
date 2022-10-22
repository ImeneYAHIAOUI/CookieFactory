package fr.unice.polytech.store;

public class AlreadyExist extends Exception {
    public AlreadyExist() {
        super("Ingredient already exist");
    }
}

