package fr.unice.polytech.biblio.Store;

import fr.unice.polytech.COD;
import fr.unice.polytech.client.Client;
import fr.unice.polytech.order.Order;
import fr.unice.polytech.order.OrderException;
import fr.unice.polytech.order.OrderStatus;
import fr.unice.polytech.recipe.Cookie;
import fr.unice.polytech.store.Cook;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CookTest {
    COD cod;
    Cook cook ;
    Cookie cookie;

    public CookTest()  {}

    @Given( "a cook with id {int}")
    public void given(Integer id)
    {
        cook = new Cook(id);
    }
    @And("a cod" )
    public void AndGiven()
    {
         cod=new COD();
    }
    @When("cook create Cookie with name {string}")
    public void WhenCookCreateCookieWithName( String name)  {
        cookie= new Cookie(name);
    }
    @Then("suggested recipe is added to suggested recipe list")
    public void ThenAddToList(){
        cod.suggestRecipe(cookie);
    }

    @When("COD accept recipe")
    public void WhenRecipeIsAccepted()  {
        cod.acceptRecipe(cookie,15.6);
    }
    @Then("suggested recipe is added to recipe list")
    public void ThenAddToRecipeList(){
        assertTrue(cod.recipes.contains(cookie));
    }
    @And("suggested recipe is removed from suggested recipe list")
    public void ThenIsRemovedlList(){
        assertTrue(!cod.suggestedRecipes.contains(cookie) );
    }
    @When("COD decline recipe")
    public void WhenRecipeIsDeclined( )  {
        cod.declineRecipe(cookie);
    }
    @Then("suggested recipe is removed from suggested recipe list")
    public void ThenIsRemovedFromlList(){
        assertTrue(!cod.suggestedRecipes.contains(cookie) );
    }



}
