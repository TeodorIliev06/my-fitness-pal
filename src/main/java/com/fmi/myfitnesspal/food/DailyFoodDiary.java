package com.fmi.myfitnesspal.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.stream.Collectors;

public final class DailyFoodDiary {

    private final Map<EatingTime, ArrayList<Food>> foods;

    public DailyFoodDiary() {
        this.foods = new HashMap<>();

        for (EatingTime eatingTime : EatingTime.values()) {
            this.foods.put(eatingTime, new ArrayList<>());
        }
    }

    public void removeFood(EatingTime eatingTime, FoodId id) {
        if (foodIsMissing(eatingTime, id)) {
            throw new IllegalArgumentException(GlobalConstants.FOOD_NOT_CONSUMED_MESSAGE);
        }

        this.foods.get(eatingTime).removeIf(food -> food.getId().equals(id));
    }

    public void addFood(EatingTime eatingTime, Food food, double numberOfServings) {
        this.foods.get(eatingTime).add(food.scaledBy(numberOfServings));
    }

    public List<Food> getFoodsByEatingTime(EatingTime eatingTime) {
        return Collections.unmodifiableList(this.foods.get(eatingTime));
    }

    public List<Food> getAllFoods() {
        return this.foods.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private boolean foodIsMissing(EatingTime eatingTime, FoodId id) {
        return this.foods.get(eatingTime).stream().noneMatch(food -> food.getId().equals(id));
    }
}
