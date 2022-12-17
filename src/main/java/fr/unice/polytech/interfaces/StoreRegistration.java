package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.recipe.enums.Theme;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.entities.store.Inventory;
import fr.unice.polytech.entities.store.Occasion;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.services.LocationService;

import java.util.List;

public interface StoreRegistration {

    Store addStore(int nbCooks, Inventory inventory, String address, String openingTime, String endingTime, double tax, List<Occasion> occasions);

    void addStore(Store store);

    void removeStore(Store store);

    void addCook(Cook c, Store store);

    void addOccasion(Store store, Occasion occasion);

    void addTheme(Store store, Theme theme);

}
