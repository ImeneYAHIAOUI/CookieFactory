package fr.unice.polytech.cucumber.store;

import fr.unice.polytech.entities.recipe.cookies.Cookie;
import fr.unice.polytech.entities.recipe.cookies.SimpleCookieBuilder;
import fr.unice.polytech.entities.store.Cook;
import fr.unice.polytech.exception.CookieException;
import fr.unice.polytech.interfaces.CookieFinder;
import fr.unice.polytech.interfaces.CookieRegistration;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CookStepDefs {
    Cook cook;
    Cookie cookie;

    @Autowired
    CookieRegistration cookieRegistration;

    @Autowired
    CookieFinder cookieFinder;

    public CookStepDefs() {
    }

    @Given("a cook")
    public void given() {
        cook = new Cook();
    }

    @And("a cookie with name {string}")
    public void andGivenCookie(String name) throws CookieException {
        cookie = new SimpleCookieBuilder().setName(name).setPrice(0.0).setCookingTime(0).build();
    }

    @When("cook suggest recipe")
    public void whenSuggestCookie() {
        cookieRegistration.suggestRecipe(cookie);
    }

    @Then("suggested recipe is added to suggested recipe list")
    public void thenAddToList() {
        assertTrue(cookieFinder.getSuggestedRecipes().contains(cookie));
    }

    @When("COD accept recipe")
    public void whenRecipeIsAccepted() {
        cookieRegistration.acceptRecipe(cookie, 15.6);
        cookieFinder.getRecipes().add(cookie);
    }

    @Then("suggested recipe is added to recipe list")
    public void thenAddToRecipeList() {

        assertTrue(cookieFinder.getRecipes().contains(cookie));
    }

    @And("suggested recipe is removed from suggested recipe list")
    public void thenIsRemovedFomList() {
        assertFalse(cookieFinder.getSuggestedRecipes().contains(cookie));
    }

    @When("COD decline recipe")
    public void whenRecipeIsDeclined() {
        cookieRegistration.declineRecipe(cookie);
    }

}
