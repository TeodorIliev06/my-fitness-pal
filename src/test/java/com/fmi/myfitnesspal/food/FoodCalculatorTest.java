package com.fmi.myfitnesspal.food;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FoodCalculatorTest {

    @Test
    public void testRecalculateMethodWithAllParametersSet() {
        FoodId id = new FoodId("apple", "green");
        Food initial = Food.builder(id, 20, 50)
                .setFats(Optional.of(12.0))
                .setCarbs(Optional.of(14.0))
                .setProtein(Optional.of(13.0))
                .build();

        Food modified = FoodCalculator.recalculateFoodParameters(initial, 2);

        assertEquals(modified.getId(), id);
        assertEquals(modified.getServingSize(), 40);
        assertEquals(modified.getCalories(), 100);
        assertEquals(modified.getFats(), Optional.of(24.0));
        assertEquals(modified.getProtein(), Optional.of(26.0));
        assertEquals(modified.getCarbs(), Optional.of(28.0));
    }

    @Test
    public void testRecalculateMethodWithoutAllParametersSet() {
        FoodId id = new FoodId("apple", "green");
        Food initial = Food.builder(id, 20, 50)
                .setFats(Optional.of(12.0))
                .build();

        Food modified = FoodCalculator.recalculateFoodParameters(initial, 2);

        assertEquals(modified.getId(), id);
        assertEquals(modified.getServingSize(), 40);
        assertEquals(modified.getCalories(), 100);
        assertEquals(modified.getFats(), Optional.of(24.0));
        assertTrue(modified.getCarbs().isEmpty());
        assertTrue(modified.getProtein().isEmpty());
    }
}
