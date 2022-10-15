package fr.unice.polytech;

import fr.unice.polytech.Client.RegistredClient;
import fr.unice.polytech.Order.Order;
import fr.unice.polytech.Order.OrderException;
import fr.unice.polytech.Order.OrderStatus;
import fr.unice.polytech.Recipe.Cookie;
import fr.unice.polytech.Recipe.Ingredient;
import fr.unice.polytech.Store.Store;
import fr.unice.polytech.Store.TimeSlot;

import java.util.Date;
import java.util.List;

public class COD {
    public List<Cookie> recipes;
    public List<RegistredClient> clients;
    public List<Store> stores;
    public List<Order> orders;
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

