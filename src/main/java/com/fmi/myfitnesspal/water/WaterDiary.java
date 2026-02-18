package com.fmi.myfitnesspal.water;

import com.fmi.myfitnesspal.exception.WaterNotLoggedException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WaterDiary {
    private final Map<LocalDate, Water> diary;

    public WaterDiary() {
        this(new HashMap<>());
    }

    public WaterDiary(Map<LocalDate, Water> diary) {
        this.diary = Objects.requireNonNull(diary);
    }

    /**
     * Adds water intake for a specific date.
     *
     * @param date the date for which water intake is being recorded
     * @param portion the portion of water to be added (represented by Portion)
     */
    public void addWater(LocalDate date, Portion portion) {
        diary.putIfAbsent(date, new Water());
        diary.get(date).add(portion);
    }

    /**
     * Adds a specified quantity of water for a given date.
     *
     * @param date the date for which water quantity is being recorded
     * @param quantity the quantity of water (in milliliters) to be added
     */
    public void addWater(LocalDate date, int quantity) {
        diary.putIfAbsent(date, new Water());
        diary.get(date).add(quantity);
    }

    /**
     * Removes a specified quantity of water for a given date.
     *
     * @param date the date for which water quantity is being removed
     * @param quantity the quantity of water (in milliliters) to be removed
     * @throws WaterNotLoggedException if no water intake is recorded for the given date
     */
    public void removeWater(LocalDate date, int quantity) throws WaterNotLoggedException {
        if (!diary.containsKey(date)) {
            throw new WaterNotLoggedException("No water intake is recorded for the given date");
        }
        diary.get(date).remove(quantity);
    }

    /**
     * Removes a portion of water for a given date.
     *
     * @param date the date for which water portion is being removed
     * @param portion the portion of water to be removed (represented by Portion)
     * @throws WaterNotLoggedException if no water intake is recorded for the given date
     */
    public void removeWater(LocalDate date, Portion portion) throws WaterNotLoggedException {
        if (!diary.containsKey(date)) {
            throw new WaterNotLoggedException("No water intake is recorded for the given date");
        }
        diary.get(date).remove(portion);
    }

    /**
     * Retrieves the total amount of water recorded for a specific date.
     *
     * @param date the date for which water intake is being queried
     * @return the total amount of water (in milliliters) recorded for the given date,
     *         or 0 if no water intake is recorded for that date
     */
    public int getDailyWater(LocalDate date) {
        if (!diary.containsKey(date)) {
            return 0;
        }
        return diary.get(date).getQuantity();
    }
}
