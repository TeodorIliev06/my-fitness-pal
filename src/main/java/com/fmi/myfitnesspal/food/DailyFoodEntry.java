package com.fmi.myfitnesspal.food;

import java.time.LocalDate;

public record DailyFoodEntry(
        LocalDate consumptionDate,
        EatingTime eatingTime,
        Food food
) {
}
