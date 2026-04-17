package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.food.InMemoryFoodPool;
import org.external.json.JsonConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public final class FoodPoolFactoryTest {

    @TempDir
    Path tempDirectory;

    @Mock
    private JsonConverter jsonConverter;

    @Test
    void testCreateWithoutPersistenceReturnsInMemoryFoodPool() {
        FoodPoolFactory factory = buildFactory(false);

        FoodPool createdPool = factory.create();

        assertInstanceOf(InMemoryFoodPool.class, createdPool,
                "create should return a plain InMemoryFoodPool when file persistence is disabled");
    }

    @Test
    void testCreateWithoutPersistenceDoesNotCallJsonConverter() {
        FoodPoolFactory factory = buildFactory(false);

        factory.create();

        verifyNoInteractions(jsonConverter);
    }

    @Test
    void testCreateWithPersistenceReturnsFoodFileRepository() {
        FoodPoolFactory factory = buildFactory(true);

        FoodPool createdPool = factory.create();

        assertInstanceOf(FoodFileRepository.class, createdPool,
                "create should return a FoodFileRepository when file persistence is enabled");
    }

    @Test
    void testCreateWithPersistenceAndNonExistingFileReturnsEmptyPool() {
        FoodPoolFactory factory = buildFactory(true);

        FoodPool createdPool = factory.create();

        assertTrue(createdPool.getAllFoods().isEmpty(),
                "The pool should be empty when no persisted file exists yet");
    }

    @Test
    void testCreateWithPersistenceReturnsCorrectPool() {
        when(jsonConverter.serialize(any())).thenReturn("[]");
        FoodPoolFactory factory = new FoodPoolFactory(
                true, jsonConverter, new FoodDtoMapper(), getFoodFilePath()
        );
        FoodPool createdPool = factory.create();
        Food food = createApple();

        verifyNoInteractions(jsonConverter);
        createdPool.addFood(food);

        Food retrieved = createdPool.getFood(new FoodId("Golden", "Apple"));
        assertEquals(food, retrieved,
                "The FoodFileRepository returned by the factory should delegate getFood to its inner pool");
    }

    private FoodPoolFactory buildFactory(boolean persistToFile) {
        return new FoodPoolFactory(
                persistToFile,
                jsonConverter,
                new FoodDtoMapper(),
                getFoodFilePath()
        );
    }

    private Path getFoodFilePath() {
        return tempDirectory.resolve("foods.json");
    }

    private static Food createApple() {
        return Food.builder(new FoodId("Golden", "Apple"), 182.0, 95.0)
                .setFats(Optional.of(0.3))
                .setProtein(Optional.of(0.5))
                .setCarbs(Optional.of(25.0))
                .build();
    }
}
