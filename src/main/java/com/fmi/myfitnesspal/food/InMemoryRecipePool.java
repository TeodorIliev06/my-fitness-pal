package com.fmi.myfitnesspal.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class InMemoryRecipePool implements RecipePool {

    private final Map<RecipeId, Recipe> recipes;

    public InMemoryRecipePool() {
        this.recipes = new HashMap<>();
    }

    @Override
    public void addRecipe(Recipe toAdd) {
        this.recipes.put(toAdd.getId(), toAdd);
    }

    @Override
    public void removeRecipe(RecipeId targetId) {
        if (!this.recipes.containsKey(targetId)) {
            throw new IllegalArgumentException(GlobalConstants.NOT_EXISTING_RECIPE_MESSAGE);
        }

        this.recipes.remove(targetId);
    }

    @Override
    public Recipe getRecipe(RecipeId targetId) {
        if (!this.recipes.containsKey(targetId)) {
            throw new IllegalArgumentException(GlobalConstants.NOT_EXISTING_RECIPE_MESSAGE);
        }

        return this.recipes.get(targetId);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return List.copyOf(this.recipes.values());
    }
}
