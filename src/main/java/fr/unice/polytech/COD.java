package fr.unice.polytech;

import fr.unice.polytech.client.Cart;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.RegisteredClient;
import fr.unice.polytech.exception.BadQuantityException;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.exception.RegistrationException;
import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.recipe.Cookie;
import fr.unice.polytech.recipe.Ingredient;
import fr.unice.polytech.recipe.Topping;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.Store;
import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class COD {
    @Getter
    private final List<Cookie> recipes;
    @Getter
    private final List<Cookie> suggestedRecipes;
    @Getter
    private final List<Store> stores;
    @Getter
    private final List<Order> orders;
    @Getter
    private final List<RegisteredClient> clients;


    public COD(){
        this.recipes = new ArrayList<>();
        this.stores = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.suggestedRecipes = new ArrayList<>();
        this.clients = new ArrayList<>();

        //Initialisation with 1 store + 1 recipe
        Inventory inventory = new Inventory(new ArrayList<>());
        Store store = new Store(
                List.of(new Cook(1)),
                recipes,
                "30 Rte des Colles, 06410 Biot",
                LocalTime.parse("08:00"),
                LocalTime.parse("20:00"),
                1,
                inventory
        );
        stores.add(store);
    }

    public void chooseAmount(int i, Cookie cookie, Cart cart){
        cart.addItem(new Item(i, cookie));
    }

    public String finalizeOrder(Client client, Store store) throws BadQuantityException {
        Cook cook = store.getFreeCook(client.getCart());
        Order order = new Order("order1", client, cook);
        // remove ingredients from inventory
        for (Item item : order.getItems()) {
            Cookie cookie = item.getCookie();
            int numberOfCookie = item.getQuantity();
            Ingredient dough = cookie.getDough();
            int doughQuantity = dough.getQuantity();
            order.store.getInventory().decreaseIngredientQuantity(dough, doughQuantity * numberOfCookie);
            Ingredient flavour = cookie.getFlavour();
            int flavourQuantity = flavour.getQuantity();
            order.store.getInventory().decreaseIngredientQuantity(item.getCookie().getFlavour(), numberOfCookie * flavourQuantity);
            // topping
            for (Topping topping : cookie.getToppings()) {
                store.getInventory().decreaseIngredientQuantity(topping, numberOfCookie * topping.getQuantity());
            }
            client.emptyCart(order);
            this.orders.add(order);
        }
        //cook.addOrder(order);         //Pas de temps de cuisson pour l'instant donc pas de TimeSlot
        return order.getId();
    }

    public void register(String id, String password, int phoneNumber) throws RegistrationException {
        if (clients.stream().anyMatch(client -> client.getId().equals(id)))
            throw new RegistrationException("User " + id + " is already registered.");
        clients.add(new RegisteredClient(id, password, phoneNumber));
    }

    public void setStatus(String orderId, OrderStatus status) throws OrderException {
        Order order = orders.stream().filter(ord -> ord.getId().equals(orderId)).findFirst().orElse(null);
        assert order != null;
        order.setStatus(status);
    }

    public void setHours(Store store, LocalTime openingTime, LocalTime closingTime) {
        if (openingTime.isBefore(closingTime)) {
            this.stores.get(this.stores.indexOf(store)).setHours(openingTime, closingTime);
        } else {
            System.out.println("Error, given closingTime is before openingTime, openingTime get closedTime value and closingTime get openingTime value");
            this.stores.get(this.stores.indexOf(store)).setHours(closingTime, openingTime);
        }
    }

    public void chooseCookie(Client client, Store store, Cookie cookie, int amount) throws CookieException {
        if (!store.getRecipes().contains(cookie)) {
            throw new CookieException("this cookie is not available in this store");
        }
        int maxCookieAmount = store.getMaxCookieAmount(cookie);
        if (maxCookieAmount < amount) {
            throw new CookieException("this store can't make this amount of cookies");
        }
        client.getCart().addItem(new Item(amount, cookie));
    }

    public void suggestRecipe(Cookie cookie) {
        if (!suggestedRecipes.contains(cookie) && !recipes.contains(cookie)) {
            suggestedRecipes.add(cookie);
        }
    }

    public void acceptRecipe(Cookie cookie,Double price){//TODO rajouter Exception si le cookie n'existe pas ? (tu peux utiliser cookieException (imene))

        if(suggestedRecipes.contains(cookie)){
            recipes.add(cookie);
            cookie.setPrice(price);
            suggestedRecipes.remove(cookie);
        }
    }
    public void declineRecipe(Cookie cookie) {
        suggestedRecipes.remove(cookie);
    }

    public void cancelOrder(Order order)throws OrderException  {
        order.setStatus(OrderStatus.CANCELLED);
        Store store =order.store;
        for ( Item item : order.getItems()) {
            Cookie cookie=item.getCookie();
            int numberOfCookie=item.getQuantity();
            Ingredient dough=cookie.getDough();
            int doughQuantity=dough.getQuantity();
            store.getInventory().addIngredient(dough,doughQuantity*numberOfCookie);
            Ingredient flavour = cookie.getFlavour();
            int flavourQuantity = flavour.getQuantity();
            store.getInventory().addIngredient(item.getCookie().getFlavour(), numberOfCookie * flavourQuantity);
            //topping
            cookie.getToppings().forEach(topping -> store.getInventory().addIngredient(topping, numberOfCookie * topping.getQuantity()));
        }
    }
    public void printStoresOpeningHours(){
        for(Store store : stores){
            System.out.println(store.openingTime + " - " + store.closingTime);
        }
    }
}

