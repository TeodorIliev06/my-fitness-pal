package com.fmi.myfitnesspal.food;

import java.util.Optional;

public record WeeklyNutritionSummary(
        int weekNumber,
        int year,
        double calories,
        Optional<Double> protein,
        Optional<Double> carbs,
        Optional<Double> fats
) implements NutritionSummary {

}
