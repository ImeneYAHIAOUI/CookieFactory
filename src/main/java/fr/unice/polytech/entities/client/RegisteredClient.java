package fr.unice.polytech.entities.client;

import fr.unice.polytech.entities.order.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RegisteredClient extends Client implements MailSubscriber {

    private final String password;
    private List<Order> pastOrders;
    private int nbCookie;
    private String remainingBanTime;
    private boolean eligibleForDiscount;
    private boolean toGoodToGoClient;
    private String mail;
    private List<LocalDateTime> notificationsDates;

    public RegisteredClient(String password, String phoneNumber, String mail) {
        super(phoneNumber);
        this.mail=mail;
        this.password = password;
        nbCookie = 0;
        this.pastOrders = new ArrayList<>();
        eligibleForDiscount = false;
        toGoodToGoClient = false;
        notificationsDates = new ArrayList<>();
    }

    public boolean isRegistered() {
        return true;
    }

    public void sendMail(NotificationMessage message) {
        System.out.println("Sending mail to " + this.mail + " with message : " + message.getMessage());
    }
}
