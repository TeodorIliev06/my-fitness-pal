package com.fmi.myfitnesspal.food;

import java.time.LocalDate;
import java.util.Map;

public record DailyMealCaloriesSummary(
        LocalDate date,
        Map<EatingTime, Double> caloriesByEatingTime
) {
    public DailyMealCaloriesSummary {
        caloriesByEatingTime = Map.copyOf(caloriesByEatingTime);
    }

    public double getCaloriesFor(EatingTime eatingTime) {
        return caloriesByEatingTime.getOrDefault(eatingTime, 0.0);
    }
}
