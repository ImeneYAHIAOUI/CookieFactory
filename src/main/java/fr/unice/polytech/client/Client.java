package fr.unice.polytech.client;

import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.order.Order;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Client {
    @Getter
    private final String phoneNumber;
    @Getter
    private final Cart cart;

    public Client(String phoneNumber) throws InvalidPhoneNumberException {
        checkPhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
        this.cart = new Cart();
    }

    /**
     * Check if the phone number is valid
     *
     * @param phoneNumber the phone number to check
     * @throws InvalidPhoneNumberException if the phone number is invalid
     */
    private void checkPhoneNumber(String phoneNumber) throws InvalidPhoneNumberException {
        Pattern pattern = Pattern.compile("(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}");
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            throw new InvalidPhoneNumberException(phoneNumber);
        }
    }

    public void validateOrder(Order order) {
        cart.emptyItems();
    }

    public void getNotified(Order order, String message) {
        System.out.println(order + " : " + message);
    }

    public boolean isRegistered() {
        return false;
    }
}
