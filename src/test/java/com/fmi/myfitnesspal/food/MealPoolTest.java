package com.fmi.myfitnesspal.food;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MealPoolTest extends MealBase {

    @Test
    public void testAddMeal() {
        mealPool.addMeal(firstMeal);
        mealPool.addMeal(secondMeal);

        assertNotNull(mealPool.getMeal(new MealId("bolognese", "tomato sauce and meat")));
        assertNotNull(mealPool.getMeal(new MealId("carbonara", "cream sauce and meat")));
    }

    @Test
    public void testGetMeal() {
        mealPool.addMeal(firstMeal);

        assertEquals(firstMeal, mealPool.getMeal(new MealId("bolognese", "tomato sauce and meat")));
    }

    @Test
    public void testGetNonExistingMeal() {
        assertThrows(IllegalArgumentException.class, () -> {
            mealPool.getMeal(new MealId("bolognese", "tomato sauce and meat"));
        });
    }
}
