package com.fmi.myfitnesspal.food;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class MealPoolTest extends MealBase {

    @Test
    public void testAddMeal() {
        mealPool.addMeal(firstMeal);
        mealPool.addMeal(secondMeal);

        assertNotNull(mealPool.getMeal(new MealId("bolognese", "tomato sauce and meat")),
                "MealPool must return bolognese after it was added");
        assertNotNull(mealPool.getMeal(new MealId("carbonara", "cream sauce and meat")),
                "MealPool must return carbonara after it was added");
    }

    @Test
    public void testGetMeal() {
        mealPool.addMeal(firstMeal);

        assertEquals(firstMeal, mealPool.getMeal(new MealId("bolognese", "tomato sauce and meat")),
                "getMeal must return the exact Meal instance that was added");
    }

    @Test
    public void testGetNonExistingMealThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> mealPool.getMeal(new MealId("bolognese", "tomato sauce and meat")),
                "getMeal must throw IllegalArgumentException when the meal does not exist in the pool");
    }

    @Test
    public void testRemoveMealDeletesTemplateFromPool() {
        mealPool.addMeal(firstMeal);
        MealId bolognese = new MealId("bolognese", "tomato sauce and meat");

        mealPool.removeMeal(bolognese);

        assertTrue(mealPool.getAllMeals().isEmpty(),
                "Pool must be empty after the only meal template is removed");
    }

    @Test
    public void testRemoveNonExistingMealThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> mealPool.removeMeal(new MealId("bolognese", "tomato sauce and meat")),
                "removeMeal must throw IllegalArgumentException when the meal does not exist in the pool");
    }
}
