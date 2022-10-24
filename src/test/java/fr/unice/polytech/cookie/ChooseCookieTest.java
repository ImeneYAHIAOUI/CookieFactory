package fr.unice.polytech.cookie;

import fr.unice.polytech.COD;
import fr.unice.polytech.CookieException;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.recipe.*;
import fr.unice.polytech.store.Inventory;
import fr.unice.polytech.store.Store;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ChooseCookieTest {

    COD cod;

    Client client;

    Integer amount;

    Store store;

    Inventory inventory;


    Cookie cookie;

    List<Ingredient> CODIngredients = new ArrayList<>();

    List<Cookie> cookieList = new ArrayList<>();



    @And("A client with phone number {int}")
    public void givenAClient(Integer number)
    {
        client = new UnregisteredClient(number);
    }

    @Given("a cod to store data")
    public void AndGiven()
    {
        cod=new COD();
        CODIngredients.add(new Topping("chocolate chips", 1));
        CODIngredients.add(new Dough("chocolate dough", 1));
        CODIngredients.add(new Flavour("vanillaFlavour", 1));
        CODIngredients.add(new Flavour("strawberryFlavour", 1));
        CODIngredients.add(new Flavour("chocolateFlavour", 1));
        CODIngredients.add(new Topping("white chocolate chips", 1));


    }



    @Given("^an inventory with the ingredients and amounts$")
    public void givenInventory(DataTable data)

    {
        List<List<String>> rows = data.asLists(String.class);

        inventory = new Inventory(CODIngredients);

        for (List<String> row : rows)
        {
            Ingredient ingredient = CODIngredients.stream().filter(i -> i.getName().equals(row.get(0))).findFirst().orElse(null);
            inventory.addAmountQuantity(ingredient, Integer.parseInt(row.get(1)));
        }

    }



    @And("a store with id {int}")
    public void givenStore(Integer id)
    {


        store = new Store(new ArrayList<>(), new ArrayList<>(), "address", LocalTime.parse("08:30"), LocalTime.parse("16:00"), id, inventory);
    }



    @And("^the store has cookies$")
    public void AndGiven(DataTable table)
    {
        List<List<String>> rows = table.asLists(String.class);

            for (List<String> row : rows)
            {
                Cookie cookie = new Cookie(row.get(0));
                List<Topping> toppings = new ArrayList<>();


                cookie.setDough((Dough) CODIngredients.stream().filter(i -> i.getName().equals(row.get(1))).findFirst().orElse(null));

                cookie.setFlavour( (Flavour) CODIngredients.stream().filter(i -> i.getName().equals(row.get(2))).findFirst().orElse(null));

                for (int j = 3; j < row.size(); j++)
                {
                    int finalJ = j;
                    toppings.add(((Topping) CODIngredients.stream().filter(i -> i.getName().equals(row.get(finalJ))).findFirst().orElse(null)));
                }
                cookie.setToppingList(toppings);
                cookieList.add(cookie);
            }

        store.addCookies(cookieList);

    }




    @When("Client chooses cookies of type {string}")
    public void WhenChooseCookie(String name)
    {
        cookie = cookieList.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(new Cookie("unavailable"));
    }

    @And("amount {int}")
    public void WhenChooseAmount(int i)
    {
        amount = i;
    }

    @Then("this order cannot be purchased")
    public void ThenCookiesNotAvailable()
    {
        assertThrows(CookieException.class, () -> cod.chooseCookie(client,store, cookie, amount));
    }

    @Then("this order can be purchased")
    public void ThenCookiesAvailable()
    {
        assertDoesNotThrow( () -> cod.chooseCookie(client,store, cookie, amount));
    }
















}
