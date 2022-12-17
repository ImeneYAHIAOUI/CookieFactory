package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.StoreException;
import fr.unice.polytech.services.LocationService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface StoreFinder {

    List<Store> getStores();
    Store getStore(UUID idStore) throws StoreException;
    List<Store> getNearbyStores(String address) throws IOException;
    List<Store> getNearbyStores(String address, int proximity, String unit) throws IOException;
}
