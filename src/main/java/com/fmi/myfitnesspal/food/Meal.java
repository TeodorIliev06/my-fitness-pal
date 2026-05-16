package com.fmi.myfitnesspal.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Meal {

    private final MealId id;
    private final List<FoodPortion> foodPortions;

    public Meal(MealId id) {
        this.id = id;
        this.foodPortions = new ArrayList<>();
    }

    public MealId getId() {
        return this.id;
    }

    public List<FoodPortion> getFoodPortions() {
        return Collections.unmodifiableList(this.foodPortions);
    }

    public void addFoodPortion(Food food, double servingsUsed) {
        this.foodPortions.add(new FoodPortion(food, servingsUsed));
    }

    public void removeFoodPortion(FoodId targetId) {
        if (isMissing(targetId)) {
            throw new IllegalArgumentException(GlobalConstants.NOT_EXISTING_FOOD_IN_MEAL_MESSAGE);
        }

        this.foodPortions.removeIf(foodPortion -> foodPortion.food().getId().equals(targetId));
    }

    private boolean isMissing(FoodId targetId) {
        return this.foodPortions.stream().noneMatch(i -> i.food().getId().equals(targetId));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Meal consists of:");
        sb.append(System.lineSeparator());
        for (FoodPortion foodPortion : this.foodPortions) {
            sb.append("   ");
            sb.append(foodPortion.food().toString());
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }
}
