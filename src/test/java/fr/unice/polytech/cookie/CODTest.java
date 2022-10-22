package fr.unice.polytech.cookie;

import fr.unice.polytech.COD;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.client.UnregisteredClient;
import fr.unice.polytech.recipe.Cookie;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CODTest {

    COD cod;

    Client client;

    Cookie cookie;


    @Given("A new client with an empty cart and with phone number {int}")
    public void givenAClient(Integer number){
        client = new UnregisteredClient(number);
    }

    @And("an empty cod to stock data" )
    public void AndGiven()
    {
        cod=new COD();
    }

    @And("a Cookie {string}")
    public void AndGiven(String name)
    {
        cookie=new Cookie(name);
    }

    @When("Client add amount {int} of cookie to cart")
    public void WhenChooseAmount(int i){
        cod.chooseAmount(i, cookie, client.getCart());
    }

    @Then("Cart is not empty")
    public void ThenCartNotEmpty(){
        assertFalse(client.getCart().getItems().isEmpty());
    }

    @And("Item is added to cart")
    public void ThenAddToCart(){
        assertTrue(client.getCart().getItems().stream().
                anyMatch(cookies -> cookies.getCookie().getName().equals(cookie.getName())));
    }

    @And("Right quantity {int} is added to cart")
    public void ThenCheckQuantityToCart(int i){
        assertTrue(client.getCart().getItems().stream().
                anyMatch(cookies -> cookies.getQuantity()==i));
    }

    @And("Client finalize order")
    public void WhenFinalizeOrder(){
        cod.finalizeOrder(client, cod.getStores().get(0));
    }

    @Then("Cod orders is not empty")
    public void ThenCodOrdersNotEmpty(){
        assertFalse(cod.getOrders().isEmpty());
    }

    @And("Order with right client is added to Cod")
    public void ThenOrderAddedToCodCheckClient(){
        assertTrue(cod.getOrders().stream().
                anyMatch(order -> order.getClient().equals(client)));
    }

    @And("Order with right item is added to Cod")
    public void ThenOrderAddedToCodCheckItems(){
        //System.out.println(cod.getOrders());

        assertTrue(cod.getOrders().stream().
                anyMatch(order -> order.getItems().stream().
                        anyMatch(item -> item.getCookie().getName().equals(cookie.getName()))));
    }

    @And("Cart is empty")
    public void ThenCartEmpty(){
        assertTrue(client.getCart().getItems().isEmpty());
    }





}
