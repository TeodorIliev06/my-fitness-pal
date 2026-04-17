package com.fmi.myfitnesspal.food;

import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;

public class InMemoryFoodDiaryBase {

    public DailyFoodDiary diary;
    public Food firstFood;
    public Food secondFood;
    public Meal firstMeal;
    public Meal secondMeal;

    /** Represents an employee.
     * setting up the base for the test class
     *
     */

    @BeforeEach
    public void setUp() {
        diary = new DailyFoodDiary();
        firstFood = Food.builder(new FoodId("apple", "green"), 20, 50)
                .setFats(Optional.of(12.0))
                .setCarbs(Optional.of(14.0))
                .setProtein(Optional.of(13.0))
                .build();

        secondFood = Food.builder(new FoodId("banana", "medium and yellow"), 10, 40)
                .setFats(Optional.of(15.0))
                .setCarbs(Optional.of(15.0))
                .setProtein(Optional.of(11.0))
                .build();

        firstMeal = new Meal(new MealId("bolognese", "tomato sauce and meat"));
        secondMeal = new Meal(new MealId("carbonara", "cream sauce and meat"));
    }
}
