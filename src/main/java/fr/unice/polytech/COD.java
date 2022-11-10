package fr.unice.polytech;
import fr.unice.polytech.client.Cart;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.RegisteredClient;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.recipe.*;
import fr.unice.polytech.services.PaymentService;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.Store;

import lombok.Getter;
import lombok.Setter;

import java.time.Clock;
import java.time.LocalTime;
import java.util.*;

public class COD {
    @Getter
    private final List<Cookie> recipes;
    @Getter
    private final List<Cookie> suggestedRecipes;
    @Getter
    private final List<RegisteredClient> connectedClients;

    @Getter
    private final List<Store> stores;
    @Getter
    private final List<Order> orders;
    @Getter
    private final List<RegisteredClient> clients;

    @Getter
    @Setter
    private static Clock CLOCK = Clock.systemDefaultZone();
    private int idCook = 0;
    private int idStore = 0;
    @Setter
    private LocationServer locationServer;



    public COD() {
        this.recipes = new ArrayList<>();
        this.stores = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.suggestedRecipes = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.connectedClients = new ArrayList<>();

        //Initialisation with 1 store + 1 recipe
        recipes.add(new Cookie(
                "chocolala",
                1.,
                15,
                Cooking.CHEWY,
                Mix.MIXED,
                new Dough("chocolate", 1),
                new Flavour("chocolate", 1),
                List.of(new Topping("chocolate chips", 1))
        ));
        addStore(2, "30 Rte des Colles, 06410 Biot", "08:00", "20:00", 10.3);
        locationServer = new LocationServer();
    }

    public String finalizeOrder(Client client, Store store) throws BadQuantityException, CookException, PaymentException {
        Cook cook = store.getFreeCook(client.getCart());
        Order order;
        String id = UUID.randomUUID().toString();
        order = new Order(id, client, cook, store);
        createOrderItem(client.getCart(), order);
        client.validateOrder(order);
        PaymentService.getInstance().performPayment(order.getPrice());
        client.getNotified(order, "Your order is paid");
        this.orders.add(order);
        cook.addOrder(order);
        return order.getId();
    }

    public void register(String id, String password, String phoneNumber) throws RegistrationException {
        if (clients.stream().anyMatch(client -> client.getId().equals(id)))
            throw new RegistrationException("User " + id + " is already registered.");
        clients.add(new RegisteredClient(id, password, phoneNumber));
    }

    public RegisteredClient logIn(String id, String password) throws InvalidInputException {
        if (connectedClients.stream().noneMatch(client -> client.getId().equals(id))) {
            Optional<RegisteredClient> registeredClient = (clients.stream().filter(client -> client.getId().equals(id)).findFirst());
            if (registeredClient.isPresent()) {
                RegisteredClient client = registeredClient.get();
                if (client.getPassword().equals(password)){
                    connectedClients.add(client);
                    return client;
                }
                else
                    throw new InvalidInputException("The password you entered is not valid. ");

            } else
                throw new InvalidInputException("ID not found. Please log in with another ID");
        }
        throw new InvalidInputException("You are already connected ");
    }


    public void setStatus(Order order, OrderStatus status) throws OrderException {
        order.setStatus(status);
    }

    public void setHours(Store store, LocalTime openingTime, LocalTime closingTime) {
        if (openingTime.isBefore(closingTime)) {
            this.stores.get(this.stores.indexOf(store)).setHours(openingTime, closingTime);
        } else {
            System.err.println("Error, given closingTime is before openingTime, openingTime get closedTime value and closingTime get openingTime value");
            this.stores.get(this.stores.indexOf(store)).setHours(closingTime, openingTime);
        }
    }

    public void chooseCookie(Client client, Store store, String cookieName, int amount) throws CookieException, OrderException {
        Optional<Cookie> cookie = (store.getRecipes().stream().filter(c -> (c.getName().equals(cookieName))).findFirst());
        if(cookie.isEmpty())
            throw new CookieException("The cookie "+cookieName+" does not exist.");
        chooseCookie(client, store, cookie.get(), amount);
    }

    public void chooseCookie(Client client, Store store, Cookie cookie, int amount) throws CookieException, OrderException {

        if (client instanceof RegisteredClient && ((RegisteredClient) client).isBanned())
            throw new OrderException("You have cancelled two orders in 8 minutes or less, you are banned for 10 minutes.\n Remaining time : " + ((RegisteredClient) client).getRemainingBanTime());

        if (!store.getRecipes().contains(cookie)) {
            throw new CookieException("this cookie is not available in this store");
        }
        int maxCookieAmount = store.getMaxCookieAmount(cookie);
        if (maxCookieAmount < amount) {
            throw new CookieException("this store can't make this amount of cookies");
        }
        Cart cart= client.getCart();
        if(cart.getTax() == null && store.getTax()!=null ){
            cart.setTax(store.getTax());
        }
        cart.addItem(new Item(amount, cookie));

    }


    public void suggestRecipe(Cookie cookie) {
        if (!suggestedRecipes.contains(cookie) && !recipes.contains(cookie)) {
            suggestedRecipes.add(cookie);
        }
    }

    public void acceptRecipe(Cookie cookie, Double price) {//TODO rajouter Exception si le cookie n'existe pas ? (tu peux utiliser cookieException (imene))

        if (suggestedRecipes.contains(cookie)) {
            recipes.add(cookie);
            cookie.setPrice(price);
            suggestedRecipes.remove(cookie);
        }
    }

    public void declineRecipe(Cookie cookie) {
        suggestedRecipes.remove(cookie);
    }

    public void cancelOrder(String idOrder) throws OrderException {
        Optional<Order> order = (orders.stream().filter(o -> (o.getId().equals(idOrder))).findFirst());
        if(order.isPresent())
            cancelOrder(order.get());
        else
            throw new OrderException("The order "+idOrder+" does not exist.");
    }

    public void cancelOrder(Order order) throws OrderException {
        order.setStatus(OrderStatus.CANCELLED);
        order.getCook().cancelOrder(order);
        Store store = order.store;
        for (Item item : order.getItems()) {
            Cookie cookie = item.getCookie();
            int numberOfCookie = item.getQuantity();
            store.getInventory().addIngredient(item.getCookie().getDough(),  numberOfCookie);
            store.getInventory().addIngredient(item.getCookie().getFlavour(), numberOfCookie);
            //topping
            cookie.getToppings().forEach(topping -> store.getInventory().addIngredient(topping, numberOfCookie));
        }
    }

    public void printStoresOpeningHours() {
        for (Store store : stores) {
            System.out.println(store.openingTime + " - " + store.closingTime);
        }
    }
    public void setTax(Store store, double tax){
        store.setTax(tax);
    }

    public List<Order> getClientPastOrders(RegisteredClient client) {
        return client.getPastOrders();
    }

    public String payOrder(Client client, Store store) throws BadQuantityException, CookException, PaymentException {
        return finalizeOrder(client, store);
    }


    private void createOrderItem(Cart cart, Order order) throws BadQuantityException {
        for (Item item : cart.getItems()) {
            Cookie cookie = item.getCookie();
            int numberOfCookie = item.getQuantity();
            order.store.getInventory().decreaseIngredientQuantity(item.getCookie().getDough(), numberOfCookie);
            order.store.getInventory().decreaseIngredientQuantity(item.getCookie().getFlavour(), numberOfCookie);
            for (Topping topping : cookie.getToppings()) {
                order.store.getInventory().decreaseIngredientQuantity(topping, numberOfCookie);
            }
        }
        if (order.getClient() instanceof RegisteredClient) {
            if (((RegisteredClient) order.getClient()).isEligibleForDiscount()) {
                order.setPrice(order.getPrice()*0.9);
            }
        }
    }
    public void addStore(Store store) {
        stores.add(store);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public Order getOrder(String id) throws OrderException {
        for (Order order : orders) {
            if (order.getId().equals(String.valueOf(id))) {
                return order;
            }
        }
        throw new OrderException("Order not found");
    }

    /**
     * Returns the list of possible pickup times for the given cart and store
     *
     * @param cart  the cart to be ordered
     * @param store the store where the order will be picked up
     * @return a list of possible pickup times
     */
    public List<LocalTime> getPickupTimes(Cart cart, Store store) {
        return store.getPossiblePickupTimes(cart);
    }

    /**
     * Sets the desired pickup time on the given cart
     *
     * @param cart       the cart to set the pickup time on
     * @param store      the store where the order will be picked up
     * @param pickupTime the desired pickup time
     */
    public void choosePickupTime(Cart cart, Store store, LocalTime pickupTime) throws InvalidPickupTimeException {
        List<LocalTime> validPickupTimes = getPickupTimes(cart, store);
        if (!validPickupTimes.contains(pickupTime)) {
            throw new InvalidPickupTimeException(pickupTime);
        }
        cart.setPickupTime(pickupTime);
    }

    public void addStore(int nbCooks, String address, String openingTime, String endingTime, double tax){
        List<Cook> cooks = new ArrayList<>();
        for (int i =0; i<nbCooks; i++)
            cooks.add(new Cook(idCook++));
        stores.add(new Store(cooks, List.copyOf(recipes), address, LocalTime.parse(openingTime), LocalTime.parse(endingTime), idStore++, new Inventory(new ArrayList<>()),tax));
    }

    public void addCook(int idStore) throws StoreException {
        getStore(idStore).addCook(new Cook(idCook++));
    }

    public Store getStore(int idStore) throws StoreException {
        Optional<Store> store = (stores.stream().filter(s -> (s.getId()==idStore)).findFirst());
        if(store.isPresent())
            return store.get();
        else
            throw new StoreException("The store "+idStore+" does not exist.");
    }

    public List<Store> getNearbyStores(String address)  {
        List<Store> nearbyStores = new ArrayList<>();

        for (Store store : stores) {
            String storeAddress = store.getAddress();
            double distance = locationServer.distance(address, storeAddress);
            if (distance <= 3) {
                nearbyStores.add(store);
            }
        }
        return nearbyStores;

    }

    public List<Store> getNearbyStores(String address, int proximity, String unit)
    {
        List<Store> nearbyStores = new ArrayList<>();

        for (Store store : stores) {
            String storeAddress = store.getAddress();
            double distance = locationServer.distance(address,storeAddress,unit);
            if (distance <= proximity) {
                nearbyStores.add(store);
            }
        }
        return nearbyStores;
    }

}


