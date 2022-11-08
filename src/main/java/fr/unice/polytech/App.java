package fr.unice.polytech;

import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.recipe.*;
import fr.unice.polytech.store.Store;

import java.util.ArrayList;
import java.util.Scanner;

public class App {
    static Scanner SCANNER = new Scanner(System.in);
    static COD COD = new COD();
    public static void main(String[] args) throws RegistrationException, InvalidInputException, StoreException, OrderException, PaymentException, CookException, CookieException, BadQuantityException, AlreadyExistException {
        welcomeInterface();
    }

    private static void printStores(){
        System.out.println("Stores:");
        for (Store s: COD.getStores()) {
            System.out.println(s);
        }
    }

    private static void printRecipes(){
        System.out.println("Recipes:");
        for (Cookie c: COD.getRecipes()) {
            System.out.println(c);
        }
    }

    private static void printRecipes(Store s){
        System.out.println("Recipes Store "+s);
        for (Cookie c: s.getRecipes()) {
            System.out.println(c);
        }
    }

    private static void welcomeInterface() throws RegistrationException, InvalidInputException, StoreException, OrderException, PaymentException, CookException, CookieException, BadQuantityException, AlreadyExistException {
        System.out.println("Welcome in the Cookie On Demand System !");
        printStores();
        printRecipes();

        System.out.println("Are you a Client (Cl), a Cook (Co), an admin (A) or do ypu want to Quit (Q) ?");
        String rep = SCANNER.nextLine();
        switch (rep) {
            case "Cl" -> clientInterface();
            case "Co" -> cookInterface();
            case "A" -> adminInterface();
            case "Q" -> {
                return;
            }
            default -> welcomeInterface();
        }
        welcomeInterface();
    }

    private static void cookInterface() throws OrderException {
        System.out.println("Cook Interface : Do you want to suggest a recipe (S), or to set a status order (O) ?");
        String rep = SCANNER.nextLine();
        switch (rep) {
            case "S" -> suggestRecipe();
            case "O" -> setStatusOrder();
            default -> cookInterface();
        }
    }

    private static void suggestRecipe(){
        System.out.println("Let's create a recipe ! What is the name ?");
        String name = SCANNER.nextLine();
        System.out.println("Choose a price :");
        String price = SCANNER.nextLine();
        System.out.println("Estimate the cooking time :");
        String time = SCANNER.nextLine();
        System.out.println("Do you want a Cooking Crunchy (Cr) or Chewy (Ch) ? ");
        String cooking = SCANNER.nextLine();
        System.out.println("Do you want a mix Mixed (M) or Topped (T)? ");
        String mix = SCANNER.nextLine();
        System.out.println("Choose a dough :");
        String dough = SCANNER.nextLine();
        System.out.println("Choose a flavor :");
        String flavor = SCANNER.nextLine();
        Mix mix_chosen = Mix.MIXED;
        if(mix.equals("T"))
            mix_chosen = Mix.TOPPED;
        Cooking cooking_chosen = Cooking.CHEWY;
        if(cooking.equals("Cr"))
            cooking_chosen = Cooking.CRUNCHY;
        COD.suggestRecipe(new Cookie(name, Double.valueOf(price), Integer.parseInt(time), cooking_chosen, mix_chosen, new Dough(dough, 0.0), new Flavour(flavor, 0.0), new ArrayList<>()));
    }

    private static void setStatusOrder() throws OrderException {
        System.out.println("Enter the idOrder :");
        String rep = SCANNER.nextLine();
        Order o = COD.getOrder(rep);
        System.out.println("The order "+rep+" has a status "+o.getStatus());
        System.out.println("Choose the new status : in progress (P), Ready (R), Completed (C), Obsolete (O).");
        rep = SCANNER.nextLine();
        OrderStatus status = o.getStatus();
        switch (rep) {
            case "P" -> status = OrderStatus.IN_PROGRESS;
            case "R" -> status = OrderStatus.READY;
            case "C" -> status = OrderStatus.COMPLETED;
            case "O" -> status = OrderStatus.OBSOLETE;
            default -> {}
        }
        COD.setStatus(o, status);
    }

    private static void adminInterface() throws StoreException, AlreadyExistException, BadQuantityException {
        System.out.println("Admin Interface : Do you want to add a Store (S), fill an Inventory (I), add a Cook (C), or to validate the news Recipes (R) ?");
        String rep = SCANNER.nextLine();
        switch (rep) {
            case "S" -> addStore();
            case "I" -> fillInventory();
            case "C" -> addCook();
            case "R" -> validateRecipes();
            default -> adminInterface();
        }
    }

    private static void addStore(){
        System.out.println("Let's create a store ! How many cooks do you want ?");
        String nbCook = SCANNER.nextLine();
        System.out.println("Enter the address of the store :");
        String address = SCANNER.nextLine();
        System.out.println("Enter the opening time :");
        String open = SCANNER.nextLine();
        System.out.println("Enter the ending time :");
        String end = SCANNER.nextLine();
        COD.addStore(Integer.parseInt(nbCook), address, open, end);
    }

    private static void fillInventory() throws StoreException, AlreadyExistException, BadQuantityException {
        System.out.println("Enter the id of a store :");
        String idStore = SCANNER.nextLine();
        Store store = COD.getStore(Integer.parseInt(idStore));
        System.out.println("What is the name of the ingredient you want to add ?");
        String name = SCANNER.nextLine();
        System.out.println("How much do you want to add ?");
        String amount = SCANNER.nextLine();
        //Pb d'ingredient, pas sûre de comprendre comment l'utiliser
        store.addIngredients(new Ingredient(name, 0.0), Integer.parseInt(amount));
    }

    private static void addCook() throws StoreException {
        System.out.println("Enter the id of the store for the cook :");
        String idStore = SCANNER.nextLine();
        COD.addCook(Integer.parseInt(idStore));
    }

    private static void validateRecipes(){
        if(COD.getSuggestedRecipes().isEmpty())
            System.out.println("No suggested recipes today !");
        for (Cookie c: COD.getSuggestedRecipes()) {
            System.out.println(c);
            System.out.println("Do you want to validate this recipe ? (Y/N)");
            String rep = SCANNER.nextLine();
            if(rep.equals("Y")){
                System.out.println("Choose a price for the recipe :");
                String price = SCANNER.nextLine();
                COD.acceptRecipe(c, Double.valueOf(price));
                System.out.println("Recipe accepted.");
            }
            else if (rep.equals("N")) {
                COD.declineRecipe(c);
                System.out.println("Recipe declined.");
            } else
                System.out.println("Recipe passed.");
        }
    }

    private static void clientInterface() throws RegistrationException, InvalidInputException, OrderException, StoreException, CookieException, PaymentException, CookException, BadQuantityException {
        Client client;
        System.out.println("Client Interface : ");

        //Authentication
        if(askIfRegister()){
            if(!askIfAccount()){
                System.out.println("Let's create your account ! Enter your userName :");
                String un = SCANNER.nextLine();
                System.out.println("Enter your password :");
                String pw = SCANNER.nextLine();
                System.out.println("Enter your phone number :");
                String pn = SCANNER.nextLine();
                COD.register(un, pw, pn);
            }
            System.out.println("Let's log into your account ! Enter your userName :");
            String un = SCANNER.nextLine();
            System.out.println("Enter your password :");
            String pw = SCANNER.nextLine();
            client = COD.logIn(un, pw);
        } else {
            System.out.println("You still have to enter your phone number :");
            String phoneNumber = SCANNER.nextLine();
            client = new UnregisteredClient(phoneNumber);
        }

        //Order a cookie
        if(orderOrCancel()){
            printStores();
            System.out.println("Enter the id of a store :");
            String idStore = SCANNER.nextLine();
            Store store = COD.getStore(Integer.parseInt(idStore));
            printRecipes(store);
            System.out.println("Choose a recipe :");
            String name = SCANNER.nextLine();
            System.out.println("Choose an amount :");
            String amount = SCANNER.nextLine();
            COD.chooseCookie(client, store, name, Integer.parseInt(amount));

            while (name.equals("STOP")){
                System.out.println("Choose a recipe (STOP if you have enough):");
                name = SCANNER.nextLine();
                System.out.println("Choose an amount :");
                amount = SCANNER.nextLine();
                //Bug ici, pb d'inventaire ?
                COD.chooseCookie(client, store, name, Integer.parseInt(amount));
            }
            System.out.println("Your Cart : "+client.getCart());
            String orderId = COD.finalizeOrder(client, store);
            System.out.println("Congrats ! Here is the id to pick up your order : " + orderId);
        } else {
            System.out.println("Enter the id of your order :");
            String idOrder = SCANNER.nextLine();
            COD.cancelOrder(idOrder);
        }

    }

    private static boolean orderOrCancel(){
        System.out.println("Do you want to order (O) or to cancel your order (C) ?");
        String rep = SCANNER.nextLine();
        if(rep.equals("O"))
            return true;
        else if (rep.equals("C")) {
            return false;
        } else
            return orderOrCancel();
    }

    private static boolean askIfRegister(){
        System.out.println("Do you want to register ? (Y/N)");
        String rep = SCANNER.nextLine();
        if(rep.equals("Y"))
            return true;
        else if (rep.equals("N")) {
            return false;
        } else
            return askIfRegister();
    }

    private static boolean askIfAccount(){
        System.out.println("Do you already have an account ? (Y/N)");
        String rep = SCANNER.nextLine();
        if(rep.equals("Y"))
            return true;
        else if (rep.equals("N")) {
            return false;
        } else
            return askIfAccount();
    }
}
