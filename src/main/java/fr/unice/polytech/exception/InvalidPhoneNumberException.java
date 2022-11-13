package fr.unice.polytech.exception;

public class InvalidPhoneNumberException extends Exception {
    public InvalidPhoneNumberException(String phoneNumber) {
        super("Invalid phone number: " + phoneNumber);
    }
}
