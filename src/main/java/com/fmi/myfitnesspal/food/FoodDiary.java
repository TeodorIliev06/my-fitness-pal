package com.fmi.myfitnesspal.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FoodDiary {

    private Map<LocalDate, DailyFoodDiary> diary;

    public FoodDiary() {
        this.diary = new HashMap<>();
    }

    public void removeFood(LocalDate date, EatingTime eatingTime, FoodId foodId) {
        validateDate(date);
        this.diary.get(date).removeFood(eatingTime, foodId);
    }

    public void removeMeal(LocalDate date, EatingTime eatingTime, MealId id) {
        validateDate(date);
        this.diary.get(date).removeMeal(eatingTime, id);
    }

    public void addFood(LocalDate date, EatingTime eatingTime, Food food, double numberOfServings) {

        if (isDailyDiaryEmpty(date)) {
            this.diary.put(date, new DailyFoodDiary());
        }

        this.diary.get(date).addFood(eatingTime, food, numberOfServings);
    }

    public void addMeal(LocalDate date, EatingTime eatingTime, Meal meal) {

        if (isDailyDiaryEmpty(date)) {
            this.diary.put(date, new DailyFoodDiary());
        }

        this.diary.get(date).addMeal(eatingTime, meal);
    }

    public List<Food> getFoodsByDateAndEatingTime(LocalDate date, EatingTime eatingTime) {
        validateDate(date);
        return this.diary.get(date).getFoodsByEatingTime(eatingTime);
    }

    public List<Meal> getMealsByDateAndEatingTime(LocalDate date, EatingTime eatingTime) {
        validateDate(date);
        return this.diary.get(date).getMealsByEatingTime(eatingTime);
    }

    private boolean isDailyDiaryEmpty(LocalDate date) {
        return !this.diary.containsKey(date);
    }

    private void validateDate(LocalDate date) {
        if (isDailyDiaryEmpty(date)) {
            throw new IllegalArgumentException(GlobalConstants.NOT_EXISTING_DATE_IN_FOOD_DIARY_MESSAGE);
        }
    }
}
