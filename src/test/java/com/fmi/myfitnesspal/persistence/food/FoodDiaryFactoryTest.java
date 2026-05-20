package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.InMemoryFoodDiary;

import com.fmi.myfitnesspal.persistence.PersistenceStoreFactory;
import com.fmi.myfitnesspal.persistence.file.ObjectPersistenceStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class FoodDiaryFactoryTest {

    private static final LocalDate CONSUMPTION_DATE = LocalDate.of(2026, 4, 1);
    private static final EatingTime EATING_TIME = EatingTime.LUNCH;

    @TempDir
    Path tempDirectory;

    @Mock
    private PersistenceStoreFactory storeFactoryMock;
    @Mock
    private ObjectPersistenceStore<FoodDiaryDto> diaryStoreMock;

    @Test
    void testCreateWithoutPersistenceReturnsInMemoryFoodDiary() {
        FoodDiaryFactory factory = buildFactory(false);

        FoodDiary createdDiary = factory.createIn(tempDirectory);

        assertInstanceOf(InMemoryFoodDiary.class, createdDiary,
                "create should return a plain InMemoryFoodDiary when file persistence is disabled");
    }

    @Test
    void testCreateWithoutPersistenceDoesNotCallPersistenceStoreFactory() {
        FoodDiaryFactory factory = buildFactory(false);

        factory.createIn(tempDirectory);

        verifyNoInteractions(storeFactoryMock);
    }

    @Test
    void testCreateWithPersistenceReturnsFoodDiaryFileRepository() {
        when(storeFactoryMock.createObjectStore(any(), eq(FoodDiaryDto.class))).thenReturn(diaryStoreMock);
        when(diaryStoreMock.load()).thenReturn(Optional.empty());

        FoodDiaryFactory factory = buildFactory(true);

        FoodDiary createdDiary = factory.createIn(tempDirectory);

        assertInstanceOf(FoodDiaryFileRepository.class, createdDiary,
                "create should return a FoodDiaryFileRepository when file persistence is enabled");
    }

    @Test
    void testCreateWithPersistenceAndNonExistingFileStartsWithEmptyFoodEntries() {
        when(storeFactoryMock.createObjectStore(any(), eq(FoodDiaryDto.class))).thenReturn(diaryStoreMock);
        when(diaryStoreMock.load()).thenReturn(Optional.empty());

        FoodDiaryFactory factory = buildFactory(true);

        FoodDiary createdDiary = factory.createIn(tempDirectory);

        assertTrue(createdDiary.getAllDailyFoodEntries().isEmpty(),
                "getAllDailyFoodEntries should be empty when no persisted file exists yet");
    }

    @Test
    void testCreateWithPersistenceCorrectlyTracksAddedFood() {
        when(storeFactoryMock.createObjectStore(any(), eq(FoodDiaryDto.class))).thenReturn(diaryStoreMock);
        when(diaryStoreMock.load()).thenReturn(Optional.empty());

        FoodDiaryFactory factory = buildFactory(true);
        FoodDiary createdDiary = factory.createIn(tempDirectory);
        Food apple = Food.builder(new FoodId("none", "apple"), 2.0, 180.0).build();

        createdDiary.addFood(CONSUMPTION_DATE, EATING_TIME, apple, 1.0);

        List<Food> storedFoods = createdDiary.getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EATING_TIME);
        assertEquals(1, storedFoods.size(),
                "The created diary should store and return the food that was added");
    }

    @Test
    void testLoadInMemoryFromFileReturnsEmptyDiaryWhenFileDoesNotExist() {
        when(storeFactoryMock.createObjectStore(any(), eq(FoodDiaryDto.class))).thenReturn(diaryStoreMock);
        when(diaryStoreMock.load()).thenReturn(Optional.empty());

        FoodDiaryFactory factory = buildFactory(false);
        FoodDiary loadedDiary = factory.loadInMemoryFromFile(tempDirectory);

        assertTrue(loadedDiary.getAllDailyFoodEntries().isEmpty(),
                "loadInMemoryFromFile must return an empty diary when no file exists for the user");
    }

    @Test
    void testSaveToFileInvokesStoreWithMappedEntries() {
        when(storeFactoryMock.createObjectStore(any(), eq(FoodDiaryDto.class))).thenReturn(diaryStoreMock);

        FoodDiary sourceDiary = new InMemoryFoodDiary();
        FoodDiaryFactory factory = buildFactory(false);
        factory.saveToFile(tempDirectory, sourceDiary);

        verify(diaryStoreMock).save(any(FoodDiaryDto.class));
    }

    private FoodDiaryFactory buildFactory(boolean persistToFile) {
        return new FoodDiaryFactory(
                persistToFile,
                storeFactoryMock,
                new FoodDiaryDtoMapper(new FoodDtoMapper())
        );
    }
}
