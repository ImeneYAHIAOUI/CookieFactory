package fr.unice.polytech.entities.client;

import fr.unice.polytech.exception.InvalidPhoneNumberException;

public class UnregisteredClient extends  Client {

    public UnregisteredClient(String phoneNumber)  {
            super(phoneNumber);
    }

    public boolean isRegistered() {
        return false;
    }

}
