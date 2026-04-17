package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.food.InMemoryFoodPool;
import com.fmi.myfitnesspal.persistence.file.JsonPersistence;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;
import org.external.json.JsonConverter;

import java.nio.file.Path;

public final class FoodPoolFactory {

    private final boolean persistToFile;
    private final JsonConverter jsonConverter;
    private final FoodDtoMapper foodDtoMapper;
    private final Path foodPoolFilePath;

    public FoodPoolFactory(boolean persistToFile, JsonConverter jsonConverter,
                           FoodDtoMapper foodDtoMapper, Path foodPoolFilePath) {
        this.persistToFile = persistToFile;
        this.jsonConverter = jsonConverter;
        this.foodDtoMapper = foodDtoMapper;
        this.foodPoolFilePath = foodPoolFilePath;
    }

    public FoodPool create() {
        InMemoryFoodPool inMemoryFoodPool = new InMemoryFoodPool();
        if (!persistToFile) {
            return inMemoryFoodPool;
        }

        PersistenceStore<FoodDto> persistenceStore =
                new JsonPersistence<>(jsonConverter, foodPoolFilePath, FoodDto.class);

        FoodFileRepository repository =
                new FoodFileRepository(inMemoryFoodPool, foodDtoMapper, persistenceStore);
        repository.loadInitialState();
        return repository;
    }
}
