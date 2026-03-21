package com.fmi.myfitnesspal.food;

import java.util.Optional;

public interface NutritionSummary {
    Optional<Double> protein();
    Optional<Double> carbs();
    Optional<Double> fats();
}
