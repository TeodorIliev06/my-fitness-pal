package com.fmi.myfitnesspal.persistence.water;

import com.fmi.myfitnesspal.persistence.file.PersistenceStore;
import com.fmi.myfitnesspal.water.InMemoryWaterDiary;
import com.fmi.myfitnesspal.water.WaterDiary;
import com.fmi.myfitnesspal.persistence.file.JsonPersistence;
import org.external.json.JsonConverter;

import java.nio.file.Path;

public final class WaterDiaryFactory {

    private static final String WATER_DIARY_FILE_NAME = "water_diary.json";

    private final boolean persistToFile;
    private final JsonConverter jsonConverter;
    private final DailyWaterEntryDtoMapper waterDtoMapper;

    public WaterDiaryFactory(boolean persistToFile, JsonConverter jsonConverter,
                             DailyWaterEntryDtoMapper waterDtoMapper) {
        this.persistToFile = persistToFile;
        this.jsonConverter = jsonConverter;
        this.waterDtoMapper = waterDtoMapper;
    }

    public WaterDiary createIn(Path userDataDirectory) {
        InMemoryWaterDiary inMemoryWaterDiary = new InMemoryWaterDiary();
        if (!persistToFile) {
            return inMemoryWaterDiary;
        }

        Path waterDiaryFilePath = userDataDirectory.resolve(WATER_DIARY_FILE_NAME);
        PersistenceStore<DailyWaterDto> persistenceStore =
                new JsonPersistence<>(jsonConverter, waterDiaryFilePath, DailyWaterDto.class);

        WaterFileRepository repository =
                new WaterFileRepository(inMemoryWaterDiary, waterDtoMapper, persistenceStore);
        repository.loadInitialState();
        return repository;
    }
}
