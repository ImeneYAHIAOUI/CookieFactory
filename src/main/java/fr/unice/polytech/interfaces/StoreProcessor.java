package fr.unice.polytech.interfaces;

import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.store.Store;

import java.time.LocalTime;

public interface StoreProcessor {


    boolean canAddCookieToStore(Cookie cookie, Store store);

    int getMaxCookieAmount(Cookie cookie, Store store);

    void setHours(Store store, LocalTime openingTime, LocalTime closingTime);
}
