package fr.unice.polytech.store;

import fr.unice.polytech.COD;
import fr.unice.polytech.recipe.Cookie;
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
    @And( "a cookie with name {string}")
    public void AndGivenCookie(String name)
    {
        cookie= new Cookie(name);
    }
    @And("a cod" )
    public void AndGiven()
    {
         cod=new COD();
    }
    @When("cook suggest recipe")
    public void WhensuggestCookie()  {
        cod.suggestRecipe(cookie);
    }
    @Then("suggested recipe is added to suggested recipe list")
    public void ThenAddToList(){
        assertTrue(cod.getSuggestedRecipes().contains(cookie));
    }
    @When("COD accept recipe")
    public void WhenRecipeIsAccepted()  {
        cod.acceptRecipe(cookie,15.6);
        cod.getRecipes().add(cookie);
    }
    @Then("suggested recipe is added to recipe list")
    public void ThenAddToRecipeList(){

        assertTrue(cod.getRecipes().contains(cookie));
    }
    @And("suggested recipe is removed from suggested recipe list")
    public void ThenIsRemovedFomList(){
        assertTrue(!cod.getSuggestedRecipes().contains(cookie) );
    }
    @When("COD decline recipe")
    public void WhenRecipeIsDeclined( )  {
        cod.declineRecipe(cookie);
    }




}
