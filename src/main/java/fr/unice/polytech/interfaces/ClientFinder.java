package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.order.Order;

import java.util.List;

public interface ClientFinder {
    List<Order> getClientPastOrders(RegisteredClient client);
    List<RegisteredClient> getRegisteredClients();
    List<RegisteredClient> getTooGoodToGoClients();
    List<RegisteredClient> getConnectedClients();
}
