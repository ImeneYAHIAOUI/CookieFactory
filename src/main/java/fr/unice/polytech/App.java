package fr.unice.polytech;

import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.RegisteredClient;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.recipe.*;

import fr.unice.polytech.store.Occasion;
import fr.unice.polytech.store.Store;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.min;

public class App {
  /*  static Scanner SCANNER = new Scanner(System.in);
    static COD COD = new COD();

    public static void main(String[] args) throws RegistrationException, InvalidInputException, StoreException, OrderException, CookException, CookieException, BadQuantityException, AlreadyExistException, CatalogException, InvalidPhoneNumberException, InvalidPickupTimeException {
        welcomeInterface();

    }

    private static void welcomeInterface() throws RegistrationException, InvalidInputException, StoreException, OrderException, CookException, CookieException, BadQuantityException, AlreadyExistException, CatalogException, InvalidPhoneNumberException, InvalidPickupTimeException {
        System.out.println("Welcome in the Cookie On Demand System !");
        COD.printStores();
        COD.printRecipes();

        System.out.println("Are you a Client (Cl), a Cook (Co), an admin (A) or do you want to Quit (Q) ?");
        String rep = SCANNER.nextLine();
        switch (rep) {
            case "Cl" -> authenticationInterface();
            case "Co" -> cookInterface();
            case "A" -> adminInterface();
            case "Q" -> {
                return;
            }
            default -> welcomeInterface();
        }
        welcomeInterface();
    }

    private static void cookInterface() throws OrderException, CatalogException {
        System.out.println("Cook Interface : Do you want to suggest a recipe (S), or to set a status order (O) ?");
        String rep = SCANNER.nextLine();
        switch (rep) {
            case "S" -> suggestRecipe();
            case "O" -> setStatusOrder();
            default -> cookInterface();
        }
    }

    private static void suggestRecipe() throws CatalogException {
        COD.printCatalog();
        System.out.println("Let's create a recipe ! What is the name ?");
        String name = SCANNER.nextLine();
        System.out.println("Choose a price :");
        String price = SCANNER.nextLine();
        System.out.println("Estimate the cooking time :");
        String time = SCANNER.nextLine();
        System.out.println("Do you want a Cooking Crunchy (Cr) or Chewy (Ch) ? ");
        String cooking = SCANNER.nextLine();
        System.out.println("Do you want a Mix Mixed (M) or Topped (T)? ");
        String mix = SCANNER.nextLine();
        System.out.println("Choose a dough :");
        String dough = SCANNER.nextLine();
        Ingredient doughIngredient = COD.getIngredientCatalog(dough);
        System.out.println("Choose a flavour :");
        String flavour = SCANNER.nextLine();
        Ingredient flavourIngredient = COD.getIngredientCatalog(flavour);
        Mix mix_chosen = Mix.MIXED;
        if(mix.equals("T"))
            mix_chosen = Mix.TOPPED;
        Cooking cooking_chosen = Cooking.CHEWY;
        if(cooking.equals("Cr"))
            cooking_chosen = Cooking.CRUNCHY;
        System.out.println("How many toppings do you want ?");
        String nbTopping = SCANNER.nextLine();
        List<Ingredient> toppingList = new ArrayList<>();
        for (int i = 0; i<min(Integer.parseInt(nbTopping), 3); i++){
            System.out.println("Choose a topping :");
            String t = SCANNER.nextLine();
            toppingList.add(COD.getIngredientCatalog(t));
        }
        COD.suggestRecipe(name, Double.parseDouble(price), Integer.parseInt(time), cooking_chosen, mix_chosen, doughIngredient, flavourIngredient, toppingList);
        System.out.println("You suggested the recipe "+name+".");
    }

    private static void setStatusOrder() throws OrderException {
        System.out.println("Enter the idOrder :");
        String rep = SCANNER.nextLine();
        Order o = COD.getOrder(rep);
        System.out.println("The order "+rep+" has a status "+o.getStatus()+".");
        System.out.println("Choose the new status : in progress (P), Ready (R), Completed (C), Obsolete (O).");
        rep = SCANNER.nextLine();
        OrderStatus status;
        switch (rep) {
            case "P" -> status = OrderStatus.IN_PROGRESS;
            case "R" -> status = OrderStatus.READY;
            case "C" -> status = OrderStatus.COMPLETED;
            case "O" -> status = OrderStatus.OBSOLETE;
            default -> status = o.getStatus();
        }
        COD.setStatus(o, status);
        System.out.println("The order "+rep+" has now a status "+o.getStatus()+".");
    }

    private static void adminInterface() throws StoreException, AlreadyExistException, BadQuantityException, CatalogException {
        System.out.println("Admin Interface : Do you want to add a Store (S), change Taxes of a store (T), change Hours of a store, fill an Inventory (I), add a Cook (C), add an Ingredient to the Catalog (IC) or to validate the news Recipes (R) ?");
        String rep = SCANNER.nextLine();
        switch (rep) {
            case "S" -> addStore();
            case "I" -> fillInventory();
            case "T" -> setTax();
            case "H" -> setHours();
            case "C" -> addCook();
            case "R" -> validateRecipes();
            case "IC" -> addIngredientCatalogLoop();
            default -> adminInterface();
        }
    }

    private static void addIngredientCatalogLoop() throws CatalogException {
        COD.printCatalog();
        System.out.println("How many ingredients do you want to add to the catalog ?");
        String nb = SCANNER.nextLine();
        for(int i = 0; i<Integer.parseInt(nb); i++)
            addIngredientCatalog();
        COD.printCatalog();
    }

    private static void addIngredientCatalog() throws CatalogException {
        COD.printCatalog();
        System.out.println("Enter the name of the ingredient :");
        String name = SCANNER.nextLine();
        System.out.println("Enter the price of the ingredient :");
        String price = SCANNER.nextLine();
        System.out.println("Is your ingredient a Flavour (F), a Dough (D), or a Topping (T) ?");
        String rep = SCANNER.nextLine();
        IngredientType ingredientType;
        switch (rep) {
            case "F" -> ingredientType = IngredientType.FLAVOUR;
            case "D" -> ingredientType = IngredientType.DOUGH;
            default -> ingredientType = IngredientType.TOPPING;
        }
        COD.addIngredientCatalog(name, Double.parseDouble(price), ingredientType);
        System.out.println(name+" added with success.");
    }

    private static void setTax() throws StoreException {
        System.out.println("Enter the idStore :");
        String rep = SCANNER.nextLine();
        Store s = COD.getStore(Integer.parseInt(rep));
        System.out.println("The store "+rep+" has taxes at "+s.getTax()+".");
        System.out.println("Enter the new taxes :");
        String tax = SCANNER.nextLine();
        COD.setTax(s, Double.parseDouble(tax));
        System.out.println("The store "+rep+" has now taxes at "+s.getTax()+".");
    }

    private static void setHours() throws StoreException {
        System.out.println("Enter the idStore :");
        String rep = SCANNER.nextLine();
        Store s = COD.getStore(Integer.parseInt(rep));
        System.out.println("The store "+rep+" has hours ("+s.openingTime+";"+s.closingTime+").");
        System.out.println("Enter the new opening hour (ex: \"08:00\":");
        String open = SCANNER.nextLine();
        System.out.println("Enter the new ending hour (ex: \"08:00\":");
        String end = SCANNER.nextLine();
        COD.setHours(s, LocalTime.parse(open), LocalTime.parse(end));
        System.out.println("The store "+rep+" has now hours ("+s.openingTime+";"+s.closingTime+").");
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
        System.out.println("Enter the tax :");
        String tax = SCANNER.nextLine();
        System.out.println("Enter an occasion :"); // à modifier plus tard pour ajouter plusieurs occasions
        String occasion = SCANNER.nextLine();
        COD.addStore(Integer.parseInt(nbCook), address, open, end, Double.parseDouble(tax), List.of(new Occasion((occasion))));
        System.out.println("You added a store at the address"+address+".");


    }

    private static void fillInventory() throws StoreException, AlreadyExistException, BadQuantityException, CatalogException {
        COD.printStores();
        COD.printCatalog();
        System.out.println("Enter the id of a store :");
        String idStore = SCANNER.nextLine();
        Store store = COD.getStore(Integer.parseInt(idStore));
        System.out.println(store.getInventory());
        System.out.println("What is the name of the ingredient you want to add ?");
        String name = SCANNER.nextLine();
        System.out.println("How much do you want to add ?");
        String amount = SCANNER.nextLine();
        COD.addInventory(store, name, Integer.parseInt(amount));
        System.out.println("You added "+amount+" "+name+" in the store "+idStore+".");
    }

    private static void addCook() throws StoreException {
        System.out.println("Enter the id of the store for the cook :");
        String idStore = SCANNER.nextLine();
        COD.addCook(Integer.parseInt(idStore));
        System.out.println("Cook added in the store "+idStore+".");
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

    private static void authenticationInterface() throws RegistrationException, InvalidInputException, OrderException, StoreException, CookieException, CookException, BadQuantityException, InvalidPhoneNumberException, InvalidPickupTimeException {
        Client client;
        System.out.println("Client Interface : ");

        //Authentication
        if (askIfRegister()) {
            if (!askIfAccount()) {
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
        clientInterface(client);
    }

    private static void clientInterface(Client client) throws StoreException, BadQuantityException, CookException, CookieException, OrderException, InvalidPickupTimeException {
        System.out.println("Client Interface : Do you want to order (O), to cancel your order (C) or the check your Past orders (P) ?");
        String rep = SCANNER.nextLine();
        switch (rep) {
            case "O" -> passOrder(client);
            case "C" -> cancelOrder();
            case "P" -> pastOrders(client);
            default -> clientInterface(client);
        }
    }

    private static void pastOrders(Client client){
        if(client.isRegistered())
            System.out.println(((RegisteredClient)client).getPastOrders());
        else
            System.out.println("You can't see you past orders if you aren't registered.");
    }

    private static void cancelOrder() throws OrderException {
        System.out.println("Enter the id of your order :");
        String idOrder = SCANNER.nextLine();
        COD.cancelOrder(idOrder);
    }

    private static void passOrder(Client client) throws StoreException, CookieException, OrderException, CookException, BadQuantityException, InvalidPickupTimeException {
        COD.printStores();
        System.out.println("Enter the id of a store :");
        String idStore = SCANNER.nextLine();
        Store store = COD.getStore(Integer.parseInt(idStore));
        COD.printRecipes(store);
        chooseOrder(client, store);
        System.out.println("Your Cart : "+client.getCart());
        choosePickUpTime(client, store);
        String orderId = COD.finalizeOrder(client, store);
        System.out.println("Congrats ! Here is the id to pick up your order : " + orderId);
    }

    private static void chooseOrder(Client client, Store store) throws CookieException, OrderException {
        System.out.println("Choose a recipe :");
        String name = SCANNER.nextLine();

        while (!name.equalsIgnoreCase("STOP")){
            System.out.println("Choose an amount :");
            String amount = SCANNER.nextLine();
            COD.chooseCookie(client, store, name, Integer.parseInt(amount));
            System.out.println("Choose a recipe (STOP if you have enough):");
            name = SCANNER.nextLine();
        }
    }

    private static void choosePickUpTime(Client client, Store store) throws InvalidPickupTimeException {
        List<LocalTime> possiblePickUpTimes = COD.getPickupTimes(client.getCart(), store);
        System.out.println("Choose a pickup time (enter the number)");
        int i = 0;
        for (LocalTime l: possiblePickUpTimes) {
            System.out.println(i++ + " "+ l);
        }
        String rep = SCANNER.nextLine();
        COD.choosePickupTime(client.getCart(), store, possiblePickUpTimes.get(Integer.parseInt(rep)));
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

   */
}