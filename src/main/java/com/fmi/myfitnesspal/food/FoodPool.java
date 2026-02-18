package com.fmi.myfitnesspal.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;

import java.util.HashMap;
import java.util.Map;

public final class FoodPool {

    private final Map<FoodId, Food> foods;

    public FoodPool() {
        this.foods = new HashMap<>();
    }

    public void addFood(Food foodToAdd) {
        if (contains(foodToAdd.getId())) {
            throw new IllegalArgumentException(GlobalConstants.ALREADY_ADDED_FOOD_MESSAGE);
        }

        this.foods.put(foodToAdd.getId(), foodToAdd);
    }

    public Food getFood(FoodId targetId) {
        if (!contains(targetId)) {
            throw new IllegalArgumentException(GlobalConstants.NOT_EXISTING_FOOD_MESSAGE);
        }

        return this.foods.get(targetId);
    }

    private boolean contains(FoodId foodId) {
        return this.foods.containsKey(foodId);
    }
}
