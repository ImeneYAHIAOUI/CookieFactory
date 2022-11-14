package fr.unice.polytech.store;

import fr.unice.polytech.recipe.Recipe;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Occasion {
    @Getter
    public final List<Recipe> recipeList;
    @Getter
    public String name;

    public Occasion(String name){
        this.name=name;
        recipeList=new ArrayList<>();
    }
}
