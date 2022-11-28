package stepdefs;

import fr.unice.polytech.entities.client.Client;
import fr.unice.polytech.entities.client.RegisteredClient;
import fr.unice.polytech.cod.COD;
import fr.unice.polytech.entities.recipe.*;
import fr.unice.polytech.entities.store.*;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.exception.InvalidPhoneNumberException;
import fr.unice.polytech.exception.OrderException;
import fr.unice.polytech.exception.ServiceNotAvailable;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PersonalizeCookieStepDefs {

    COD cod = COD.getInstance();
    Store store;

    Client client = new RegisteredClient("1","********","0606060606");
    final List<Ingredient> codIngredients = List.of(
            new Topping("chocolate chips", 1),
            new Topping("m&ms", 1),
            new Flavour("caramel", 1),
            new Dough("chocolate dough", 1),
            new Flavour("vanillaFlavour", 1),
            new Flavour("strawberryFlavour", 1),
            new Flavour("chocolateFlavour", 1),
            new Topping("white chocolate chips", 1));
    Cookie cookie = CookieFactory.createSimpleCookie("chocolate",1.,1,Cooking.CHEWY, Mix.MIXED,(Dough) codIngredients.get(3),(Flavour) codIngredients.get(4),List.of((Topping) codIngredients.get(0), (Topping) codIngredients.get(7)));
    Inventory inventory;

    List<Ingredient> addedIngredients;
    List<Ingredient> removedIngredients;

    Occasion occasion = Occasion.BIRTHDAY;

    Theme theme = Theme.MOVIE;

    CookieSize size;

    Dough dough;
    Flavour flavour;
    List<Topping> toppings;

    int amount;

    public PersonalizeCookieStepDefs() throws InvalidPhoneNumberException, CookieException {
    }

    @Given("a store")
    public void aStore() {

        store = StoreFactory.createStore(new ArrayList<>(), new ArrayList<>(), "1 rue de la paix", LocalTime.parse("08:00"), LocalTime.parse("12:00"), 1, inventory, 0.2, new ArrayList<>());
    }

    @Given("^an inventory with the ingredients$")
    public void givenInventoryWithIngredients(DataTable data) {
        List<List<String>> rows = data.asLists(String.class);

        inventory = new Inventory(codIngredients);

        for (List<String> row : rows) {
            Ingredient ingredient = codIngredients.stream().filter(i -> i.getName().equals(row.get(0))).findFirst().orElse(null);
            inventory.addAmountQuantity(ingredient, Integer.parseInt(row.get(1)));
        }

    }

    @When("^the client wants to personalize the cookie of by adding$")
    public void theClientWantsToPersonalizeACookieByAdding(List<String> ingredients) {
        addedIngredients = new ArrayList<>();
        for (String ingredient : ingredients) {
            addedIngredients.add(codIngredients.stream().filter(i -> i.getName().equals(ingredient)).findFirst().get());
        }
    }

    @When("the client wants to personalize the cookie of by choosing dough {string}, flavour {string}")
    public void theClientWantsToPersonalizeTheCookieOfByChoosingDoughFlavourAndToppings(String doughName, String flavourName) {
        dough = (Dough) codIngredients.stream().filter(i -> i.getName().equals(doughName)).findFirst().get();
        flavour = (Flavour) codIngredients.stream().filter(i -> i.getName().equals(flavourName)).findFirst().get();
    }

    @And("^adding the toppings$")
    public void andToppings(List<String> toppingsNames)
    {
        toppings = new ArrayList<>();
        for (String toppingName : toppingsNames) {
            toppings.add((Topping) codIngredients.stream().filter(i -> i.getName().equals(toppingName)).findFirst().get());
        }
    }

    @And("^adding the ingredients$")
    public void addingTheIngredients(List<String> ingredients) {
        addedIngredients = new ArrayList<>();
        for (String ingredient : ingredients) {
            addedIngredients.add(codIngredients.stream().filter(i -> i.getName().equals(ingredient)).findFirst().get());
        }
    }



    @Given("the store has the required occasion")
    public void theStoreHasTheRequiredOccasion() {
        cod.addOccasion(store,occasion);


    }
    @And("the store has a cook specialised in the chosen theme")
    public void theStoreHasTheRequiredTheme(){
        Cook cook = new Cook(1);
        cook.addTheme(theme);
        store.addCook(cook);
    }

    @And("the chosen cookie")
    public void theChosenCookie() {
        store.addCookies(List.of(cookie));
    }

    @And("^removing$")
    public void removing(List<String> ingredients) {
        removedIngredients = new ArrayList<>();
        for (String ingredient : ingredients) {
            addedIngredients.add(codIngredients.stream().filter(i -> i.getName().equals(ingredient)).findFirst().get());
        }
    }
    @And("order {int} of them of size {string}")
    public void orderOfThen(int amount,String size) {
        this.size = switch (size) {
            case "L" -> CookieSize.L;
            case "XL" -> CookieSize.XL;
            case "XXL" -> CookieSize.XXL;
            default -> throw new IllegalStateException("Unexpected value: " + size);
        };
        this.amount = amount;
    }

    @Then("the cookie personalized from base recipe can be ordered")
    public void theCookieCanBeAddedToCard() throws CookieException, ServiceNotAvailable, OrderException {
        cod.personalizeCookieFromBaseRecipe(client, store,cookie,amount,size, occasion,theme, addedIngredients, removedIngredients);
        assertNotNull(client.getCart().getItems().get(0));
    }

    @Then("the cookie personalized from scratch can be ordered")
    public void theCookieCanBeOrdered() throws CookieException, ServiceNotAvailable, OrderException {
        cod.personalizeCookieFromScratch(client, store,"personalized Cookie",Cooking.CHEWY,Mix.MIXED,dough,flavour,toppings,amount,size, occasion,theme, addedIngredients);
        assertNotNull(client.getCart().getItems().get(0));
    }

    @Then("the cookie personalized from scratch cannot be ordered because the store does not have the required ingredients")
    public void theCookiePersonalizedFromScratchCannotBeAddedToCard() {
            assertThrows(CookieException.class, () -> cod.personalizeCookieFromScratch(client, store,"personalized Cookie",Cooking.CHEWY,Mix.MIXED,dough,flavour,toppings,amount,size, occasion,theme, addedIngredients));


    }
    @Then("the cookie personalized from scratch cannot be ordered because the store does not have the required themes and or ingredients")
    public void theCookiePersonalizedFromScratchCannotBeAddedToCard2() {
        assertThrows(ServiceNotAvailable.class, () -> cod.personalizeCookieFromScratch(client, store, "personalized Cookie", Cooking.CHEWY, Mix.MIXED, dough, flavour, toppings, amount, size, occasion, theme, addedIngredients));
    }


    @Then("the cookie cannot be ordered because the chosen cookie is not available or because there isn't enough ingredients")
    public void theCookieCannotBeAddedToCardBecauseTheChosenCookieIsNotAvailable() {
        assertThrows(CookieException.class, () -> cod.personalizeCookieFromBaseRecipe(client, store,cookie,amount,size, occasion,theme, addedIngredients, removedIngredients));
    }

    @Then("the cookie cannot be ordered because the chosen theme and or occasion are not available")
    public void theCookieCannotBeAddedToCardBecauseTheChosenThemeAndOrOccasionAreNotAvailable() {
        assertThrows(ServiceNotAvailable.class, () ->  cod.personalizeCookieFromScratch(client, store, "personalized Cookie", Cooking.CHEWY, Mix.MIXED, dough, flavour, toppings, amount, size, occasion, theme, addedIngredients));
    }

    @And("the cookie's price is changed")
    public void theCookieSPriceIsChanged() {
        Cookie OrderedCookie =  client.getCart().getItems().get(0).getCookie();
        double addPrice = 0;
        double removePrice = 0;
        for (Ingredient ingredient : addedIngredients) {
            addPrice += ingredient.getPrice() * size.getMultiplier();
        }
        for (Ingredient ingredient : removedIngredients) {
            removePrice += ingredient.getPrice() * size.getMultiplier();
        }
        assertEquals(OrderedCookie.getPrice(), cookie.getPrice()* size.getMultiplier() + addPrice - removePrice, 0.01);

    }

    @And("the cookie's price is calculated based on the size and ingredients")
    public void theCookieSPriceIsCalculatedBasedOnTheSizeAndIngredients() {
        Cookie OrderedCookie =  client.getCart().getItems().get(0).getCookie();
        double addPrice = dough.getPrice() + flavour.getPrice() + toppings.stream().mapToDouble(Topping::getPrice).sum();
        for (Ingredient ingredient : addedIngredients) {
            addPrice += ingredient.getPrice() ;
        }
        assertEquals(OrderedCookie.getPrice(),  addPrice * size.getMultiplier(), 0.01);
    }



}
