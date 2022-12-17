package fr.unice.polytech.components;

import fr.unice.polytech.entities.client.Cart;
import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.entities.order.Item;
import fr.unice.polytech.entities.recipe.cookies.*;
import fr.unice.polytech.entities.recipe.enums.CookieSize;
import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.entities.recipe.enums.Theme;
import fr.unice.polytech.entities.recipe.ingredients.Dough;
import fr.unice.polytech.entities.recipe.ingredients.Flavour;
import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.recipe.ingredients.Topping;
import fr.unice.polytech.entities.store.Occasion;
import fr.unice.polytech.entities.store.Store;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.exception.ServiceNotAvailable;
import fr.unice.polytech.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CookieSelector implements CookieChoice {

    StoreProcessor storeManager;
    CartHandler cartManager;
    ClientHandler clientManager;
    CookieFinder cookieFinder;

    @Autowired
    public CookieSelector(
            CartHandler cartManager,
            ClientHandler clientManager,
            StoreProcessor storeProcessor,
            CookieFinder cookieFinder
    )
    {
        this.cartManager = cartManager;
        this.storeManager = storeProcessor;
        this.clientManager = clientManager;
        this.cookieFinder = cookieFinder;
    }
    @Override
    public void personalizeCookieFromBaseRecipe(
            Client client,
            Store store,
            Cookie cookie,
            int amount,
            CookieSize size,
            Occasion occasion,
            Theme theme,
            List<Ingredient> addedIngredients,
            List<Ingredient> removedIngredients
    ) throws CookieException, OrderException, ServiceNotAvailable
    {
        List<Theme> themeList = store.getThemeList();
        List<Occasion> occasionList = store.getOccasionList();
        if (themeList.contains(theme) && occasionList.contains(occasion)) {
            PartyCookieWithBaseRecipe partyCookie = new PartyCookieWithBaseRecipeBuilder()
                    .setBaseCookie(cookie)
                    .setSize(size)
                    .setTheme(theme)
                    .setAddedIngredients(addedIngredients)
                    .setRemovedIngredients(removedIngredients)
                    .build();
            chooseCookie(client, store, partyCookie, amount);
        } else
            throw new ServiceNotAvailable();
    }

    @Override
    public void personalizeCookieFromScratch(
            Client client,
            Store store,
            String cookieName,
            Cooking cooking,
            Mix mix,
            Dough dough,
            Flavour flavour,
            List<Topping> toppings,
            int amount,
            CookieSize size,
            Occasion occasion,
            Theme theme,
            List<Ingredient> supplementaryIngredients
    ) throws CookieException, OrderException, ServiceNotAvailable
    {
        List<Theme> themeList = store.getThemeList();
        List<Occasion> occasionList = store.getOccasionList();
        if (themeList.contains(theme) && occasionList.contains(occasion)) {
            PartyCookieFromScratch partyCookie = new PartyCookieFromScratchBuilder()
                    .setSize(size)
                    .setTheme(theme)
                    .setName(cookieName)
                    .setCooking(cooking)
                    .setMix(mix)
                    .setDough(dough)
                    .setFlavour(flavour)
                    .setToppings(toppings)
                    .setSupplementaryIngredients(supplementaryIngredients)
                    .build();
            chooseCookie(client, store, partyCookie, amount);
        } else throw new ServiceNotAvailable();
    }

    /**
     * Check if the cookie exist before adding the cookie in the cart
     *
     * @param client     the client ordering
     * @param store      the store chosen by the client
     * @param cookieName name of the cookie to add
     * @param amount     amount of cookie to add
     * @throws CookieException if the Cookie does not exist
     * @throws OrderException  if 2 cancelled orders in less than 8 minutes
     */
    @Override
    public void chooseCookie(Client client, Store store, String cookieName, int amount)
            throws CookieException, OrderException
    {
        Optional<Cookie> cookie = cookieFinder
                .getRecipes()
                .stream()
                .filter(c -> (c.getName().equals(cookieName)))
                .findFirst();
        if (cookie.isEmpty())
            throw new CookieException("The cookie " + cookieName + " does not exist.");
        chooseCookie(client, store, cookie.get(), amount);
    }

    @Override
    public void chooseCookie(
            Client client,
            Store store,
            Cookie cookie,
            int amount
    ) throws CookieException, OrderException
    {
        if (client instanceof RegisteredClient && clientManager.isBanned((RegisteredClient) client))
            throw new OrderException(
                    "You have cancelled two orders in 8 minutes or less, you are banned for 10 minutes.\n Remaining time : " + ((RegisteredClient) client).getRemainingBanTime());


        int maxCookieAmount = storeManager.getMaxCookieAmount(cookie, store);
        if (maxCookieAmount < amount) {
            throw new CookieException("This store can't make this amount of cookies");
        }
        Cart cart = client.getCart();
        if (cart.getTax() == null && store.getTax() != null) {
            cart.setTax(store.getTax());
        }
        cartManager.addItem(new Item(amount, cookie), cart);
    }
}
