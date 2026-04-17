package com.fmi.myfitnesspal.persistence.water;

import com.fmi.myfitnesspal.exception.WaterNotLoggedException;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;
import com.fmi.myfitnesspal.water.DailyWaterEntry;
import com.fmi.myfitnesspal.water.Portion;
import com.fmi.myfitnesspal.water.WaterDiary;

import java.time.LocalDate;
import java.util.List;

public final class WaterFileRepository implements WaterDiary {

    private final WaterDiary waterDiary;
    private final DailyWaterEntryDtoMapper waterDtoMapper;
    private final PersistenceStore<DailyWaterDto> persistenceStore;

    public WaterFileRepository(WaterDiary waterDiary, DailyWaterEntryDtoMapper waterDtoMapper,
                               PersistenceStore<DailyWaterDto> persistenceStore) {
        this.waterDiary = waterDiary;
        this.waterDtoMapper = waterDtoMapper;
        this.persistenceStore = persistenceStore;
    }

    @Override
    public void addWater(LocalDate consumptionDate, Portion portion) {
        waterDiary.addWater(consumptionDate, portion);
        saveCurrentState();
    }

    @Override
    public void addWater(LocalDate consumptionDate, int millilitres) {
        waterDiary.addWater(consumptionDate, millilitres);
        saveCurrentState();
    }

    @Override
    public void removeWater(LocalDate consumptionDate, int millilitres) throws WaterNotLoggedException {
        waterDiary.removeWater(consumptionDate, millilitres);
        saveCurrentState();
    }

    @Override
    public void removeWater(LocalDate consumptionDate, Portion portion) throws WaterNotLoggedException {
        waterDiary.removeWater(consumptionDate, portion);
        saveCurrentState();
    }

    @Override
    public int getDailyWater(LocalDate consumptionDate) {
        return waterDiary.getDailyWater(consumptionDate);
    }

    @Override
    public List<DailyWaterEntry> getAllDailyWaterEntries() {
        return waterDiary.getAllDailyWaterEntries();
    }

    void loadInitialState() {
        persistenceStore.load()
                .stream()
                .map(waterDtoMapper::toEntity)
                .forEach(e -> waterDiary.addWater(e.consumptionDate(), e.millilitres()));
    }

    private void saveCurrentState() {
        persistenceStore.save(waterDtoMapper.toDtos(waterDiary.getAllDailyWaterEntries()));
    }
}
