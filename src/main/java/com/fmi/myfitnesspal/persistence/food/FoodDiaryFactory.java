package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.InMemoryFoodDiary;
import com.fmi.myfitnesspal.persistence.file.JsonObjectPersistence;
import com.fmi.myfitnesspal.persistence.file.ObjectPersistenceStore;
import org.external.json.JsonConverter;

import java.nio.file.Path;

public final class FoodDiaryFactory {

    private final boolean persistToFile;
    private final JsonConverter jsonConverter;
    private final FoodDiaryDtoMapper foodDiaryDtoMapper;
    private final Path foodDiaryFilePath;

    public FoodDiaryFactory(boolean persistToFile, JsonConverter jsonConverter,
                            FoodDiaryDtoMapper foodDiaryDtoMapper, Path foodDiaryFilePath) {
        this.persistToFile = persistToFile;
        this.jsonConverter = jsonConverter;
        this.foodDiaryDtoMapper = foodDiaryDtoMapper;
        this.foodDiaryFilePath = foodDiaryFilePath;
    }

    public FoodDiary create() {
        InMemoryFoodDiary inMemoryFoodDiary = new InMemoryFoodDiary();
        if (!persistToFile) {
            return inMemoryFoodDiary;
        }

        ObjectPersistenceStore<FoodDiaryDto> persistenceStore =
                new JsonObjectPersistence<>(jsonConverter, foodDiaryFilePath, FoodDiaryDto.class);

        FoodDiaryFileRepository repository =
                new FoodDiaryFileRepository(inMemoryFoodDiary, foodDiaryDtoMapper, persistenceStore);
        repository.loadInitialState();
        return repository;
    }
}
