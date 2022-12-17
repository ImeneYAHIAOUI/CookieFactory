package fr.unice.polytech.cucumber.store;


import fr.unice.polytech.entities.recipe.enums.Cooking;
import fr.unice.polytech.entities.recipe.enums.IngredientType;
import fr.unice.polytech.entities.recipe.enums.Mix;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.interfaces.CookieRegistration;
import fr.unice.polytech.interfaces.IngredientFinder;
import fr.unice.polytech.interfaces.IngredientRegistration;
import fr.unice.polytech.repositories.IngredientRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class SuggestRecipesStepDefs {

    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    IngredientFinder ingredientFinder;

    @Autowired
    IngredientRegistration ingredientRegistration;

    @Autowired
    CookieRegistration cookieRegistration;

    @Given("empty cod")
    public void initializeCod(){
        ingredientRepository.deleteAll();
    }

    @When("add the ingredient with name {string}, price {double} and type {string}")
    public void addCatalog(String name, double price, String type){
        IngredientType ingredientType;
        if(type.equals("DOUGH"))
            ingredientType = IngredientType.DOUGH;
        else if (type.equals("FLAVOUR"))
            ingredientType = IngredientType.FLAVOUR;
        else
            ingredientType = IngredientType.TOPPING;
        assertDoesNotThrow(() -> ingredientRegistration.addIngredient(name, price, ingredientType));
    }

    @Then("suggest Recipe with Dough {string}, Flavour {string} and Topping {string} without exception")
    public void suggestRecipe(String d, String f, String t){
                assertDoesNotThrow(() -> cookieRegistration.suggestRecipe("", 0, 0, Cooking.CHEWY, Mix.TOPPED,
                ingredientFinder.getIngredientByName(d), ingredientFinder.getIngredientByName(f), List.of(ingredientFinder.getIngredientByName(t))));
    }

    @Then("suggest Recipe with Dough {string}, Flavour {string} and Topping {string} with exception")
    public void suggestRecipeException(String d, String f, String t){
        assertThrows(CatalogException.class, () -> cookieRegistration.suggestRecipe("", 0, 0, Cooking.CHEWY, Mix.TOPPED,
                ingredientFinder.getIngredientByName(d), ingredientFinder.getIngredientByName(f), List.of(ingredientFinder.getIngredientByName(t))));
        assertThrows(CatalogException.class, () -> cookieRegistration.suggestRecipe("", 0, 0, Cooking.CHEWY, Mix.TOPPED,
                ingredientFinder.getIngredientByName(d), ingredientFinder.getIngredientByName(f), List.of(ingredientFinder.getIngredientByName(t))));
    }
}
