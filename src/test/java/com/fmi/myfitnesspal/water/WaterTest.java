package com.fmi.myfitnesspal.water;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class WaterTest {
    @Test
    void testAddWaterEmptyWater() {
        Water water = new Water();
        int quantity = 250;

        water.add(quantity);
        assertEquals(quantity, water.getQuantity(), "The water was not added");
    }

    @Test
    void testAddPortionEmptyWater() {
        Water water = new Water();
        Portion portion = Portion.P_250;

        water.add(portion);
        assertEquals(portion.getQuantity(), water.getQuantity(), "The water was not added");
    }

    @Test
    void testAddWaterExistingWater() {
        int initialQuantity = 200;
        Water water = new Water(initialQuantity);

        int quantity = 250;
        water.add(quantity);

        assertEquals(initialQuantity + quantity, water.getQuantity(), "The water was not added");
    }

    @Test
    void testAddPortionExistingWater() {
        int initialQuantity = 200;
        Water water = new Water(initialQuantity);

        Portion portion = Portion.P_250;
        water.add(portion);

        assertEquals(initialQuantity + portion.getQuantity(), water.getQuantity(), "The water was not added");
    }

    @Test
    void testAddWaterManyTimes() {
        Water water = new Water();

        int addedFirst = 200;
        int addedSecond = 250;
        water.add(addedFirst);
        water.add(addedSecond);

        assertEquals(addedFirst + addedSecond, water.getQuantity(), "The water was not added for the second time");
    }

    @Test
    void testAddPortionManyTimes() {
        Water water = new Water();

        Portion addedFirst = Portion.P_250;
        Portion addedSecond = Portion.P_500;
        water.add(addedFirst);
        water.add(addedSecond);

        assertEquals(addedFirst.getQuantity() + addedSecond.getQuantity(), water.getQuantity(),
                "The water was not added for the second time");
    }

    @Test
    void testAddWaterNegativeQuantity() {
        Water water = new Water();
        int quantity = -100;

        assertThrows(IllegalArgumentException.class, () -> water.add(quantity),
                "The method should throw IllegalArgumentException when tying to add negative quantity");
    }

    @Test
    void testRemoveWaterFromEmptyWater() {
        Water water = new Water();
        int quantity = 250;

        assertThrows(IllegalArgumentException.class, () -> water.remove(quantity),
                "The method should throw IllegalArgumentException when trying to remove water from empty water");
    }

    @Test
    void testRemovePortionFromEmptyWater() {
        Water water = new Water();
        Portion portion = Portion.P_250;

        assertThrows(IllegalArgumentException.class, () -> water.remove(portion),
                "The method should throw IllegalArgumentException when trying to remove portion from empty water");
    }

    @Test
    void testRemoveWaterFromExistingWater() {
        int initialQuantity = 500;
        Water water = new Water(initialQuantity);

        int quantity = 250;
        water.remove(quantity);

        assertEquals(initialQuantity - quantity, water.getQuantity(), "The water was not removed");
    }

    @Test
    void testRemovePortionFromExistingWater() {
        int initialQuantity = 500;
        Water water = new Water(initialQuantity);

        Portion portion = Portion.P_250;
        water.remove(portion);

        assertEquals(initialQuantity - portion.getQuantity(), water.getQuantity(), "The water was not removed");
    }

    @Test
    void testRemoveWaterManyTimes() {
        int initialQuantity = 1000;
        Water water = new Water(initialQuantity);

        int removedFirst = 200;
        int removedSecond = 250;
        water.remove(removedFirst);
        water.remove(removedSecond);

        assertEquals(initialQuantity - removedFirst - removedSecond, water.getQuantity(),
                "The water was not removed for the second time");
    }

    @Test
    void testRemovePortionManyTimes() {
        int initialQuantity = 1000;
        Water water = new Water(initialQuantity);

        Portion removedFirst = Portion.P_250;
        Portion removedSecond = Portion.P_500;
        water.remove(removedFirst);
        water.remove(removedSecond);

        assertEquals(initialQuantity - removedFirst.getQuantity() - removedSecond.getQuantity(), water.getQuantity(),
                "The water was not removed for the second time");
    }

    @Test
    void testRemoveWaterNegativeQuantity() {
        Water water = new Water();
        int quantity = -100;

        assertThrows(IllegalArgumentException.class, () -> water.remove(quantity),
                "The method should throw IllegalArgumentException when trying to remove negative quantity");
    }

    @Test
    void testInitializeNegativeQuantity() {
        int initialQuantity = -100;
        assertThrows(IllegalArgumentException.class, () -> new Water(initialQuantity),
                "The method should throw IllegalArgumentException when trying to initialize with negative quantity");
    }
}
