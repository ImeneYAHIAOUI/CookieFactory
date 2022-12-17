package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.*;

import java.util.UUID;

public interface OrderUpdater {
    UUID finalizeOrder(Client client, Store store) throws BadQuantityException, CookException, IngredientTypeException;
    void removeIngredientsFromInventory(int numberOfCookie, Cookie cookie, Store store)
            throws BadQuantityException, IngredientTypeException;
    void addOrder(Order order);
    void cancelOrder(Order order) throws OrderException, BadQuantityException, CatalogException,
            OrderStateException;
    void cancelOrder(UUID idOrder) throws OrderException, BadQuantityException, CatalogException, OrderStateException;
    void setStatus(Order order, OrderStatus status) throws OrderException, OrderStateException;

}
