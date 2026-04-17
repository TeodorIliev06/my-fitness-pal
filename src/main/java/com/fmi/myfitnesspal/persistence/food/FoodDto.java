package com.fmi.myfitnesspal.persistence.food;

public record FoodDto(
        String brand,
        String description,
        double servingSize,
        double calories,
        Double fats,
        Double protein,
        Double carbs
) {
}
