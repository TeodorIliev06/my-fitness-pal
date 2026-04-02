package com.fmi.myfitnesspal.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DailyFoodDiary {

    private final Map<EatingTime, ArrayList<Food>> foods;
    private final Map<EatingTime, ArrayList<Meal>> meals;

    public DailyFoodDiary() {
        this.foods = new HashMap<>();
        this.meals = new HashMap<>();

        for (EatingTime eatingTime : EatingTime.values()) {
            this.foods.put(eatingTime, new ArrayList<>());
            this.meals.put(eatingTime, new ArrayList<>());
        }
    }

    public void removeFood(EatingTime eatingTime, FoodId id) {
        if (foodIsMissing(eatingTime, id)) {
            throw new IllegalArgumentException(GlobalConstants.FOOD_NOT_CONSUMED_MESSAGE);
        }

        this.foods.get(eatingTime).removeIf(food -> food.getId().equals(id));
    }

    public void removeMeal(EatingTime eatingTime, MealId id) {
        if (mealIsMissing(eatingTime, id)) {
            throw new IllegalArgumentException(GlobalConstants.MEAL_NOT_CONSUMED_MESSAGE);
        }

        this.meals.get(eatingTime).removeIf(meal -> meal.getId().equals(id));
    }

    public void addFood(EatingTime eatingTime, Food food, double numberOfServings) {
        this.foods.get(eatingTime).add(food.scaledBy(numberOfServings));
    }

    public void addMeal(EatingTime eatingTime, Meal meal) {
        this.meals.get(eatingTime).add(meal);
    }

    public List<Meal> getMealsByEatingTime(EatingTime eatingTime) {
        return Collections.unmodifiableList(this.meals.get(eatingTime));
    }

    public List<Food> getFoodsByEatingTime(EatingTime eatingTime) {
        return Collections.unmodifiableList(this.foods.get(eatingTime));
    }

    public List<Food> getAllFoods() {
        return Stream.concat(
                foods.values().stream().flatMap(List::stream),
                meals.values().stream().flatMap(List::stream).flatMap(meal -> meal.getFoods().stream())
                ).collect(Collectors.toList());
    }

    public List<Food> getAllFoodsByEatingTime(EatingTime eatingTime) {
        return Stream.concat(
                foods.get(eatingTime).stream(),
                meals.get(eatingTime).stream().flatMap(meal -> meal.getFoods().stream())
        ).collect(Collectors.toList());
    }

    private boolean foodIsMissing(EatingTime eatingTime, FoodId id) {
        return this.foods.get(eatingTime).stream().noneMatch(food -> food.getId().equals(id));
    }

    private boolean mealIsMissing(EatingTime eatingTime, MealId id) {
        return this.meals.get(eatingTime).stream().noneMatch(meal -> meal.getId().equals(id));
    }
}
