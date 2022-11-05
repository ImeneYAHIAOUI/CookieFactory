package fr.unice.polytech.services;

import fr.unice.polytech.exception.PaymentException;

public class PaymentService {
    private static PaymentService INSTANCE;

    public static PaymentService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PaymentService();
        }
        return INSTANCE;
    }

    public void performPayment(double amount) throws PaymentException {
        System.out.println("Payment of " + amount + " performed");
    }
}
