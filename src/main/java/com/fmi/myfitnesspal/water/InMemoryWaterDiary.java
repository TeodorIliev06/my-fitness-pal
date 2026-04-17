package com.fmi.myfitnesspal.water;

import com.fmi.myfitnesspal.exception.WaterNotLoggedException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class InMemoryWaterDiary implements WaterDiary {
    private final Map<LocalDate, Water> waterDiary;

    public InMemoryWaterDiary() {
        this(new HashMap<>());
    }

    public InMemoryWaterDiary(Map<LocalDate, Water> waterDiary) {
        this.waterDiary = Objects.requireNonNull(waterDiary);
    }

    @Override
    public void addWater(LocalDate date, Portion portion) {
        waterDiary.putIfAbsent(date, new Water());
        waterDiary.get(date).add(portion);
    }

    @Override
    public void addWater(LocalDate date, int quantity) {
        waterDiary.putIfAbsent(date, new Water());
        waterDiary.get(date).add(quantity);
    }

    @Override
    public void removeWater(LocalDate date, int quantity) throws WaterNotLoggedException {
        if (!waterDiary.containsKey(date)) {
            throw new WaterNotLoggedException("No water intake is recorded for the given date");
        }
        waterDiary.get(date).remove(quantity);
    }

    @Override
    public void removeWater(LocalDate date, Portion portion) throws WaterNotLoggedException {
        if (!waterDiary.containsKey(date)) {
            throw new WaterNotLoggedException("No water intake is recorded for the given date");
        }
        waterDiary.get(date).remove(portion);
    }

    @Override
    public int getDailyWater(LocalDate date) {
        if (!waterDiary.containsKey(date)) {
            return 0;
        }
        return waterDiary.get(date).getQuantity();
    }

    @Override
    public List<DailyWaterEntry> getAllDailyWaterEntries() {
        return waterDiary.entrySet().stream()
                .map(e -> new DailyWaterEntry(e.getKey(), e.getValue().getQuantity()))
                .toList();
    }
}
