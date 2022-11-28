package fr.unice.polytech.client;

import fr.unice.polytech.exception.ClientException;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.store.Store;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
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

    public void addToGoodToGo(String mail, List<LocalDateTime> list, Store store) throws ClientException {
        throw new ClientException("You can't have notifications for the Too Good To Go bags of you are not registered.");
    }
}
