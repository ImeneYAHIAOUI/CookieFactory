package fr.unice.polytech.components;

import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import fr.unice.polytech.entities.recipe.ingredients.IngredientFactory;
import fr.unice.polytech.entities.recipe.enums.IngredientType;
import fr.unice.polytech.exception.CatalogException;
import fr.unice.polytech.exception.IngredientTypeException;
import fr.unice.polytech.interfaces.IngredientFinder;
import fr.unice.polytech.interfaces.IngredientRegistration;
import fr.unice.polytech.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Component
public class IngredientManager implements IngredientFinder, IngredientRegistration {

    IngredientRepository ingredientRepository;
    private static final IngredientFactory INGREDIENTFACTORY = new IngredientFactory();

    @Autowired
    public IngredientManager(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }
    @Override
    public  void removeIngredient(String name, IngredientType ingredientType){

        Ingredient ingredient=StreamSupport.stream(ingredientRepository.findAll().spliterator(), false)
                .filter(cust -> name.equals(cust.getName()) && cust.getIngredientType().equals(ingredientType))
                .findFirst().orElse(null);
        if(ingredient!=null)
            ingredientRepository.deleteById(ingredient.getId());
    }
    /**
     * Add a new ingredient to catalog
     * @param name of the ingredient
     * @param price of the ingredient
     * @param ingredientType of the ingredient
     * @throws CatalogException if an ingredient of the same name already exists
     */
    @Override
    public void addIngredient(String name, double price, IngredientType ingredientType) throws IngredientTypeException {
        Ingredient newIngredient = INGREDIENTFACTORY.createIngredient(this,name, price, ingredientType);
        ingredientRepository.save(newIngredient, newIngredient.getId());
    }

    /**
     * Return the ingredient with the chosen id
     * @param id of the ingredient
     * @return the ingredient
     * @throws CatalogException if none of the ingredients have this name
     */
    @Override
    public Ingredient getIngredientById(UUID id) throws CatalogException {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        if(ingredient.isEmpty())
            throw new CatalogException("The ingredient "+id+" is not in the catalog.");
        else
            return ingredient.get();
    }

    @Override
    public Ingredient getIngredientByName(String name) throws CatalogException {
        for (Ingredient i: ingredientRepository.findAll()) {
            if(i.getName().equalsIgnoreCase(name))
                return i;
        }
        throw new CatalogException("The ingredient "+name+" is not in the catalog.");
    }


    @Override
    public boolean containsById(UUID id) {
        return ingredientRepository.existsById(id);
    }

    @Override
    public boolean containsByName(String name) {
        for (Ingredient i: ingredientRepository.findAll()) {
            if(i.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    @Override
    public List<Ingredient> getIngredients(){
        List<Ingredient> ingredients = new ArrayList<>();
        for (Ingredient c: ingredientRepository.findAll()) {
            ingredients.add(c);
        }
        return ingredients;
    }

    @Override
    public void deleteAllIngredients() {
        ingredientRepository.deleteAll();
    }

}
