package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.EatingTime;

import java.time.LocalDate;
import java.util.List;

public record DailyMealEntryDto(
        LocalDate date,
        EatingTime eatingTime,
        String name,
        String description,
        List<FoodDto> foods
) {
}
