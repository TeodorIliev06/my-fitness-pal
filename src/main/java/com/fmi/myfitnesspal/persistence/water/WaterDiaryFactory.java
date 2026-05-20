package com.fmi.myfitnesspal.persistence.water;

import com.fmi.myfitnesspal.persistence.AbstractRepositoryFactory;
import com.fmi.myfitnesspal.persistence.PersistenceStoreFactory;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;
import com.fmi.myfitnesspal.water.InMemoryWaterDiary;
import com.fmi.myfitnesspal.water.WaterDiary;

import java.nio.file.Path;

public final class WaterDiaryFactory extends AbstractRepositoryFactory {

    private static final String WATER_DIARY_FILE_NAME = "water_diary.json";

    private final DailyWaterEntryDtoMapper waterDtoMapper;

    public WaterDiaryFactory(boolean persistToFile, PersistenceStoreFactory storeFactory,
                             DailyWaterEntryDtoMapper waterDtoMapper) {
        super(persistToFile, storeFactory);
        this.waterDtoMapper = waterDtoMapper;
    }

    public WaterDiary createIn(Path userDataDirectory) {
        return resolveRepository(
                InMemoryWaterDiary::new,
                () -> buildPersistentWaterDiaryIn(userDataDirectory)
        );
    }

    public WaterDiary loadInMemoryFromFile(Path userDataDirectory) {
        Path diaryFilePath = userDataDirectory.resolve(WATER_DIARY_FILE_NAME);
        PersistenceStore<DailyWaterDto> store =
                storeFactory.createListStore(diaryFilePath, DailyWaterDto.class);
        WaterDiary freshDiary = new InMemoryWaterDiary();
        store.load().stream()
                .map(waterDtoMapper::toEntity)
                .forEach(entry -> freshDiary.addWater(entry.consumptionDate(), entry.millilitres()));
        return freshDiary;
    }

    public void saveToFile(Path userDataDirectory, WaterDiary sourceDiary) {
        Path diaryFilePath = userDataDirectory.resolve(WATER_DIARY_FILE_NAME);
        PersistenceStore<DailyWaterDto> store =
                storeFactory.createListStore(diaryFilePath, DailyWaterDto.class);
        store.save(waterDtoMapper.toDtos(sourceDiary.getAllDailyWaterEntries()));
    }

    private WaterFileRepository buildPersistentWaterDiaryIn(Path userDataDirectory) {
        Path waterDiaryFilePath = userDataDirectory.resolve(WATER_DIARY_FILE_NAME);
        PersistenceStore<DailyWaterDto> persistenceStore =
                storeFactory.createListStore(waterDiaryFilePath, DailyWaterDto.class);

        WaterFileRepository repository =
                new WaterFileRepository(new InMemoryWaterDiary(), waterDtoMapper, persistenceStore);
        repository.loadInitialState();

        return repository;
    }
}
