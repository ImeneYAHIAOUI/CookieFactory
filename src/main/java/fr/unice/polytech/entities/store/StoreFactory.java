package fr.unice.polytech.entities.store;

import java.time.LocalTime;
import java.util.List;

public class StoreFactory {

    public static Store createStore(
            List<Cook> cooks,
            String address,
            LocalTime openingTime,
            LocalTime closingTime,
            Inventory inventory,
            double tax,
            List<Occasion> occasions) {
        return new Store(
                cooks,
                address,
                openingTime,
                closingTime,
                inventory,
                tax,
                occasions
        );
    }
}
