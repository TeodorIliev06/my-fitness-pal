package com.fmi.myfitnesspal.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class InMemoryFoodPool implements FoodPool {

    private final Map<FoodId, Food> foods;

    public InMemoryFoodPool() {
        this.foods = new HashMap<>();
    }

    @Override
    public void addFood(Food foodToAdd) {
        if (contains(foodToAdd.getId())) {
            throw new IllegalArgumentException(GlobalConstants.ALREADY_ADDED_FOOD_MESSAGE);
        }

        this.foods.put(foodToAdd.getId(), foodToAdd);
    }

    @Override
    public Food getFood(FoodId targetId) {
        if (!contains(targetId)) {
            throw new IllegalArgumentException(GlobalConstants.NOT_EXISTING_FOOD_MESSAGE);
        }

        return this.foods.get(targetId);
    }

    @Override
    public List<Food> getAllFoods() {
        return List.copyOf(foods.values());
    }

    private boolean contains(FoodId foodId) {
        return this.foods.containsKey(foodId);
    }
}
