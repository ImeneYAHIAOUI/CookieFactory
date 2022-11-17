package fr.unice.polytech.store;


import fr.unice.polytech.COD;
import fr.unice.polytech.client.Cart;
import fr.unice.polytech.exception.BadQuantityException;
import fr.unice.polytech.exception.CookException;
import fr.unice.polytech.recipe.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Store {
    @Getter
    public final String address;
    @Getter
    private final List<Cookie> recipes;
    @Getter
    private final List<Cook> cooks;
    @Getter
    public LocalTime openingTime;
    @Getter
    public LocalTime closingTime;
    @Getter
    private final int id;
    @Getter
    private final Inventory inventory;
    @Getter
    @Setter
    private  Double tax;
    @Getter
    private  final List<Occasion> occasionList;
    public Store(List<Cook> cooks, List<Cookie> recipes, String address, LocalTime openingTime, LocalTime closingTime, int id, Inventory inventory,double tax,List<Occasion> occasions) {
        occasionList =new ArrayList<>();
        this.cooks = new ArrayList<>(cooks);
        this.recipes = new ArrayList<>(recipes);
        this.address = address;
        this.tax=tax;
        if (openingTime.isBefore(closingTime)) {
            this.openingTime = openingTime;
            this.closingTime = closingTime;
        } else {
            System.out.println("Error, given closingTime is before openingTime, openingTime get closedTime value and closingTime get openingTime value");
            this.openingTime = closingTime;
            this.closingTime = openingTime;
        }
        this.id = id;
        this.inventory = inventory;
        if( occasions != null){
            occasions.forEach(occasion ->{
                if(!this.occasionList.contains(occasion)){
                    this.occasionList.add(occasion);
                }
            } );
        }

    }
    public void setHours(LocalTime openingTime, LocalTime closingTime) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }
    @Override
    public String toString() {
        return "Store{" +
                "storeId=" + id +
                ", address='" + address + '\'' +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                '}';
    }

    /**
     * Returns a free cook for the given cart
     *
     * @param cart the cart for which we want a cook
     * @return a free cook
     * @throws CookException if no cook is available
     */
    public Cook getFreeCook(Cart cart) throws CookException {
        TimeSlot orderTimeSlot = new TimeSlot(cart.getPickupTime().minus(cart.totalCookingTime()), cart.getPickupTime());
        return cooks.stream()
                .filter(cook -> cook.canTakeTimeSlot(orderTimeSlot))
                .findFirst()
                .orElseThrow(CookException::new);
    }

    /**
     * Returns the list of possible pickup times for a given cart in this store
     *
     * @param cart the cart to consider
     * @return the list of possible pickup times
     */
    public List<LocalTime> getPossiblePickupTimes(Cart cart) {
        List<LocalTime> possiblePickupTimes = new ArrayList<>();
        // Acceptable earliest pickup time is 10 minutes after now.
        // This is to account for the time it takes to place the order.
        LocalTime now = LocalTime.now(COD.getCLOCK());
        LocalTime cookingStartTime = now.plusMinutes(15+(now.getMinute() % 5)).truncatedTo(ChronoUnit.MINUTES);
        TimeSlot potentialTimeSlot = new TimeSlot(cookingStartTime, cart.totalCookingTime());
        while (potentialTimeSlot.getEnd().isBefore(closingTime)) {
            if (cooks.stream().anyMatch(cook -> cook.canTakeTimeSlot(potentialTimeSlot))) {
                possiblePickupTimes.add(potentialTimeSlot.getEnd());
            }
            // New pickup time every 5 minutes
            potentialTimeSlot.slideBy(Duration.ofMinutes(5));
        }
        return possiblePickupTimes;
    }

    public void addIngredients(Ingredient ingredient, int quantity) throws BadQuantityException {
        if (quantity < 0) {
            throw new BadQuantityException("Quantity cannot be under 0");
        }
        if (!this.inventory.hasIngredient(ingredient))
            this.inventory.addIngredient(ingredient, quantity);
        else {
            this.inventory.addAmountQuantity(ingredient, quantity);
        }
    }

    public int getMaxCookieAmount(Cookie cookie,int amountFactor) {
        List<Integer> ingredientAmounts = new ArrayList<>();
        ingredientAmounts.add(inventory.get(cookie.getDough())*amountFactor);
        ingredientAmounts.add(inventory.get(cookie.getFlavour())*amountFactor);
        List<Topping> toppingList = cookie.getToppings();

        for (Topping topping : toppingList) {
            ingredientAmounts.add(inventory.get(topping)*amountFactor);
        }
        return Collections.min(ingredientAmounts);
    }

    public void addCookies(List<Cookie> cookieList) {
        this.recipes.addAll(cookieList);
    }

    public void removeCookies(Ingredient ingredient) {
        if (ingredient instanceof Dough) {
            recipes.removeIf(cookie -> cookie.getDough().equals(ingredient));
        }

        if (ingredient instanceof Flavour) {
            recipes.removeIf(cookie -> cookie.getFlavour().equals(ingredient));
        }

        if (ingredient instanceof Topping) {
            recipes.removeIf(cookie -> cookie.getToppings().contains(ingredient));
        }
    }
    public void addCook(Cook c){
        cooks.add(c);
    }

    public boolean canAddCookieToStore(Cookie cookie){
        if(!inventory.hasIngredient(cookie.getDough()) || !inventory.hasIngredient(cookie.getFlavour()))
            return false;
        if(inventory.get(cookie.getDough()) == 0 || inventory.get(cookie.getFlavour()) == 0)
            return false;
        for (Topping t: cookie.getToppings()) {
            if(!inventory.hasIngredient(t))
                return false;
            if(inventory.get(t) ==0)
                return false;
        }
        return true;
    }

}
