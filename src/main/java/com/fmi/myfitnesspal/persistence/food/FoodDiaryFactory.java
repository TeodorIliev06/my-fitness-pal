package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.InMemoryFoodDiary;
import com.fmi.myfitnesspal.persistence.file.JsonObjectPersistence;
import com.fmi.myfitnesspal.persistence.file.ObjectPersistenceStore;
import org.external.json.JsonConverter;

import java.nio.file.Path;

public final class FoodDiaryFactory {

    private static final String FOOD_DIARY_FILE_NAME = "food_diary.json";

    private final boolean persistToFile;
    private final JsonConverter jsonConverter;
    private final FoodDiaryDtoMapper foodDiaryDtoMapper;

    public FoodDiaryFactory(boolean persistToFile, JsonConverter jsonConverter,
                            FoodDiaryDtoMapper foodDiaryDtoMapper) {
        this.persistToFile = persistToFile;
        this.jsonConverter = jsonConverter;
        this.foodDiaryDtoMapper = foodDiaryDtoMapper;
    }

    public FoodDiary createIn(Path userDataDirectory) {
        InMemoryFoodDiary inMemoryFoodDiary = new InMemoryFoodDiary();
        if (!persistToFile) {
            return inMemoryFoodDiary;
        }

        Path foodDiaryFilePath = userDataDirectory.resolve(FOOD_DIARY_FILE_NAME);
        ObjectPersistenceStore<FoodDiaryDto> persistenceStore =
                new JsonObjectPersistence<>(jsonConverter, foodDiaryFilePath, FoodDiaryDto.class);

        FoodDiaryFileRepository repository =
                new FoodDiaryFileRepository(inMemoryFoodDiary, foodDiaryDtoMapper, persistenceStore);
        repository.loadInitialState();
        return repository;
    }
}
