package fr.unice.polytech;

import fr.unice.polytech.client.Cart;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.order.Item;
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
    public List<Cookie> suggestedRecipes;
    public List<Store> stores;
    public List<Order> orders;

    public COD(){
        this.recipes = new ArrayList<>();
        this.stores = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.suggestedRecipes = new ArrayList<>();

        //Initialisation with 1 store + 1 recipe
        Cookie cookie = new Cookie(
                "ChocoCookie"
                );
        Store store = new Store(
                List.of(new Cook(1)),
                List.of(cookie),
                "30 Rte des Colles, 06410 Biot",
                new Date(),
                new Date()
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
        client.emptyCart();
        this.orders.add(order);
        //cook.addOrder(order);         //Pas de temps de cuisson pour l'instant donc pas de TimeSlot
        return order.getId();
    }

    public void setStatus(String orderId, OrderStatus status) throws OrderException
    {
        Order order = orders.stream().filter(ord -> ord.getId().equals(orderId)).findFirst().orElse(null);
        assert order != null;
        order.setStatus(status);

    }
    public void setHours(Store store, Date openingTime, Date closingTime){
        this.stores.get(this.stores.indexOf(store)).setHours(openingTime, closingTime);
    }
    public void suggestRecipe(Cookie cookie){
        if(!suggestedRecipes.contains(cookie) && ! recipes.contains(cookie)){
            suggestedRecipes.add(cookie);
        }
    }
    public List<Cookie> getSuggestedRecipes(){
        return suggestedRecipes;
    }
    public void acceptRecipe(Cookie cookie,Double price){//TODO rajouter Exception si le cookie n'existe pas ?

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
}

