package com.fmi.myfitnesspal.food;

import java.util.List;

public interface MealPool {

    void addMeal(Meal toAdd);

    void removeMeal(MealId targetId);

    Meal getMeal(MealId targetId);

    List<Meal> getAllMeals();
}
