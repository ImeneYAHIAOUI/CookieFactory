package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.ClientException;
import fr.unice.polytech.exception.InvalidInputException;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.exception.RegistrationException;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientHandler {

    void addToGoodToGo(Client client,String mail, List<LocalDateTime> list, Store store)throws ClientException;
    RegisteredClient logIn(String mail, String password) throws InvalidInputException;
    void logOff(RegisteredClient client);
    void register(String mail, String password, String phoneNumber) throws RegistrationException,InvalidPhoneNumberException;
    boolean isBanned(RegisteredClient client);
    void checkPhoneNumber(String phoneNumber) throws InvalidPhoneNumberException;
    public void clearTooGoodToGoClients();
}
