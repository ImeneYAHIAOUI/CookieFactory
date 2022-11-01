package fr.unice.polytech;

/**
 * Service that sends SMS to the user when the status of the order changes
 * It is a singleton
 */
public class SMSService {

    private static SMSService INSTANCE = null;

    public static SMSService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SMSService();
        }
        return INSTANCE;
    }

    public void notifyClient(int clientPhoneNumber) {
        System.out.println("Sending SMS to " + clientPhoneNumber);
    }
}
