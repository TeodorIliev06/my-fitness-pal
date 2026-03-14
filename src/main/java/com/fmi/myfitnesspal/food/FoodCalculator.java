package com.fmi.myfitnesspal.food;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.time.LocalDate;
import java.time.temporal.IsoFields;

public final class FoodCalculator {

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
        List<Food> weeklyFoods = diary.getFoodsByWeekNumber(weekNumber);

        double calories = weeklyFoods.stream()
                .mapToDouble(Food::getCalories)
                .sum();

        Optional<Double> protein = sumMacro(weeklyFoods, Food::getProtein);
        Optional<Double> carbs = sumMacro(weeklyFoods, Food::getCarbs);
        Optional<Double> fats = sumMacro(weeklyFoods, Food::getFats);

        return new WeeklyNutritionSummary(weekNumber, calories, protein, carbs, fats);
    }

    public static WeeklyNutritionSummary getWeeklyNutritionSummary(FoodDiary diary, LocalDate date) {
        int weekNumber = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return getWeeklyNutritionSummary(diary, weekNumber);
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
}
