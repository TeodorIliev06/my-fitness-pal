package com.fmi.myfitnesspal.water;

import com.fmi.myfitnesspal.exception.WaterNotLoggedException;

import java.time.LocalDate;
import java.util.List;

public interface WaterDiary {
    void addWater(LocalDate date, Portion portion);

    void addWater(LocalDate date, int quantity);

    void removeWater(LocalDate date, int quantity) throws WaterNotLoggedException;

    void removeWater(LocalDate date, Portion portion) throws WaterNotLoggedException;

    int getDailyWater(LocalDate date);

    List<DailyWaterEntry> getAllDailyWaterEntries();
}
