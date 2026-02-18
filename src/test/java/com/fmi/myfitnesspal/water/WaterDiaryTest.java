package com.fmi.myfitnesspal.water;

import com.fmi.myfitnesspal.exception.WaterNotLoggedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class WaterDiaryTest {
    private static final LocalDate DATE = LocalDate.now();

    private WaterDiary diary;

    @BeforeEach
    void initializeDiary() {
        this.diary = new WaterDiary();
    }

    @Test
    void testGetDailyWaterNoWaterAdded() {
        assertEquals(diary.getDailyWater(DATE), 0,
                "The daily water should be 0 when no water is added");
    }

    @Test
    void testAddPortionNoExistingWater() {
        Portion portion = Portion.P_250;

        diary.addWater(DATE, portion);
        assertEquals(diary.getDailyWater(DATE), portion.getQuantity(),
                "The water portion is not added to the diary");
    }

    @Test
    void testAddWaterNoExistingWater() {
        int quantity = 245;

        diary.addWater(DATE, quantity);
        assertEquals(diary.getDailyWater(DATE), quantity,
                "The water was not added to the diary");
    }

    @Test
    void testAddPortionAlreadyExistingWater() {
        int initialWater = 330;
        diary.addWater(DATE, initialWater);

        Portion portion = Portion.P_250;
        diary.addWater(DATE, portion);

        assertEquals(diary.getDailyWater(DATE), initialWater + portion.getQuantity(),
                "Portion is not added when there is already logged water for the current day");
    }

    @Test
    void testAddWaterAlreadyExistingWater() {
        int initialWater = 330;
        diary.addWater(DATE, initialWater);

        int quantity = 200;
        diary.addWater(DATE, quantity);

        assertEquals(diary.getDailyWater(DATE), initialWater + quantity,
                "Water is not added when there is already logged water for the current day");
    }

    @Test
    void testAddWaterNegativeAmount() {
        int quantity = -100;
        assertThrows(IllegalArgumentException.class, () -> diary.addWater(DATE, quantity),
                "The method should throw exception when adding negative amount");
    }

    @Test
    void testRemovePortionNoExistingWater() {
        Portion portion = Portion.P_250;

        assertThrows(WaterNotLoggedException.class, () -> diary.removeWater(DATE, portion),
                "The method should throw exception when trying to remove water portion when no water is logged");

    }

    @Test
    void testRemoveWaterNoExistingWater() {
        int quantity = 245;

        assertThrows(WaterNotLoggedException.class, () -> diary.removeWater(DATE, quantity),
                "The method should throw exception when trying to remove water when no water is logged");
    }

    @Test
    void testRemovePortionLoggedWater() throws WaterNotLoggedException {
        int initialWater = 330;
        diary.addWater(DATE, initialWater);

        Portion portion = Portion.P_250;
        diary.removeWater(DATE, portion);

        assertEquals(diary.getDailyWater(DATE), initialWater - portion.getQuantity(),
                "Portion is not removed when there is logged water for the current day");
    }

    @Test
    void testRemoveWaterLoggedWater() throws WaterNotLoggedException {
        int initialWater = 330;
        diary.addWater(DATE, initialWater);

        int quantity = 200;
        diary.removeWater(DATE, quantity);

        assertEquals(diary.getDailyWater(DATE), initialWater - quantity,
                "Water is not removed when there is logged water for the current day");
    }

    @Test
    void testRemoveWaterNegativeAmount() {
        diary.addWater(DATE, 200);

        int quantity = -100;
        assertThrows(IllegalArgumentException.class, () -> diary.removeWater(DATE, quantity),
                "The method should throw exception when trying to remove negative amount");
    }
}
