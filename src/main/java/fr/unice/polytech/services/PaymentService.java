package fr.unice.polytech.services;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private static PaymentService INSTANCE;
    public static PaymentService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PaymentService();
        }
        return INSTANCE;
    }

    public void performPayment(double amount) {
        System.out.println("Payment of " + amount + " performed");
    }
}
