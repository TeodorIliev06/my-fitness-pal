package com.fmi.myfitnesspal.food;

import org.junit.jupiter.api.BeforeEach;

public class MealBase {
    public MealPool mealPool;
    public Meal firstMeal;
    public Meal secondMeal;

    /** Represents an employee.
     * setting up the base for the test class
     *
     */

    @BeforeEach
    public void setUp() {
        mealPool = new InMemoryMealPool();
        firstMeal = new Meal(new MealId("bolognese", "tomato sauce and meat"));
        secondMeal = new Meal(new MealId("carbonara", "cream sauce and meat"));
    }
}
