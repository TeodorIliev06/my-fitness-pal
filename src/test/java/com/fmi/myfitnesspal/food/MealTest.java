package com.fmi.myfitnesspal.food;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
    public void testAddFood() {
        meal.addFood(first, 2);
        meal.addFood(second, 1);

        List<Food> foods = meal.getFoods();

        assertEquals(2, foods.size());
        assertEquals(foods.get(0).getId(), new FoodId("apple", "green"));
        assertEquals(foods.get(1).getId(), new FoodId("banana", "medium and yellow"));
    }

    @Test
    public void testRemoveFood() {
        meal.addFood(first, 2);
        meal.addFood(second, 1);

        meal.removeFood(new FoodId("apple", "green"));

        List<Food> foods = meal.getFoods();

        assertEquals(1, foods.size());
        assertNotEquals(foods.get(0).getId(), new FoodId("apple", "green"));
        assertEquals(foods.get(0).getId(), new FoodId("banana", "medium and yellow"));
    }

    @Test
    public void testRemoveNonExistingFood() {
        meal.addFood(first, 2);

        assertThrows(IllegalArgumentException.class, () -> {
            meal.removeFood(new FoodId("banana", "medium and yellow"));
        });
    }
}
