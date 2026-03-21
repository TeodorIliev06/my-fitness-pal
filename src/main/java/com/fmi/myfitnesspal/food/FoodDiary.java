package com.fmi.myfitnesspal.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class FoodDiary {

    private final Map<LocalDate, DailyFoodDiary> diary;

    public FoodDiary() {
        this.diary = new HashMap<>();
    }

    public void removeFood(LocalDate date, EatingTime eatingTime, FoodId foodId) {
        getValidatedDailyDiary(date).removeFood(eatingTime, foodId);
    }

    public void removeMeal(LocalDate date, EatingTime eatingTime, MealId id) {
        getValidatedDailyDiary(date).removeMeal(eatingTime, id);
    }

    public void addFood(LocalDate date, EatingTime eatingTime, Food food, double numberOfServings) {
        getOrCreateDailyDiary(date).addFood(eatingTime, food, numberOfServings);
    }

    public void addMeal(LocalDate date, EatingTime eatingTime, Meal meal) {
        getOrCreateDailyDiary(date).addMeal(eatingTime, meal);
    }

    public List<Food> getFoodsByDateAndEatingTime(LocalDate date, EatingTime eatingTime) {
        return getValidatedDailyDiary(date).getFoodsByEatingTime(eatingTime);
    }

    public List<Meal> getMealsByDateAndEatingTime(LocalDate date, EatingTime eatingTime) {
        return getValidatedDailyDiary(date).getMealsByEatingTime(eatingTime);
    }

    public List<Food> getFoodsByWeekNumber(int weekNumber) {
        return diary.keySet().stream()
                .filter(date -> date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == weekNumber)
                .flatMap(date -> diary.get(date).getAllFoods().stream())
                .collect(Collectors.toList());
    }

    public List<Food> getAllFoodsByDate(LocalDate date) {
        return findDailyDiary(date)
                .map(DailyFoodDiary::getAllFoods)
                .orElse(List.of());
    }

    public List<Food> getAllFoodsByDateAndEatingTime(LocalDate date, EatingTime eatingTime) {
        return findDailyDiary(date)
                .map(dailyDiary -> dailyDiary.getAllFoodsByEatingTime(eatingTime))
                .orElse(List.of());
    }

    private DailyFoodDiary getOrCreateDailyDiary(LocalDate date) {
        return this.diary.computeIfAbsent(date, d -> new DailyFoodDiary());
    }

    private DailyFoodDiary getValidatedDailyDiary(LocalDate date) {
        validateDate(date);
        return this.diary.get(date);
    }

    private Optional<DailyFoodDiary> findDailyDiary(LocalDate date) {
        return Optional.ofNullable(this.diary.get(date));
    }

    private void validateDate(LocalDate date) {
        if (!this.diary.containsKey(date)) {
            throw new IllegalArgumentException(GlobalConstants.NOT_EXISTING_DATE_IN_FOOD_DIARY_MESSAGE);
        }
    }
}
