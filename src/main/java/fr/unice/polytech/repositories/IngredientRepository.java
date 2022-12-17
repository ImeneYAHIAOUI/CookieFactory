package fr.unice.polytech.repositories;

import fr.unice.polytech.entities.recipe.ingredients.Ingredient;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class IngredientRepository extends BasicRepositoryImpl<Ingredient, UUID> {
}
