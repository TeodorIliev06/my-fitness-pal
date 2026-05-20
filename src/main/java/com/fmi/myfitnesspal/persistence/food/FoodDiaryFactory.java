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

    public FoodDiary loadInMemoryFromFile(Path userDataDirectory) {
        Path diaryFilePath = userDataDirectory.resolve(FOOD_DIARY_FILE_NAME);

        ObjectPersistenceStore<FoodDiaryDto> store =
                storeFactory.createObjectStore(diaryFilePath, FoodDiaryDto.class);
        FoodDiary freshDiary = new InMemoryFoodDiary();
        store.load().ifPresent(dto -> populateDiaryFrom(freshDiary, dto));

        return freshDiary;
    }

    public void saveToFile(Path userDataDirectory, FoodDiary sourceDiary) {
        Path diaryFilePath = userDataDirectory.resolve(FOOD_DIARY_FILE_NAME);

        ObjectPersistenceStore<FoodDiaryDto> persistenceStore =
                storeFactory.createObjectStore(diaryFilePath, FoodDiaryDto.class);
        persistenceStore.save(foodDiaryDtoMapper.toDto(sourceDiary.getAllDailyFoodEntries()));
    }

    private void populateDiaryFrom(FoodDiary targetDiary, FoodDiaryDto dto) {
        dto.foods().stream()
                .map(foodDiaryDtoMapper::toDailyFoodEntry)
                .forEach(entry -> targetDiary.addFood(
                        entry.consumptionDate(), entry.eatingTime(), entry.food(), 1.0));
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
