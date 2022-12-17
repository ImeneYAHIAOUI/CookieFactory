package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.client.MailSubscriber;

public interface MailPublisher {
    /**
     * Notifies the subscribed clients that bags are available
     */
    void notifyClients();

    /**
     * Subscribes a client to the notification service
     *
     * @param subscriber the client to subscribe
     */
    void addSubscriber(MailSubscriber subscriber);
}
