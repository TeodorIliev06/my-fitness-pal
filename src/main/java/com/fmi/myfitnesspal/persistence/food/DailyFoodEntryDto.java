package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.EatingTime;

import java.time.LocalDate;

public record DailyFoodEntryDto(
    LocalDate date,
    EatingTime eatingTime,
    FoodDto food
) {

}
