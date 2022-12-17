package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Cart;
import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.order.OrderStatus;
import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.cookies.PartyCookie;
import fr.unice.polytech.entities.recipe.enums.Theme;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.store.TimeSlot;
import fr.unice.polytech.exception.*;
import fr.unice.polytech.interfaces.*;
import fr.unice.polytech.repositories.OrderRepository;
import fr.unice.polytech.services.PaymentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderManager implements OrderFinder, OrderUpdater {
    @Getter
    private final OrderRepository orderRepository;
    private final InventoryUpdater inventoryManager;
    private final NotificationHandler notification;
    private final CartHandler cartManager;

    private final static int LOYALTY = 30;
    private final static double LOYALTY_REDUCTION = 0.9;

    /**
     * Allow to create the order if a Cook is available in the chosen store then call method to pay the order
     *
     * @param client the client who wants to pay
     * @param store  the store where the client wants to get his order
     * @return the order ID of the client
     * @throws BadQuantityException if the quantity of the item is not valid
     * @throws CookException        if the cook is not available
     */
    @Override
    public UUID finalizeOrder(Client client, Store store)
            throws BadQuantityException, CookException
    {
        Cook cook = getFreeCook(client.getCart(), store);
        UUID id;
        do {
            id = UUID.randomUUID();
        } while (orderRepository.existsById(id));
        Order order = new Order(id, client, cook, store, cartManager.totalCookingTime(client.getCart()));
        transferItemsToOrder(client.getCart(), order);
        validateOrder(client, order);
        PaymentService.getInstance().performPayment(order.getPrice());
        client.getNotified(order, "Your order is paid");
        addOrder(order);
        cook.addOrder(order);
        return order.getId();
    }

    private void validateOrder(Client client , Order order) {
        if(client instanceof RegisteredClient ){
            int nb = 0;
            for (Item item : client.getCart().getItems()) {
                nb += item.getQuantity();
            }
            ((RegisteredClient) client).setEligibleForDiscount(((RegisteredClient) client).getNbCookie() < LOYALTY && ((RegisteredClient) client).getNbCookie() + nb >= LOYALTY);
            ((RegisteredClient) client).setNbCookie(((RegisteredClient) client).getNbCookie()+nb);
            ((RegisteredClient) client).getPastOrders().add(order);
        }
        cartManager.clearCart(client.getCart());
    }

    /**
     * Set order status to order CANCELLED, cancel the cook and call method to put back the ingredients in the store
     *
     * @param order the order to cancel
     * @throws OrderException if the order does not exist
     */
    @Override
    public void cancelOrder(Order order)
            throws OrderException, BadQuantityException, CatalogException, OrderStateException {
        setStatus(order, OrderStatus.CANCELLED);
        order.getCook().cancelOrder(order);
        Store store = order.store;
        inventoryManager.putBackIngredientsInInventory(order.getItems(), store);
    }

    @Override
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        for (Order c : orderRepository.findAll()) {
            orders.add(c);
        }
        return orders;
    }

    /**
     * Check if the Order exists before cancelling the order
     *
     * @param idOrder which we want the Order
     * @throws OrderException of the Order does not exist
     */
    @Override
    public void cancelOrder(UUID idOrder)
            throws OrderException, BadQuantityException, CatalogException, OrderStateException {
        Optional<Order> order = StreamSupport
                .stream(orderRepository.findAll().spliterator(), false)
                .filter(o -> (o.getId().equals(idOrder)))
                .findFirst();
        if (order.isPresent())
            cancelOrder(order.get());
        else
            throw new OrderException("The order " + idOrder + " does not exist.");
    }

    /**
     * Check status change consistency and update the order status
     *
     * @param order  the order to update
     * @param status the new status
     */
    @Override
    public void setStatus(Order order, OrderStatus status) throws OrderStateException {
        order.setState(status);
        if (status.equals(OrderStatus.READY)) {
            notification.statusSchedulerTask(order);
        }
    }

    /**
     * Add cookie to the order if all ingredients are available
     * the bigger the cookie is, the more ingredients you'll need
     *
     * @param numberOfCookie the number of this type of cookie in the cart
     * @param cookie         the cookie to add
     * @param store          the store to add the cookie to
     * @throws BadQuantityException if the quantity of ingredients to make the cookie is not available
     */
    @Override
    public void removeIngredientsFromInventory(
            int numberOfCookie,
            Cookie cookie,
            Store store
    ) throws BadQuantityException
    {
        int ingredientsToDecreaseDueToSize = 1;
        if (cookie.getSize() != null) {
            switch (cookie.getSize()) {
                case L -> ingredientsToDecreaseDueToSize = 2;
                case XL -> ingredientsToDecreaseDueToSize = 3;
                case XXL -> ingredientsToDecreaseDueToSize = 4;
            }
        }

        inventoryManager.decreaseIngredientQuantity(
                cookie.getDough(),
                numberOfCookie * ingredientsToDecreaseDueToSize,
                store.getInventory()
        );
        inventoryManager.decreaseIngredientQuantity(
                cookie.getFlavour(),
                numberOfCookie * ingredientsToDecreaseDueToSize,
                store.getInventory()
        );
        for (Topping topping : cookie.getToppings()) {
            inventoryManager.decreaseIngredientQuantity(
                    topping,
                    numberOfCookie * ingredientsToDecreaseDueToSize,
                    store.getInventory()
            );
        }
    }

    @Override
    public void addOrder(Order order) {
        orderRepository.save(order, order.getId());
    }


    /**
     * Add cookie to the order and modify the price if the client is a registered client and eligible for a discount
     *
     * @param cart  the cart of the client
     * @param order the order we are creating
     * @throws BadQuantityException if the quantity of an ingredient is not enough
     */
    private void transferItemsToOrder(Cart cart, Order order) throws BadQuantityException {
        for (Item item : cart.getItems()) {
            Cookie cookie = item.getCookie();
            removeIngredientsFromInventory(item.getQuantity(), cookie, order.getStore());
        }
        if (order.getClient() instanceof RegisteredClient) {
            if (((RegisteredClient) order.getClient()).isEligibleForDiscount()) {
                order.setPrice(order.getPrice() * LOYALTY_REDUCTION);
            }
        }
    }

    private Cook getFreeCook(Cart cart, Store store) throws CookException {
        TimeSlot orderTimeSlot = new TimeSlot(
                cart.getPickupTime().minus(cartManager.totalCookingTime(cart)),
                cart.getPickupTime()
        );
        List<Theme> themes = new ArrayList<>();
        for (Item item : cart.getItems()) {
            if (item.getCookie() instanceof PartyCookie) {
                themes.add(((PartyCookie) item.getCookie()).getTheme());
            }
        }
        return store.getCooks().stream()
                    .filter(cook -> cook.canTakeTimeSlot(orderTimeSlot) && new HashSet<>(cook.getThemeList()).containsAll(
                            themes))
                    .findFirst()
                    .orElseThrow(CookException::new);
    }

    @Override
    public List<Order> getOrdersWithStatus(OrderStatus status) {
        List<Order> orders = new ArrayList<>();
        for (Order c : orderRepository.findAll()) {
            if (c.getState() != null)
                if (c.getState().getOrderStatus() == status) {
                    orders.add(c);
            }
        }
        return orders;
    }

    @Override
    public Order getOrder(UUID idOrder) throws OrderException {
        if (orderRepository.findById(idOrder).isPresent())
            return orderRepository.findById(idOrder).get();
        else
            throw new OrderException("The store " + idOrder + " does not exist.");
    }
}