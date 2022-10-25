package fr.unice.polytech;

/**
 * Service that sends SMS to the user when the status of the order changes
 * It is a singleton
 */
public class SMSService {

    private static SMSService instance = null;

    public static SMSService getInstance() {
        if (instance == null) {
            instance = new SMSService();
        }
        return instance;
    }

    public void notifyClient(int clientPhoneNumber) {
        System.out.println("Sending SMS to " + clientPhoneNumber);
    }
}
