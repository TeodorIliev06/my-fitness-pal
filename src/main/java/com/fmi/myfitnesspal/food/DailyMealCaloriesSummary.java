package com.fmi.myfitnesspal.food;

import java.time.LocalDate;

public record DailyMealCaloriesSummary(
        LocalDate date,
        double breakfastCalories,
        double lunchCalories,
        double dinnerCalories,
        double snackCalories
) {

}
