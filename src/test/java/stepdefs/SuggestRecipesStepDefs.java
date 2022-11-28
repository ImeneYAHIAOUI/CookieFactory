package stepdefs;

import fr.unice.polytech.cod.COD;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.entities.recipe.Cooking;
import fr.unice.polytech.entities.recipe.IngredientType;
import fr.unice.polytech.entities.recipe.Mix;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SuggestRecipesStepDefs {

    COD cod;

    @Given("empty cod")
    public void initializeCod(){
        cod = COD.getInstance();
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
        assertDoesNotThrow(() -> cod.addIngredientCatalog(name, price, ingredientType));
    }

    @Then("suggest Recipe with Dough {string}, Flavour {string} and Topping {string} without exception")
    public void suggestRecipe(String d, String f, String t){
        assertDoesNotThrow(() -> cod.suggestRecipe("", 0, 0, Cooking.CHEWY, Mix.TOPPED,
                cod.getIngredientCatalog(d), cod.getIngredientCatalog(f), List.of(cod.getIngredientCatalog(t))));
    }

    @Then("suggest Recipe with Dough {string}, Flavour {string} and Topping {string} with exception")
    public void suggestRecipeException(String d, String f, String t){
        assertThrows(CatalogException.class, () -> cod.suggestRecipe("", 0, 0, Cooking.CHEWY, Mix.TOPPED,
                cod.getIngredientCatalog(d), cod.getIngredientCatalog(f), List.of(cod.getIngredientCatalog(t))));
    }
}
