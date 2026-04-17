package com.fmi.myfitnesspal.food;

import java.time.LocalDate;
import java.util.List;

public interface FoodDiary {

    void addFood(LocalDate consumptionDate, EatingTime eatingTime, Food food, double numberOfServings);

    void addMeal(LocalDate consumptionDate, EatingTime eatingTime, Meal meal);

    void removeFood(LocalDate consumptionDate, EatingTime eatingTime, FoodId foodId);

    void removeMeal(LocalDate consumptionDate, EatingTime eatingTime, MealId mealId);

    List<Food> getFoodsByDateAndEatingTime(LocalDate consumptionDate, EatingTime eatingTime);

    List<Meal> getMealsByDateAndEatingTime(LocalDate consumptionDate, EatingTime eatingTime);

    List<Food> getFoodsByWeekNumber(int weekNumber);

    List<Food> getAllFoodsByDate(LocalDate consumptionDate);

    List<Food> getAllFoodsByDateAndEatingTime(LocalDate consumptionDate, EatingTime eatingTime);

    WeeklyNutritionSummary getWeeklyNutritionSummary(int weekNumber);

    WeeklyNutritionSummary getWeeklyNutritionSummary(LocalDate consumptionDate);

    DailyNutritionSummary getDailyNutritionSummary(LocalDate consumptionDate);

    List<DailyNutritionSummary> getDailyNutritionSummariesForWeek(LocalDate targetDate);

    DailyMealCaloriesSummary getDailyMealCaloriesSummary(LocalDate consumptionDate);

    List<DailyFoodEntry> getAllDailyFoodEntries();

    List<DailyMealEntry> getAllDailyMealEntries();
}
