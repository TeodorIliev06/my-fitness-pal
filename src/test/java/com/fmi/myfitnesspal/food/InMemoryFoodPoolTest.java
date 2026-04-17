package com.fmi.myfitnesspal.food;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public final class InMemoryFoodPoolTest extends FoodBase {
    @Test
    public void testAddFood() {
        inMemoryFoodPool.addFood(firstFood);
        inMemoryFoodPool.addFood(secondFood);

        assertNotNull(inMemoryFoodPool.getFood(new FoodId("apple", "green")));
        assertNotNull(inMemoryFoodPool.getFood(new FoodId("banana", "medium and yellow")));
    }

    @Test
    public void testGetFood() {
        inMemoryFoodPool.addFood(firstFood);

        assertEquals(firstFood, inMemoryFoodPool.getFood(new FoodId("apple", "green")));
    }

    @Test
    public void testAddExistingFood() {
        inMemoryFoodPool.addFood(firstFood);

        assertThrows(IllegalArgumentException.class, () -> {
            inMemoryFoodPool.addFood(firstFood);
        });
    }

    @Test
    public void testGetNonExistingFood() {
        assertThrows(IllegalArgumentException.class, () -> {
            inMemoryFoodPool.getFood(new FoodId("apple", "green"));
        });
    }
}
