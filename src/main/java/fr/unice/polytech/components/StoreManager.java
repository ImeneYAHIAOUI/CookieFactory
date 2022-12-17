package fr.unice.polytech.components;

import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.cookies.PartyCookie;
import fr.unice.polytech.entities.recipe.cookies.PartyCookieFromScratch;
import fr.unice.polytech.entities.recipe.cookies.PartyCookieWithBaseRecipe;
import fr.unice.polytech.entities.recipe.enums.Theme;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.entities.store.*;
import fr.unice.polytech.exception.StoreException;
import fr.unice.polytech.interfaces.*;
import fr.unice.polytech.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

@Component
public class StoreManager implements StoreProcessor, StoreFinder, StoreRegistration {

    StoreRepository storeRepository;

    InventoryFiller inventoryManager;
    ILocationService locationService;

    @Autowired
    public StoreManager(StoreRepository storeRepository, InventoryFiller inventoryManager, ILocationService locationService) {
        this.storeRepository = storeRepository;
        this.inventoryManager = inventoryManager;
        this.locationService = locationService;
    }

    @Override
    public void addStore(Store store) {
        storeRepository.save(store, store.getId());
    }

    /**
     * Return the store with the chosen idStore
     *
     * @param idStore which we want the Store
     * @return the Store
     * @throws StoreException if the store does not exist
     */
    @Override
    public Store getStore(UUID idStore) throws StoreException {

        if (storeRepository.findById(idStore).isPresent())
            return storeRepository.findById(idStore).get();
        else
            throw new StoreException("The store " + idStore + " does not exist.");
    }

    /**
     * add a store in Cod
     *
     * @param nbCooks     nb cooks in the store
     * @param address     of the store
     * @param openingTime of the store
     * @param endingTime  of the store
     * @param tax         of the store
     * @param occasions   occasions available in the store
     */
    @Override
    public Store addStore(int nbCooks, Inventory inventory, String address, String openingTime, String endingTime, double tax, List<Occasion> occasions) {
        List<Cook> cooks = new ArrayList<>();
        for (int i = 0; i < nbCooks; i++) cooks.add(new Cook());

        Store s = StoreFactory.createStore(cooks, address, LocalTime.parse(openingTime), LocalTime.parse(endingTime), inventory, tax, occasions);
        storeRepository.save(s, s.getId());
        return s;
    }

    @Override
    public boolean canAddCookieToStore(Cookie cookie, Store store) {
        if (!inventoryManager.hasIngredient(cookie.getDough(), store.getInventory()) || !inventoryManager.hasIngredient(cookie.getDough(), store.getInventory()))
            return false;
        if (store.getInventory().get(cookie.getDough()) == 0 || store.getInventory().get(cookie.getFlavour()) == 0)
            return false;
        for (Topping t : cookie.getToppings()) {
            if (!inventoryManager.hasIngredient(t, store.getInventory()))
                return false;
            if (store.getInventory().get(t) == 0)
                return false;
        }
        if (cookie instanceof PartyCookie) {
            Theme theme = ((PartyCookie) cookie).getTheme();
            return store.getThemeList().contains(theme);
        }
        return true;
    }

    /**
     * Calls the right method to calculate the max amount of cookies that can be made in the store
     *
     * @param cookie the chosen cookie
     * @param store  the chosen store
     * @return the maximum amount of cookies that can be made in the store
     */
    @Override
    public int getMaxCookieAmount(Cookie cookie, Store store) {
        int factor = 1;
        List<Integer> ingredientAmounts = new ArrayList<>();
        ingredientAmounts.add(store.getInventory().get(cookie.getDough()));
        ingredientAmounts.add(store.getInventory().get(cookie.getFlavour()));
        List<Topping> toppingList = cookie.getToppings();
        for (Topping topping : toppingList) {
            ingredientAmounts.add(store.getInventory().get(topping));
        }

        if (cookie instanceof PartyCookie) {

            if (cookie instanceof PartyCookieWithBaseRecipe) {
                ((PartyCookieWithBaseRecipe) cookie).getAddedIngredients().forEach((ingredient) -> ingredientAmounts.add(store.getInventory().get(ingredient)));
                ((PartyCookieWithBaseRecipe) cookie).getRemovedIngredients().forEach((ingredient) -> ingredientAmounts.remove(store.getInventory().get(ingredient)));
            }
            if (cookie instanceof PartyCookieFromScratch) {
                ((PartyCookieFromScratch) cookie).getSupplementaryIngredients().forEach((ingredient) -> ingredientAmounts.add(store.getInventory().get(ingredient)));
            }
            factor = cookie.getSize().getMultiplier();
        }
        return Collections.min(ingredientAmounts) / factor;
    }

    @Override
    public void addCook(Cook c, Store store) {
        store.getCooks().add(c);
        List<Theme> themes = c.getThemeList();
        for (Theme theme : themes) {
            if (!store.getThemeList().contains(theme))
                store.getThemeList().add(theme);
        }
    }
    @Override
    public void removeStore(Store store) {
        storeRepository.deleteById(store.getId());
    }

    @Override
    public void setHours(Store store, LocalTime openingTime, LocalTime closingTime) {
        if (openingTime.isBefore(closingTime))
            store.setHours(openingTime, closingTime);
        else
            store.setHours(closingTime, openingTime);
    }

    @Override
    public void addOccasion(Store store, Occasion occasion) {
        if (!store.getOccasionList().contains(occasion)) {
            store.getOccasionList().add(occasion);
        }
    }

    @Override
    public void addTheme(Store store, Theme theme) {
        store.getThemeList().add(theme);
    }

    @Override
    public List<Store> getNearbyStores(String address) throws IOException {
        List<Store> nearbyStores = new ArrayList<>();
        for (Store store : storeRepository.findAll()) {
            String storeAddress = store.getAddress();
            double distance = locationService.distance(address, storeAddress);
            if (distance <= 3) {
                nearbyStores.add(store);
            }
        }
        return nearbyStores;

    }

    @Override
    public List<Store> getNearbyStores(String address, int proximity, String unit) throws IOException {
        List<Store> nearbyStores = new ArrayList<>();
        for (Store store : storeRepository.findAll()) {
            String storeAddress = store.getAddress();
            double distance = locationService.distance(address, storeAddress, unit);
            if (distance <= proximity) {
                nearbyStores.add(store);
            }
        }
        return nearbyStores;
    }

    @Override
    public List<Store> getStores() {
        List<Store> stores = new ArrayList<>();
        for (Store s : storeRepository.findAll()) {
            stores.add(s);
        }
        return stores;
    }
}


