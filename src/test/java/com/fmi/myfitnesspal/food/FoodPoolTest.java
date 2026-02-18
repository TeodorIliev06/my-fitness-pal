package com.fmi.myfitnesspal.food;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public final class FoodPoolTest extends FoodBase {
    @Test
    public void testAddFood() {
        foodPool.addFood(firstFood);
        foodPool.addFood(secondFood);

        assertNotNull(foodPool.getFood(new FoodId("apple", "green")));
        assertNotNull(foodPool.getFood(new FoodId("banana", "medium and yellow")));
    }

    @Test
    public void testGetFood() {
        foodPool.addFood(firstFood);

        assertEquals(firstFood, foodPool.getFood(new FoodId("apple", "green")));
    }

    @Test
    public void testAddExistingFood() {
        foodPool.addFood(firstFood);

        assertThrows(IllegalArgumentException.class, () -> {
            foodPool.addFood(firstFood);
        });
    }

    @Test
    public void testGetNonExistingFood() {
        assertThrows(IllegalArgumentException.class, () -> {
            foodPool.getFood(new FoodId("apple", "green"));
        });
    }
}
