package com.fmi.myfitnesspal.persistence.water;

import com.fmi.myfitnesspal.persistence.file.PersistenceStore;
import com.fmi.myfitnesspal.water.InMemoryWaterDiary;
import com.fmi.myfitnesspal.water.WaterDiary;
import com.fmi.myfitnesspal.persistence.file.JsonPersistence;
import org.external.json.JsonConverter;

import java.nio.file.Path;

public final class WaterDiaryFactory {

    private final boolean persistToFile;
    private final JsonConverter jsonConverter;
    private final DailyWaterEntryDtoMapper waterDtoMapper;
    private final Path waterDiaryFilePath;

    public WaterDiaryFactory(boolean persistToFile, JsonConverter jsonConverter,
                             DailyWaterEntryDtoMapper waterDtoMapper, Path waterDiaryFilePath) {
        this.persistToFile = persistToFile;
        this.jsonConverter = jsonConverter;
        this.waterDtoMapper = waterDtoMapper;
        this.waterDiaryFilePath = waterDiaryFilePath;
    }

    public WaterDiary create() {
        InMemoryWaterDiary inMemoryWaterDiary = new InMemoryWaterDiary();
        if (!persistToFile) {
            return inMemoryWaterDiary;
        }

        PersistenceStore<DailyWaterDto> persistenceStore =
                new JsonPersistence<>(jsonConverter, waterDiaryFilePath, DailyWaterDto.class);

        WaterFileRepository repository =
                new WaterFileRepository(inMemoryWaterDiary, waterDtoMapper, persistenceStore);
        repository.loadInitialState();
        return repository;
    }
}
