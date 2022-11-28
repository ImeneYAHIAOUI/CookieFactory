package fr.unice.polytech.entities.store;

import fr.unice.polytech.cod.COD;
import fr.unice.polytech.entities.recipe.Cookie;
import fr.unice.polytech.services.TooGoodToGo;

import java.time.LocalTime;
import java.util.List;

public class StoreFactory {
    public static Store createStore(
            List<Cook> cooks,
            List<Cookie> recipes,
            String address,
            LocalTime openingTime,
            LocalTime closingTime,
            int id,
            Inventory inventory,
            double tax,
            List<Occasion> occasions
    ) {
        Store store = new Store(
                cooks,
                recipes,
                address,
                openingTime,
                closingTime,
                id,
                inventory,
                tax,
                occasions
        );
        COD.getInstance().addStore(store);
        return store;
    }
}
