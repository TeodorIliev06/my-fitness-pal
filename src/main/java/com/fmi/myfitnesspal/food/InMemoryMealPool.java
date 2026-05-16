package com.fmi.myfitnesspal.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class InMemoryMealPool implements MealPool {

    private final Map<MealId, Meal> meals;

    public InMemoryMealPool() {
        this.meals = new HashMap<>();
    }

    @Override
    public void addMeal(Meal toAdd) {
        this.meals.put(toAdd.getId(), toAdd);
    }

    @Override
    public void removeMeal(MealId targetId) {
        if (!this.meals.containsKey(targetId)) {
            throw new IllegalArgumentException(GlobalConstants.NOT_EXISTING_MEAL_MESSAGE);
        }

        this.meals.remove(targetId);
    }

    @Override
    public Meal getMeal(MealId targetId) {
        if (!this.meals.containsKey(targetId)) {
            throw new IllegalArgumentException(GlobalConstants.NOT_EXISTING_MEAL_MESSAGE);
        }

        return this.meals.get(targetId);
    }

    @Override
    public List<Meal> getAllMeals() {
        return List.copyOf(this.meals.values());
    }
}
