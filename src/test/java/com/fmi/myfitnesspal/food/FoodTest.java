package com.fmi.myfitnesspal.food;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FoodTest {

    @Test
    public void testScaledByWithAllMacrosSet() {
        FoodId id = new FoodId("apple", "green");
        Food initial = Food.builder(id, 20, 50)
                .setFats(Optional.of(12.0))
                .setCarbs(Optional.of(14.0))
                .setProtein(Optional.of(13.0))
                .build();

        Food scaled = initial.scaledBy(2);

        assertEquals(id, scaled.getId(),
                "Scaling must preserve the original FoodId");
        assertEquals(40, scaled.getServingSize(),
                "Serving size must be multiplied by the servings count");
        assertEquals(100, scaled.getCalories(),
                "Calories must be multiplied by the servings count");
        assertEquals(Optional.of(24.0), scaled.getFats(),
                "Fats must be multiplied by the servings count");
        assertEquals(Optional.of(26.0), scaled.getProtein(),
                "Protein must be multiplied by the servings count");
        assertEquals(Optional.of(28.0), scaled.getCarbs(),
                "Carbs must be multiplied by the servings count");
    }

    @Test
    public void testScaledByWithPartialMacrosPreservesAbsentMacros() {
        FoodId id = new FoodId("apple", "green");
        Food initial = Food.builder(id, 20, 50)
                .setFats(Optional.of(12.0))
                .build();

        Food scaled = initial.scaledBy(2);

        assertEquals(id, scaled.getId(),
                "Scaling must preserve the original FoodId");
        assertEquals(40, scaled.getServingSize(),
                "Serving size must be multiplied by the servings count");
        assertEquals(100, scaled.getCalories(),
                "Calories must be multiplied by the servings count");
        assertEquals(Optional.of(24.0), scaled.getFats(),
                "Present fats must be multiplied by the servings count");
        assertTrue(scaled.getCarbs().isEmpty(),
                "Absent carbs must remain absent after scaling");
        assertTrue(scaled.getProtein().isEmpty(),
                "Absent protein must remain absent after scaling");
    }
}
