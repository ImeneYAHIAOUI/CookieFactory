package fr.unice.polytech;

import fr.unice.polytech.client.Cart;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.RegisteredClient;
import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderException;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.recipe.*;
import fr.unice.polytech.store.BadQuantity;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.Store;

import java.time.LocalTime;
import java.util.*;

public class COD {
    private final List<Cookie> recipes;
    private final List<Cookie> suggestedRecipes;
    private final List<Store> stores;
    private final List<Order> orders;
    private final List<RegisteredClient> clients;


    public COD(){
        this.recipes = new ArrayList<>();
        this.stores = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.suggestedRecipes = new ArrayList<>();
        this.clients = new ArrayList<>();

        //Initialisation with 1 store + 1 recipe
        Cookie cookie = new Cookie(
                "ChocoCookie"
                );
        Inventory inventory = new Inventory(new ArrayList<>());
        Store store = new Store(
                List.of(new Cook(1)),
                List.of(cookie),
                "30 Rte des Colles, 06410 Biot",
                LocalTime.parse("08:00"),
                LocalTime.parse("20:00"),
                1,
                inventory
        );
        recipes.add(cookie);
        stores.add(store);
    }

    public void chooseAmount(int i, Cookie cookie, Cart cart){
        cart.addItem(new Item(i, cookie));
    }

    public String finalizeOrder(Client client, Store store){
        Cook cook = store.getFreeCook(client.getCart());
        Order order = new Order("order1", client, cook);
        client.emptyCart(order);
        this.orders.add(order);
        //cook.addOrder(order);         //Pas de temps de cuisson pour l'instant donc pas de TimeSlot
        return order.getId();
    }

    public void register(String id, String password, int phoneNumber){
        clients.add(new RegisteredClient(id, password, phoneNumber));
    }

    public void setStatus(String orderId, OrderStatus status) throws OrderException
    {
        Order order = orders.stream().filter(ord -> ord.getId().equals(orderId)).findFirst().orElse(null);
        assert order != null;
        order.setStatus(status);

    }
    public void setHours(Store store, LocalTime openingTime, LocalTime closingTime){
        if (openingTime.isBefore(closingTime)) {
            this.stores.get(this.stores.indexOf(store)).setHours(openingTime, closingTime);
        } else {
            System.out.println("Error, given closingTime is before openingTime, openingTime get closedTime value and closingTime get openingTime value");
            this.stores.get(this.stores.indexOf(store)).setHours(closingTime, openingTime);
        }
    }

    public void chooseCookie(Client client, Store store, Cookie cookie, int amount ) throws CookieException, BadQuantity {
        if (! store.getRecipes().contains(cookie))
        {
            throw new CookieException("this cookie is not available in this store");
        }
         int maxCookieAmount = store.getMaxCookieAmount(cookie);
        if (maxCookieAmount < amount)
        {
            throw new CookieException("this store can't make this amount of cookies");
        }

        client.getCart().addItem(new Item(amount, cookie));
        Inventory inventory = store.getInventory();

        inventory.decreaseIngredientQuantity(cookie.getDough(), amount);

        if (inventory.get(cookie.getDough()) == 0)
        {
            store.removeCookies(cookie.getDough());
        }

        inventory.decreaseIngredientQuantity(cookie.getFlavor(), amount);

        if (inventory.get(cookie.getFlavor()) == 0)
        {
            store.removeCookies(cookie.getFlavor());
        }

        for( Topping topping : cookie.getToppings())
        {
            inventory.decreaseIngredientQuantity(topping, amount);
            if (inventory.get(topping) == 0)
            {
                store.removeCookies(topping);
            }
        }

    }



    public void suggestRecipe(Cookie cookie){
        if(!suggestedRecipes.contains(cookie) && ! recipes.contains(cookie)){
            suggestedRecipes.add(cookie);
        }
    }
    public List<Cookie> getSuggestedRecipes(){
        return List.copyOf(suggestedRecipes);
    }

    public List<Cookie> getRecipes() {
        return recipes;
    }

    public List<Store> getStores() {
        return stores;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<RegisteredClient> getClients() {
        return List.copyOf(clients);
    }

    public void acceptRecipe(Cookie cookie,Double price){//TODO rajouter Exception si le cookie n'existe pas ? (tu peux utiliser cookieException (imene))

        if(suggestedRecipes.contains(cookie)){
            recipes.add(cookie);
            cookie.setPrice(price);
            suggestedRecipes.remove(cookie);
        }
    }
    public void declineRecipe(Cookie cookie) {
        if(suggestedRecipes.contains(cookie)){
            suggestedRecipes.remove(cookie);
        }
    }

    public void cancelOrder(Order order) throws OrderException {
        Order orderToCancel = orders.stream()
                .filter(o -> o.getId().equals(order.getId()))
                .findFirst()
                .orElseThrow();
        orderToCancel.setStatus(OrderStatus.CANCELLED);
    }

    public void printStoresOpeningHours(){
        for(Store store : stores){
            System.out.println(store.openingTime + " - " + store.closingTime);
        }
    }
}

