package com.fmi.myfitnesspal.food;

import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;

public class FoodBase {

    public InMemoryFoodPool inMemoryFoodPool;
    public Food firstFood;
    public Food secondFood;

    /** Represents an employee.
     * setting up the base for the test class
     *
     */
    @BeforeEach
    public void setUp() {
        inMemoryFoodPool = new InMemoryFoodPool();
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
    }
}
