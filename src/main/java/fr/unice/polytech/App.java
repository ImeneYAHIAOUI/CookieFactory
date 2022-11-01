package fr.unice.polytech;

import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.exception.BadQuantityException;
import fr.unice.polytech.exception.CookException;
import fr.unice.polytech.exception.StoreException;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        COD cod = new COD();

        System.out.println("Store :"+ cod.getStores().get(0));
        System.out.println("Recipe : "+cod.getRecipes().get(0));
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your phone number :");
        String phoneNumber = scanner.nextLine();
        Client client = new UnregisteredClient(Integer.parseInt(phoneNumber));

        System.out.println("Enter the number of cookie you want :");
        String amount = scanner.nextLine();
        cod.chooseAmount(Integer.parseInt(amount), cod.getRecipes().get(0), client.getCart());
        try {
            String orderId = cod.finalizeOrder(client, cod.getStores().get(0));
            System.out.println("Congrats ! Here is the id to pick up your order : " + orderId);
        } catch (BadQuantityException | CookException | StoreException exception) {
            System.out.println(exception.getMessage());
        }

    }
}
