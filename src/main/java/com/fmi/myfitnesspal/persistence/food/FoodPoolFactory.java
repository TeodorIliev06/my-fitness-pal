package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.food.InMemoryFoodPool;
import com.fmi.myfitnesspal.persistence.AbstractRepositoryFactory;
import com.fmi.myfitnesspal.persistence.PersistenceStoreFactory;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;

import java.nio.file.Path;

public final class FoodPoolFactory extends AbstractRepositoryFactory {

    private final FoodDtoMapper foodDtoMapper;
    private final Path foodPoolFilePath;

    public FoodPoolFactory(boolean persistToFile, PersistenceStoreFactory storeFactory,
                           FoodDtoMapper foodDtoMapper, Path foodPoolFilePath) {
        super(persistToFile, storeFactory);
        this.foodDtoMapper = foodDtoMapper;
        this.foodPoolFilePath = foodPoolFilePath;
    }

    public FoodPool create() {
        return resolveRepository(
                InMemoryFoodPool::new,
                this::buildPersistentFoodPool
        );
    }

    private FoodFileRepository buildPersistentFoodPool() {
        PersistenceStore<FoodDto> persistenceStore =
                storeFactory.createListStore(foodPoolFilePath, FoodDto.class);

        FoodFileRepository repository =
                new FoodFileRepository(new InMemoryFoodPool(), foodDtoMapper, persistenceStore);
        repository.loadInitialState();

        return repository;
    }
}
