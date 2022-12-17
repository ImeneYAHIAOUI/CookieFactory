package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.entities.tooGoodToGo.TooGoodToGoBag;

import java.util.Collection;

public interface TooGoodToGoBagFinder {


    /**
     * Returns the TooGoodToGoBags created by this store
     *
     * @return the list of TooGoodToGo bags
     */

    Collection<TooGoodToGoBag> getBags(Store store);
}
