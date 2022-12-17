package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.order.Order;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.tooGoodToGo.TooGoodToGoBag;

import java.util.Collection;

/**
 * This interface is used to check for obsolete orders and convert them
 * into TooGoodToGo bags. There should be one instance of this interface
 * per store.
 */
public interface TooGoodToGoBagCreator {
    /**
     * Initializes the scheduler that will check for obsolete
     * commands and convert them into bags.
     */
    void initScheduler(Store store);

    /**
     * Checks if the store has obsolete orders
     * and returns them
     *
     * @return the list of obsolete orders
     */
    Collection<Order> checkForObsoleteOrders();

    /**
     * Converts obsolete orders into TooGoodToGo bags
     *
     * @param orders the list of obsolete orders
     * @return a list of TooGoodToGo bags
     */
    Collection<TooGoodToGoBag> convertOrders(Collection<Order> orders);

    /**
     * Publishes the TooGoodToGo bags to the service
     *
     * @param bags the list of TooGoodToGo bags
     */
    void publishBags(Collection<TooGoodToGoBag> bags);

    /**
     * Performs th actual action to check for obsolete orders,
     * convert them into bags and to publish them
     *
     * @param store the store to perform the action on
     */
    void performOrderRecycling(Store store);
}
