package com.fmi.myfitnesspal.food;

import java.time.LocalDate;
import java.util.Optional;

public record DailyNutritionSummary(
        LocalDate date,
        double calories,
        Optional<Double> protein,
        Optional<Double> carbs,
        Optional<Double> fats
) implements NutritionSummary {

}
