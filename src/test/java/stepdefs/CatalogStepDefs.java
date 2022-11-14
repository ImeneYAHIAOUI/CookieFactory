package stepdefs;

import fr.unice.polytech.Catalog;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.recipe.IngredientType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CatalogStepDefs {

    Catalog catalog;

    @Given("an empty catalog")
    public void emptyCatalog(){
        catalog = new Catalog();
    }

    @And("add the ingredient with name {string}, price {double} and type {string} does not throw exception")
    public void fillCatalog(String name, double price, String type){
        IngredientType ingredientType;
        if(type.equals("DOUGH"))
            ingredientType = IngredientType.DOUGH;
        else if (type.equals("FLAVOUR"))
            ingredientType = IngredientType.FLAVOUR;
        else
            ingredientType = IngredientType.TOPPING;

        assertDoesNotThrow(() -> catalog.addIngredient(name, price, ingredientType));
    }

    @And("add the ingredient with name {string}, price {double} and type {string} throws exception")
    public void fillCatalogException(String name, double price, String type){
        IngredientType ingredientType;
        if(type.equals("DOUGH"))
            ingredientType = IngredientType.DOUGH;
        else if (type.equals("FLAVOUR"))
            ingredientType = IngredientType.FLAVOUR;
        else
            ingredientType = IngredientType.TOPPING;

        assertThrows(CatalogException.class, () -> catalog.addIngredient(name, price, ingredientType));
    }

    @Then("get the ingredient with name {string} does not throw exception")
    public void getCatalog(String name){
        assertDoesNotThrow(() -> catalog.getIngredient(name));
    }

    @Then("get the ingredient with name {string} throws exception")
    public void getCatalogException(String name){
        catalog = new Catalog();
        assertThrows(CatalogException.class, () -> catalog.getIngredient(name));
    }

}
