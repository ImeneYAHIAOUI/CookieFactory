package fr.unice.polytech.cod;

import fr.unice.polytech.client.Cart;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.RegisteredClient;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.order.Item;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.recipe.*;
import fr.unice.polytech.services.LocationService;
import fr.unice.polytech.services.PaymentService;
import fr.unice.polytech.services.StatusScheduler;
import fr.unice.polytech.store.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class COD {
    private static COD INSTANCE = null;
    @Getter
    private final List<Cookie> recipes;
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
    private LocationService locationService;

    private final Catalog catalog;

     COD() {
        this.recipes = new ArrayList<>();
        this.stores = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.suggestedRecipes = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.connectedClients = new ArrayList<>();
        this.catalog = new Catalog();
        locationService = new LocationService();
    }

    public static COD getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new COD();
        }
        return INSTANCE;
    }

    public static void reset() {
        INSTANCE = null;
    }

    /**
     * Initialization of the COD with 1 store, 1 recipe and 1 occasion
     */
    public void initializationCod() throws CookieException {
        //Initialisation with 1 store + 1 recipe
        recipes.add(CookieFactory.createSimpleCookie(
                "chocolala",
                1.,
                15,
                Cooking.CHEWY,
                Mix.MIXED,
                new Dough("chocolate", 1),
                new Flavour("chocolate", 1),
                List.of(new Topping("chocolate chips", 1))
        ));
        locationService = new LocationService();

        addStore(2, "30 Rte des Colles, 06410 Biot", "08:00",

                "20:00",10.3,List.of(Occasion.BIRTHDAY));

    }

    /**
     * Allow to create the order if a Cook is available in the chosen store then call method to pay the order
     * @param client the client who wants to pay
     * @param store the store where the client wants to get his order
     * @return the order ID of the client
     * @throws BadQuantityException if the quantity of the item is not valid
     * @throws CookException if the cook is not available
     */
    public String finalizeOrder(Client client, Store store) throws BadQuantityException, CookException {
        Cook cook = store.getFreeCook(client.getCart());
        String id = UUID.randomUUID().toString();
        Order order = new Order(id, client, cook, store);
        createOrderItem(client.getCart(), order);
        client.validateOrder(order);
        PaymentService.getInstance().performPayment(order.getPrice());
        client.getNotified(order, "Your order is paid");
        this.orders.add(order);
        cook.addOrder(order);
        return order.getId();
    }

    /**
     * Register a new Client
     * @param id chosen by the client
     * @param password chosen by the client
     * @param phoneNumber chosen by the client
     * @throws RegistrationException if user already registered
     * @throws InvalidPhoneNumberException if phone number invalid
     */
    public void register(String id, String password, String phoneNumber) throws RegistrationException, InvalidPhoneNumberException {
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
        StatusScheduler.getInstance().setStatus(order, status);
    }

    public void setHours(Store store, LocalTime openingTime, LocalTime closingTime) {
        if (openingTime.isBefore(closingTime)) {
            this.stores.get(this.stores.indexOf(store)).setHours(openingTime, closingTime);
        } else {
            System.err.println("Error, given closingTime is before openingTime, openingTime get closedTime value and closingTime get openingTime value");
            this.stores.get(this.stores.indexOf(store)).setHours(closingTime, openingTime);
        }
    }

    /**
     * Check if the cookie exist before adding the cookie in the cart
     * @param client the client ordering
     * @param store the store chosen by the client
     * @param cookieName name of the cookie to add
     * @param amount amount of cookie to add
     * @throws CookieException if the Cookie does not exist
     * @throws OrderException if 2 cancelled orders in less than 8 minutes
     */
    public void chooseCookie(Client client, Store store, String cookieName, int amount) throws CookieException, OrderException {
        Optional<Cookie> cookie = (store.getRecipes().stream().filter(c -> (c.getName().equals(cookieName))).findFirst());
        if(cookie.isEmpty())
            throw new CookieException("The cookie "+cookieName+" does not exist.");
        chooseCookie(client, store, cookie.get(), amount);
    }

    public void chooseCookie(Client client, Store store, Cookie cookie, int amount) throws CookieException, OrderException {

        if (client instanceof RegisteredClient && ((RegisteredClient) client).isBanned())
            throw new OrderException("You have cancelled two orders in 8 minutes or less, you are banned for 10 minutes.\n Remaining time : " + ((RegisteredClient) client).getRemainingBanTime());

        if ( ! (cookie instanceof PartyCookie) && (cookie==null || ! store.getRecipes().contains(cookie))) {
            throw new CookieException("this cookie is not available in this store");
        }
        int maxCookieAmount = calculateMaxCookieAmount(cookie,store);
        if (maxCookieAmount < amount) {
            throw new CookieException("This store can't make this amount of cookies");
        }
        Cart cart= client.getCart();
        if(cart.getTax() == null && store.getTax()!=null ){
            cart.setTax(store.getTax());
        }
        cart.addItem(new Item(amount, cookie));
    }

    /**
     * personalize cookie
     * @param client ordering the cookie
     * @param store of the order
     * @param cookie chosen cookie

     * @param client the client ordering
     * @param store the store chosen by the client
     * @param cookie chosen by the client
     * @param amount amount of cookie chosen by the client
     * @param size size of the cookie chosen by the client
     * @param occasion occasion chosen by the client
     * @param theme theme chosen by the client
     * @throws CookieException
     * @throws OrderException
     * @throws ServiceNotAvailable
     */
    public void personalizeCookieFromBaseRecipe(Client client, Store store, Cookie cookie,int amount,CookieSize size, Occasion occasion, Theme theme, List<Ingredient> addedIngredients, List<Ingredient> removedIngredients) throws CookieException, OrderException, ServiceNotAvailable{
        //Optional<Cookie> cookie = (store.getRecipes().stream().filter(c -> (c.getName().equals(cookieName))).findFirst());
        if(! store.getRecipes().contains(cookie))
            throw new CookieException("The cookie "+cookie.getName()+" does not exist.");
        List<Theme> themeList=store.getThemeList();
        List<Occasion> occasionList=store.getOccasionList();
        if(themeList.contains(theme)&& occasionList.contains(occasion)){
            PartyCookieWithBaseRecipe partyCookie=CookieFactory.createPartyCookieWithBaseCookie(cookie,size,theme,addedIngredients,removedIngredients);
            chooseCookie(client,store,partyCookie,amount);

        }else
            throw new ServiceNotAvailable();
    }

    public void personalizeCookieFromScratch(Client client, Store store, String cookieName, Cooking cooking, Mix mix, Dough dough, Flavour flavour, List<Topping> toppings, int amount, CookieSize size, Occasion occasion, Theme theme, List<Ingredient> SupplementaryIngredients) throws CookieException, OrderException, ServiceNotAvailable {
        List<Theme> themeList=store.getThemeList();
        List<Occasion> occasionList=store.getOccasionList();
        if(themeList.contains(theme)&& occasionList.contains(occasion)){
            PartyCookieFromScratch partyCookie=CookieFactory.createPartyCookieFromScratch(size,theme,cookieName, cooking,mix,dough,flavour,toppings,SupplementaryIngredients);
            chooseCookie(client,store,partyCookie,amount);
        }
        else throw new ServiceNotAvailable();
    }



    /**
     * add occasion to store
     * @param store to add the occasion
     * @param occasion to add
     */
    public void addOccasion(Store store, Occasion occasion){
        store.addOccasion(occasion);
    }

    public void addTheme(Store cook, Theme theme){
        cook.addTheme(theme);
    }

    /**
     * Calls the right method to calculate the max amount of cookies that can be made in the store
     * @param cookie the chosen cookie
     * @param store the chosen store
     * @return the maximum amount of cookies that can be made in the store
     */
    public int calculateMaxCookieAmount(Cookie cookie,Store store){
        if (cookie.getClass() == PartyCookie.class){
            return choosePartyCookie(store,(PartyCookie) cookie);
        }
        else {
            return store.getMaxCookieAmount(cookie,1);
        }
    }

    /**
     * Check if the ingredients of the recipes are in the catalog and if they are of the right Ingredient Type
     * before adding the recipe in the suggestedRecipes List
     * @param name of the new recipe
     * @param price of the new recipe
     * @param time to cook the recipe
     * @param cooking_chosen Cooking of the recipe
     * @param mix_chosen Mix of the recipe
     * @param doughIngredient Dough of the recipe
     * @param flavourIngredient Flavour of the recipe
     * @param toppings Toppings of the recipe
     * @throws CatalogException if one of the ingredients does not exist
     */
    public void suggestRecipe(String name, double price, int time, Cooking cooking_chosen, Mix mix_chosen, Ingredient doughIngredient, Ingredient flavourIngredient,List<Ingredient> toppings) throws CatalogException, CookieException {
        List<Topping> toppingList = new ArrayList<>();
        for (Ingredient i: toppings) {
            if(i.getIngredientType().equals(IngredientType.TOPPING)
                    && i.equals(catalog.getIngredient(i.getName()))
            )
                toppingList.add((Topping) i);
            else
                throw new CatalogException("Bad Ingredient Type");
        }
        if(doughIngredient.getIngredientType().equals(IngredientType.DOUGH)
                && flavourIngredient.getIngredientType().equals(IngredientType.FLAVOUR)
                && doughIngredient.equals(catalog.getIngredient(doughIngredient.getName()))
                && flavourIngredient.equals(catalog.getIngredient(flavourIngredient.getName()))
        )
            suggestRecipe(CookieFactory.createSimpleCookie(name, price, time, cooking_chosen, mix_chosen, (Dough) doughIngredient, (Flavour) flavourIngredient, toppingList));
        else
            throw new CatalogException("Bad Ingredient Type");
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
            for (Store s: this.stores) {
                if(s.canAddCookieToStore(cookie))
                    s.addCookies(List.of(cookie));
            }
        }
    }

    public void declineRecipe(Cookie cookie) {
        suggestedRecipes.remove(cookie);
    }

    /**
     * Check if the Order exists before cancelling the order
     * @param idOrder which we want the Order
     * @throws OrderException of the Order does not exist
     */
    public void cancelOrder(String idOrder) throws OrderException, BadQuantityException {
        Optional<Order> order = (orders.stream().filter(o -> (o.getId().equals(idOrder))).findFirst());
        if(order.isPresent())
            cancelOrder(order.get());
        else
            throw new OrderException("The order "+idOrder+" does not exist.");
    }

    /**
     * Set order status to CANCELLED, cancel the cook and call method to put back the ingredients in the store
     * @param order the order to cancel
     * @throws OrderException if the order does not exist
     */
    public void cancelOrder(Order order) throws OrderException, BadQuantityException {
        order.setStatus(OrderStatus.CANCELLED);
        order.getCook().cancelOrder(order);
        Store store = order.store;
        putBackIngredientsInInventory(order.getItems(), store);
    }

    /**
     * Put back the ingredients in the store inventory
     * @param items the items of the cancelled order
     * @param store the store where the order was made
     */
    public static void putBackIngredientsInInventory(List<Item> items, Store store) throws BadQuantityException {
        for (Item item : items) {
            Cookie cookie = item.getCookie();
            int numberOfCookie = item.getQuantity();
            store.addIngredients(item.getCookie().getDough(),  numberOfCookie);
            store.addIngredients(item.getCookie().getFlavour(), numberOfCookie);
            //topping
            cookie.getToppings().forEach(topping -> {
                try {
                    store.addIngredients(topping, numberOfCookie);
                } catch (BadQuantityException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /**
     * Fill the inventory of the store with the chosen ingredient and the chosen amount
     * Check if we can add some new recipes in the store
     * @param s chosen Store
     * @param ingredient name of the ingredient
     * @param amount to add in th store
     * @throws BadQuantityException if the Quantity of Ingredient become negative
     * @throws CatalogException if the Ingredient does not exist
     */
    public void addInventory(Store s, String ingredient, int amount) throws BadQuantityException, CatalogException {
        s.addIngredients(getIngredientCatalog(ingredient), amount);
        for (Cookie c: this.recipes) {
            if(!s.getRecipes().contains(c) && s.canAddCookieToStore(c))
                s.addCookies(List.of(c));
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

    /**
     *
     * @param client the client who wants to pay his current order
     * @param store the store chosen by the client
     * @return the ID of the order
     * @throws BadQuantityException if not enough ingredients in the store's inventory
     * @throws CookException if no cook is not available
     */
    public String payOrder(Client client, Store store) throws BadQuantityException, CookException {
        return finalizeOrder(client, store);
    }

    /**
     * Add cookie to the order and modify the price if the client is a registered client and eligible for a discount
     * @param cart the cart of the client
     * @param order the order we are creating
     * @throws BadQuantityException if the quantity of an ingredient is not enough
     */

    private void createOrderItem(Cart cart, Order order) throws BadQuantityException {
        for (Item item : cart.getItems()) {
            Cookie cookie = item.getCookie();
            addCookieInOrder(item.getQuantity(),cookie,order.getStore());
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
     * Returns the list of possible pickup times for the given cart and store for a given date
     * @param cart the cart to be ordered
     * @param store the store where the order will be picked up
     * @param date the date for which we want the pickup times
     * @return a list of possible pickup times
     */
    public List<LocalDateTime> getPickupTimesForAnotherDate(Cart cart, Store store, LocalDate date) throws InvalidPickupTimeException {
        if(date.isAfter(LocalDate.now(COD.CLOCK))) {
            return store.getPossiblePickupTimesForADate(cart, date);
        }
        throw new InvalidPickupTimeException("The date must be after today");
    }

    /**
     * Sets the desired pickup time on the given cart
     *
     * @param cart       the cart to set the pickup time on
     * @param store the store where the order will be picked up
     * @param pickupTime the desired pickup time
     */
    public void choosePickupTime(Cart cart, Store store, LocalTime pickupTime) throws InvalidPickupTimeException {
        List<LocalTime> validPickupTimes = getPickupTimes(cart, store);
        if (!validPickupTimes.contains(pickupTime)) {
            throw new InvalidPickupTimeException(pickupTime);
        }
        cart.setPickupTime(pickupTime.atDate(LocalDate.now(CLOCK)));
    }

    /**
     * Sets the desired pickup time on the given cart for a given date
     * @param cart the cart to set the pickup time on
     * @param store the store where the order will be picked up
     * @param pickupTime the desired pickup time
     */
    public void  choosePickupTimeAtAnotherDate(Cart cart, Store store, LocalDateTime pickupTime) throws InvalidPickupTimeException {
        List<LocalDateTime> validPickupTimes = getPickupTimesForAnotherDate(cart, store, pickupTime.toLocalDate());
        if (!validPickupTimes.contains(pickupTime)) {
            throw new InvalidPickupTimeException(pickupTime.toLocalTime());
        }
        cart.setPickupTime(pickupTime);
    }


    /**
     * add a store in Cod
     * @param nbCooks nb cooks in the store
     * @param address of the store
     * @param openingTime of the store
     * @param endingTime of the store
     * @param tax of the store
     * @param occasions occasions available in the store
     */
    public void addStore(int nbCooks, String address, String openingTime, String endingTime, double tax,List<Occasion>occasions){
        List<Cook> cooks = new ArrayList<>();
        for (int i =0; i<nbCooks; i++)
            cooks.add(new Cook(idCook++));
        stores.add(StoreFactory.createStore(cooks, List.copyOf(recipes), address, LocalTime.parse(openingTime), LocalTime.parse(endingTime), idStore++, new Inventory(new ArrayList<>()), tax, occasions));
    }

    /**
     * Add a cook to the chosen store
     * @param idStore id Of the store we want to add a cook to
     * @throws StoreException if the Sotre does not exist
     */
    public void addCook(int idStore) throws StoreException {
        getStore(idStore).addCook(new Cook(idCook++));
    }

    /**
     * Return the store with the chosen idStore
     * @param idStore which we want the Store
     * @return the Store
     * @throws StoreException if the store does not exist
     */
    public Store getStore(int idStore) throws StoreException {
        Optional<Store> store = (stores.stream().filter(s -> (s.getId()==idStore)).findFirst());
        if(store.isPresent())
            return store.get();
        else
            throw new StoreException("The store "+idStore+" does not exist.");
    }

    public List<Store> getNearbyStores(String address) throws IOException {
        List<Store> nearbyStores = new ArrayList<>();

        for (Store store : stores) {
            String storeAddress = store.getAddress();
            double distance = locationService.distance(address, storeAddress);
            if (distance <= 3) {
                nearbyStores.add(store);
            }
        }
        return nearbyStores;

    }

    public List<Store> getNearbyStores(String address, int proximity, String unit) throws IOException {
        List<Store> nearbyStores = new ArrayList<>();

        for (Store store : stores) {
            String storeAddress = store.getAddress();
            double distance = locationService.distance(address,storeAddress,unit);
            if (distance <= proximity) {
                nearbyStores.add(store);
            }
        }
        return nearbyStores;
    }

    /**
     * Add a new ingredient to catalog
     * @param name of the ingredient
     * @param price of the ingredient
     * @param ingredientType of the ingredient
     * @throws CatalogException if an ingredient of the same name already exists
     */
    public void addIngredientCatalog(String name, double price, IngredientType ingredientType) throws CatalogException, IngredientTypeException {
        catalog.addIngredient(name, price, ingredientType);
    }

    /**
     * Return the ingredient with the chosen name
     * @param name of the ingredient
     * @return the ingredient
     * @throws CatalogException if none of the ingredients in Catalog have this name
     */
    public Ingredient getIngredientCatalog(String name) throws CatalogException {
        return catalog.getIngredient(name);
    }

    public void printStores(){
        System.out.println("Stores:");
        for (Store s: this.stores) {
            System.out.println(s);
        }
    }

    public void printRecipes(){
        System.out.println("Recipes:");
        for (Cookie c: this.recipes) {
            System.out.println(c);
        }
    }

    public void printRecipes(Store s){
        System.out.println("Recipes Store "+s);
        for (Cookie c: s.getRecipes()) {
            System.out.println(c);
        }
    }

    public void printCatalog(){
        System.out.println(catalog);
    }

    /**
     * Add cookie to the order if all ingredients are available
     * the bigger the cookie is, the more ingredients you'll need
     * @param numberOfCookie the number of this type of cookie in the cart
     * @param cookie the cookie to add
     * @param store the store to add the cookie to
     * @throws BadQuantityException if the quantity of ingredients to make the cookie is not available
     */
    public void addCookieInOrder(int numberOfCookie, Cookie cookie, Store store) throws BadQuantityException {
        int ingredientsToDecreaseDueToSize =1 ;
        if (cookie.getSize() != null) {
            switch (cookie.getSize()) {
                case L -> ingredientsToDecreaseDueToSize = 2;
                case XL -> ingredientsToDecreaseDueToSize = 3;
                case XXL -> ingredientsToDecreaseDueToSize = 4;
            }
        }
        store.getInventory().decreaseIngredientQuantity(cookie.getDough(), numberOfCookie * ingredientsToDecreaseDueToSize);
        store.getInventory().decreaseIngredientQuantity(cookie.getFlavour(), numberOfCookie * ingredientsToDecreaseDueToSize);
        for (Topping topping : cookie.getToppings()) {
            store.getInventory().decreaseIngredientQuantity(topping, numberOfCookie * ingredientsToDecreaseDueToSize);
        }
    }

    /**
     *
     * @param store the chosen store
     * @param cookie the chosen type of PartyCookie
     * @return the number of this type of Party Cookie you can make with the ingredients available
     */
    public int choosePartyCookie(Store store, PartyCookie cookie) {
        int amountFactor=1;
        switch (cookie.getSize()){
            case L -> amountFactor = 2;
            case XL -> amountFactor = 3;
            case XXL -> amountFactor = 4;
        }
        return store.getMaxCookieAmount(cookie,amountFactor);
    }

    public List<Cookie> getSuggestedRecipes(){
        return List.copyOf(this.suggestedRecipes);
    }

    public void addClientToGoodToGo(Client client, String mail, List<LocalDateTime> list) throws ClientException {
        client.addToGoodToGo(mail, list);
    }

    public List<RegisteredClient> getTooGooToGoClients(){
        return getClients().stream().filter(RegisteredClient::isToGoodToGoClient).toList();
    }
}