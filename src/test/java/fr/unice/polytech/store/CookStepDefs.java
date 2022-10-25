package fr.unice.polytech.store;

import fr.unice.polytech.COD;
import fr.unice.polytech.recipe.Cookie;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CookStepDefs {
    COD cod;
    Cook cook;
    Cookie cookie;

    public CookStepDefs() {
    }

    @Given("a cook with id {int}")
    public void given(Integer id) {
        cook = new Cook(id);
    }

    @And("a cookie with name {string}")
    public void andGivenCookie(String name) {
        cookie = new Cookie(name, 0.0, 0.0, null, null, null, null, null);
    }

    @And("a cod")
    public void andGiven() {
        cod = new COD();
    }

    @When("cook suggest recipe")
    public void whenSuggestCookie() {
        cod.suggestRecipe(cookie);
    }

    @Then("suggested recipe is added to suggested recipe list")
    public void thenAddToList() {
        assertTrue(cod.getSuggestedRecipes().contains(cookie));
    }

    @When("COD accept recipe")
    public void whenRecipeIsAccepted() {
        cod.acceptRecipe(cookie, 15.6);
        cod.getRecipes().add(cookie);
    }

    @Then("suggested recipe is added to recipe list")
    public void thenAddToRecipeList() {

        assertTrue(cod.getRecipes().contains(cookie));
    }

    @And("suggested recipe is removed from suggested recipe list")
    public void thenIsRemovedFomList() {
        assertFalse(cod.getSuggestedRecipes().contains(cookie));
    }

    @When("COD decline recipe")
    public void whenRecipeIsDeclined() {
        cod.declineRecipe(cookie);
    }




}
