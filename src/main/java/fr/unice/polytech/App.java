package fr.unice.polytech;

import fr.unice.polytech.client.Client;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        COD cod = new COD();
        System.out.println("Store :"+ cod.stores.get(0));
        System.out.println("Recipe : "+cod.recipes.get(0));
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your phone number :");
        String phoneNumber = scanner.nextLine();
        Client client = new Client(Integer.parseInt(phoneNumber));

        System.out.println("Enter the number of cookie you want :");
        String amount = scanner.nextLine();
        cod.chooseAmount(Integer.parseInt(amount), cod.recipes.get(0), client.getCart());

        System.out.println("Congrats ! Here is the id to pick up your order : "+cod.finalizeOrder(client, cod.stores.get(0)));

    }
}
