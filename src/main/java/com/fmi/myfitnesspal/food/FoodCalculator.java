package com.fmi.myfitnesspal.food;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.IsoFields;

public final class FoodCalculator {

    private static final int DAYS_IN_WEEK = 7;

    private FoodCalculator() {
    }

    public static Food recalculateFoodParameters(Food original, double servingsCount) {

        double newCalories = original.getCalories() * servingsCount;
        Optional<Double> newFats = original.getFats().map(fats -> fats * servingsCount);
        Optional<Double> newProtein = original.getProtein().map(protein -> protein * servingsCount);
        Optional<Double> newCarbs = original.getCarbs().map(carbs -> carbs * servingsCount);
        double newServingsSize = original.getServingSize() * servingsCount;

        return Food.builder(original.getId(), newServingsSize, newCalories)
                .setFats(newFats)
                .setProtein(newProtein)
                .setCarbs(newCarbs)
                .build();
    }

    public static WeeklyNutritionSummary getWeeklyNutritionSummary(FoodDiary diary, int weekNumber) {
        NutritionTotals totals = sumNutritionFrom(diary.getFoodsByWeekNumber(weekNumber));
        return new WeeklyNutritionSummary(
                weekNumber,
                totals.calories(),
                totals.protein(),
                totals.carbs(),
                totals.fats()
        );
    }

    public static WeeklyNutritionSummary getWeeklyNutritionSummary(FoodDiary diary, LocalDate date) {
        int weekNumber = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return getWeeklyNutritionSummary(diary, weekNumber);
    }

    public static DailyNutritionSummary getDailyNutritionSummary(FoodDiary diary, LocalDate date) {
        NutritionTotals totals = sumNutritionFrom(diary.getAllFoodsByDate(date));
        return new DailyNutritionSummary(
                date,
                totals.calories(),
                totals.protein(),
                totals.carbs(),
                totals.fats()
        );
    }

    public static List<DailyNutritionSummary> getDailyNutritionSummariesForWeek(
            FoodDiary diary, LocalDate targetDate) {
        LocalDate monday = targetDate.with(DayOfWeek.MONDAY);
        return IntStream.range(0, DAYS_IN_WEEK)
                .mapToObj(monday::plusDays)
                .map(day -> getDailyNutritionSummary(diary, day))
                .toList();
    }

    public static DailyMealCaloriesSummary getDailyMealCaloriesSummary(FoodDiary diary, LocalDate date) {
        return new DailyMealCaloriesSummary(date,
                sumCaloriesForMealTime(diary, date, EatingTime.BREAKFAST),
                sumCaloriesForMealTime(diary, date, EatingTime.LUNCH),
                sumCaloriesForMealTime(diary, date, EatingTime.DINNER),
                sumCaloriesForMealTime(diary, date, EatingTime.SNACKS)
        );
    }

    private static NutritionTotals sumNutritionFrom(List<Food> foods) {
        double calories = foods.stream().mapToDouble(Food::getCalories).sum();

        return new NutritionTotals(
                calories,
                sumMacro(foods, Food::getProtein),
                sumMacro(foods, Food::getCarbs),
                sumMacro(foods, Food::getFats)
        );
    }

    private static double sumCaloriesForMealTime(FoodDiary diary, LocalDate date, EatingTime eatingTime) {
        return diary.getAllFoodsByDateAndEatingTime(date, eatingTime)
                .stream()
                .mapToDouble(Food::getCalories)
                .sum();
    }

    private static Optional<Double> sumMacro(List<Food> foods, Function<Food, Optional<Double>> macroGetter) {
        boolean anyMacrosPresent = foods.stream().anyMatch(f -> macroGetter.apply(f).isPresent());

        if (!anyMacrosPresent) {
            return Optional.empty();
        }

        double total = foods.stream()
                .mapToDouble(f -> macroGetter.apply(f).orElse(0.0))
                .sum();

        return Optional.of(total);
    }

    private record NutritionTotals(
            double calories,
            Optional<Double> protein,
            Optional<Double> carbs,
            Optional<Double> fats
    ) implements NutritionSummary {

    }
}
