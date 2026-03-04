package com.fmi.myfitnesspal.food;

import java.util.Optional;

public record WeeklyNutritionSummary(
        int weekNumber,
        double calories,
        Optional<Double> protein,
        Optional<Double> carbs,
        Optional<Double> fats
) {

}
