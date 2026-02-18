package com.fmi.myfitnesspal.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;

import java.util.HashMap;
import java.util.Map;

public final class MealPool {

    private final Map<MealId, Meal> meals;

    public MealPool() {
        this.meals = new HashMap<>();
    }

    public void addMeal(Meal toAdd) {
        this.meals.put(toAdd.getId(), toAdd);
    }

    public Meal getMeal(MealId targetId) {
        if (!this.meals.containsKey(targetId)) {
            throw new IllegalArgumentException(GlobalConstants.NOT_EXISTING_MEAL_MESSAGE);
        }

        return this.meals.get(targetId);
    }
}
