package com.fmi.myfitnesspal.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Meal {

    private final MealId id;
    private final List<Food> foods;

    public Meal(MealId id) {
        this.id = id;
        this.foods = new ArrayList<>();
    }

    public MealId getId() {
        return this.id;
    }

    public List<Food> getFoods() {
        return Collections.unmodifiableList(this.foods);
    }

    public void addFood(Food food, double numberOfServings) {
        Food modifiedFood = FoodCalculator.recalculateFoodParameters(food, numberOfServings);
        this.foods.add(modifiedFood);
    }

    public void removeFood(FoodId targetId) {
        if (isMissing(targetId)) {
            throw new IllegalArgumentException(GlobalConstants.NOT_EXISTING_FOOD_IN_MEAL_MESSAGE);
        }

        this.foods.removeIf(food -> food.getId().equals(targetId));
    }

    private boolean isMissing(FoodId targetId) {
        return this.foods.stream().noneMatch(food -> food.getId().equals(targetId));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Meal consists of:");
        sb.append(System.lineSeparator());
        for (Food food : this.foods) {
            sb.append("   ");
            sb.append(food.toString());
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }
}
