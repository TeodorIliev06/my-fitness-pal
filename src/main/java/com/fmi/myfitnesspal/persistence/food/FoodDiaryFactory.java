package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.InMemoryFoodDiary;
import com.fmi.myfitnesspal.persistence.AbstractRepositoryFactory;
import com.fmi.myfitnesspal.persistence.PersistenceStoreFactory;
import com.fmi.myfitnesspal.persistence.file.ObjectPersistenceStore;

import java.nio.file.Path;

public final class FoodDiaryFactory extends AbstractRepositoryFactory {

    private static final String FOOD_DIARY_FILE_NAME = "food_diary.json";

    private final FoodDiaryDtoMapper foodDiaryDtoMapper;

    public FoodDiaryFactory(boolean persistToFile, PersistenceStoreFactory storeFactory,
                            FoodDiaryDtoMapper foodDiaryDtoMapper) {
        super(persistToFile, storeFactory);
        this.foodDiaryDtoMapper = foodDiaryDtoMapper;
    }

    public FoodDiary createIn(Path userDataDirectory) {
        return resolveRepository(
                InMemoryFoodDiary::new,
                () -> buildPersistentFoodDiaryIn(userDataDirectory)
        );
    }

    private FoodDiaryFileRepository buildPersistentFoodDiaryIn(Path userDataDirectory) {
        Path foodDiaryFilePath = userDataDirectory.resolve(FOOD_DIARY_FILE_NAME);
        ObjectPersistenceStore<FoodDiaryDto> persistenceStore =
                storeFactory.createObjectStore(foodDiaryFilePath, FoodDiaryDto.class);

        FoodDiaryFileRepository repository =
                new FoodDiaryFileRepository(new InMemoryFoodDiary(), foodDiaryDtoMapper, persistenceStore);
        repository.loadInitialState();
        return repository;
    }
}
