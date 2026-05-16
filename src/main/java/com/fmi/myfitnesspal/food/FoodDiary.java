package com.fmi.myfitnesspal.food;

import java.time.LocalDate;
import java.util.List;

public interface FoodDiary {

    void addFood(LocalDate consumptionDate, EatingTime eatingTime, Food food, double numberOfServings);

    void addFoodPortions(LocalDate consumptionDate, EatingTime eatingTime, List<FoodPortion> foodPortions);

    void removeFood(LocalDate consumptionDate, EatingTime eatingTime, FoodId foodId);

    List<Food> getFoodsByDateAndEatingTime(LocalDate consumptionDate, EatingTime eatingTime);

    List<Food> getFoodsByWeekNumber(int weekNumber, int year);

    List<Food> getAllFoodsByDate(LocalDate consumptionDate);

    List<Food> getAllFoodsByDateAndEatingTime(LocalDate consumptionDate, EatingTime eatingTime);

    WeeklyNutritionSummary getWeeklyNutritionSummary(int weekNumber, int year);

    WeeklyNutritionSummary getWeeklyNutritionSummary(LocalDate consumptionDate);

    DailyNutritionSummary getDailyNutritionSummary(LocalDate consumptionDate);

    List<DailyNutritionSummary> getDailyNutritionSummariesForWeek(LocalDate targetDate);

    DailyMealCaloriesSummary getDailyMealCaloriesSummary(LocalDate consumptionDate);

    List<DailyFoodEntry> getAllDailyFoodEntries();
}
