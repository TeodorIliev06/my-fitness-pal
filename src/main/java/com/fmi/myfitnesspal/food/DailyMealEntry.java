package com.fmi.myfitnesspal.food;

import java.time.LocalDate;

public record DailyMealEntry(
        LocalDate consumptionDate,
        EatingTime eatingTime,
        Meal meal
) {
}
