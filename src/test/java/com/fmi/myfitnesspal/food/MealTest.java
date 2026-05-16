package com.fmi.myfitnesspal.food;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MealTest {
    private MealId mealId;
    private Meal meal;
    private Food first;
    private Food second;

    @BeforeEach
    public void setUp() {
        mealId = new MealId("fruits meal", "meal consisting of more than one fruit");
        meal = new Meal(mealId);
        first = Food.builder(new FoodId("apple", "green"), 20, 50)
                .setFats(Optional.of(12.0))
                .setCarbs(Optional.of(14.0))
                .setProtein(Optional.of(13.0))
                .build();

        second = Food.builder(new FoodId("banana", "medium and yellow"), 10, 40)
                .setFats(Optional.of(15.0))
                .setCarbs(Optional.of(15.0))
                .setProtein(Optional.of(11.0))
                .build();
    }

    @Test
    public void testAddFoodPortion() {
        meal.addFoodPortion(first, 2.0);
        meal.addFoodPortion(second, 1.0);

        List<FoodPortion> foodPortions = meal.getFoodPortions();

        assertEquals(2, foodPortions.size(),
                "Meal must contain exactly 2 food portions after two addFoodPortion calls");
        assertEquals(new FoodId("apple", "green"), foodPortions.get(0).food().getId(),
                "First food portion must be apple");
        assertEquals(new FoodId("banana", "medium and yellow"), foodPortions.get(1).food().getId(),
                "Second food portion must be banana");
    }

    @Test
    public void testRemoveFoodPortion() {
        meal.addFoodPortion(first, 2.0);
        meal.addFoodPortion(second, 1.0);

        meal.removeFoodPortion(new FoodId("apple", "green"));

        List<FoodPortion> foodPortions = meal.getFoodPortions();

        assertEquals(1, foodPortions.size(), "Meal must contain 1 food portion after removing apple");
        assertEquals(new FoodId("banana", "medium and yellow"), foodPortions.get(0).food().getId(),
                "Remaining food portion must be banana");
    }

    @Test
    public void testRemoveNonExistingFoodPortionThrows() {
        meal.addFoodPortion(first, 2.0);

        assertThrows(IllegalArgumentException.class,
                () -> meal.removeFoodPortion(new FoodId("banana", "medium and yellow")),
                "Removing an food portion not in the meal must throw IllegalArgumentException");
    }
}
