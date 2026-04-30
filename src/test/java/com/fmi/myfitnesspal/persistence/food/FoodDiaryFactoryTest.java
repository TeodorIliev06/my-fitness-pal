package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.InMemoryFoodDiary;

import org.external.json.JsonConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class FoodDiaryFactoryTest {

    private static final LocalDate CONSUMPTION_DATE = LocalDate.of(2026, 4, 1);
    private static final EatingTime EATING_TIME = EatingTime.LUNCH;

    @TempDir
    Path tempDirectory;

    @Mock
    private JsonConverter jsonConverter;

    @Test
    void testCreateWithoutPersistenceReturnsInMemoryFoodDiary() {
        FoodDiaryFactory factory = buildFactory(false);

        FoodDiary createdDiary = factory.createIn(tempDirectory);

        assertInstanceOf(InMemoryFoodDiary.class, createdDiary,
                "create should return a plain InMemoryFoodDiary when file persistence is disabled");
    }

    @Test
    void testCreateWithoutPersistenceDoesNotCallJsonConverter() {
        FoodDiaryFactory factory = buildFactory(false);

        factory.createIn(tempDirectory);

        verifyNoInteractions(jsonConverter);
    }

    @Test
    void testCreateWithPersistenceReturnsFoodDiaryFileRepository() {
        FoodDiaryFactory factory = buildFactory(true);

        FoodDiary createdDiary = factory.createIn(tempDirectory);

        assertInstanceOf(FoodDiaryFileRepository.class, createdDiary,
                "create should return a FoodDiaryFileRepository when file persistence is enabled");
    }

    @Test
    void testCreateWithPersistenceAndNonExistingFileStartsWithEmptyFoodEntries() {
        FoodDiaryFactory factory = buildFactory(true);

        FoodDiary createdDiary = factory.createIn(tempDirectory);

        assertTrue(createdDiary.getAllDailyFoodEntries().isEmpty(),
                "getAllDailyFoodEntries should be empty when no persisted file exists yet");
    }

    @Test
    void testCreateWithPersistenceAndNonExistingFileStartsWithEmptyMealEntries() {
        FoodDiaryFactory factory = buildFactory(true);

        FoodDiary createdDiary = factory.createIn(tempDirectory);

        assertTrue(createdDiary.getAllDailyMealEntries().isEmpty(),
                "getAllDailyMealEntries should be empty when no persisted file exists yet");
    }

    @Test
    void testCreateWithPersistenceCorrectlyTracksAddedFood() {
        when(jsonConverter.serializeSingle(any())).thenReturn("{}");
        FoodDiaryFactory factory = buildFactory(true);
        FoodDiary createdDiary = factory.createIn(tempDirectory);
        Food apple = Food.builder(new FoodId("none", "apple"), 2.0, 180.0).build();

        createdDiary.addFood(CONSUMPTION_DATE, EATING_TIME, apple, 1.0);

        List<Food> storedFoods = createdDiary.getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EATING_TIME);
        assertEquals(1, storedFoods.size(),
                "The created diary should store and return the food that was added");
    }

    private FoodDiaryFactory buildFactory(boolean persistToFile) {
        return new FoodDiaryFactory(
                persistToFile,
                jsonConverter,
                new FoodDiaryDtoMapper(new FoodDtoMapper())
        );
    }
}
