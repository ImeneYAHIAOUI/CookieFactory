package fr.unice.polytech;

import fr.unice.polytech.client.RegistredClient;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderException;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.recipe.*;
import fr.unice.polytech.store.Cook;
import fr.unice.polytech.store.Store;
import fr.unice.polytech.store.TimeSlot;

import java.util.*;

public class COD {
    public List<Cookie> recipes;
    public List<RegistredClient> clients;
    public List<Store> stores;
    public List<Order> orders;

    public COD(){
        this.recipes = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.stores = new ArrayList<>();
        this.orders = new ArrayList<>();

        //Initialisation with 1 store + 1 recipe
        Cookie cookie = new Cookie(
                "ChocoCookie",
                10.0,
                40.0,
                Cooking.CHEWY,
                Mix.MIXED,
                new Dough("Chocolate", 2.0),
                new Flavour("Cinnamon", 1.5),
                List.of(new Topping("White chocolate", 0.5), new Topping("Milk Chocolate", 1.0))
        );
        Store store = new Store(
                List.of(new Cook()),
                List.of(cookie),
                "30 Rte des Colles, 06410 Biot",
                0.2,
                new Date(),
                new Date()
        );
        recipes.add(cookie);
        stores.add(store);
    }
    public List<Store> getNearbyStores(){
        return null;
    }
    public void setChosenStore(Store store){

    }
    public List<Cookie> getRecipes(Store store){
        return null;
    }
    public void setStatus(String orderId, OrderStatus status) throws OrderException
    {
        Order order = orders.stream().filter(ord -> ord.getId().equals(orderId)).findFirst().orElse(null);
        assert order != null;
        order.setStatus(status);

    }
    public void setHours(Store store, Date openningTime, Date closingTime){

    }
    public void setTax(Store store , Double tax){

    }
    public void ChooseCookie(Cookie cookie){

    }
    public List<TimeSlot> getFreeTimeSlots(Store store, Order order){
        return null;
    }
    public void registerOrder(){

    }
    public void finaliseOrder(){

    }
    public void payOrder(){

    }
    public  void suggestRecipe(Cookie cookie){

    }
    public List<Cookie> getSuggestedRecipes(){
        return null;
    }
    public void acceptRecipe(Cookie cookie){

    }
    public void declineRecipe(Cookie cookie) {

    }
    public void deleteRecipe(Cookie cookie){

    }
    public void register(String id,int phoneNumber){

    }
    public void authenticate(String id, int phoneNumber){

    }
    public List<Order> getPastOrders(RegistredClient client) {
        return null;
    }
    public void fillInventory(Store store, List<Ingredient> ingredients){

    }
}

