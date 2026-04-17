package com.fmi.myfitnesspal.food;

import java.util.List;

public interface FoodPool {
    void addFood(Food food);

    Food getFood(FoodId targetFoodId);

    List<Food> getAllFoods();
}
