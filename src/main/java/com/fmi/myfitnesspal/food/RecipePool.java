package com.fmi.myfitnesspal.food;

import java.util.List;

public interface RecipePool {
    void addRecipe(Recipe recipe);

    void removeRecipe(RecipeId targetId);

    Recipe getRecipe(RecipeId targetId);

    List<Recipe> getAllRecipes();
}
