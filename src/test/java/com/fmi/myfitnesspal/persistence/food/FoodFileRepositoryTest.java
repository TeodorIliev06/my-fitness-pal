package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class FoodFileRepositoryTest {

    private static final FoodId APPLE_ID = new FoodId("Golden", "Apple");

    @Mock
    private FoodPool foodPool;
    @Mock
    private FoodDtoMapper foodDtoMapper;
    @Mock
    private PersistenceStore<FoodDto> persistenceStore;

    @InjectMocks
    private FoodFileRepository foodFileRepository;

    @Test
    void testLoadInitialStateWithEmptyListAddsNothingToPool() {
        when(persistenceStore.load()).thenReturn(List.of());

        foodFileRepository.loadInitialState();

        verify(persistenceStore).load();
        verifyNoInteractions(foodDtoMapper);
        verifyNoInteractions(foodPool);
    }

    @Test
    void testLoadInitialStateWithSingleDtoMapsAndAddsToPool() {
        FoodDto dto = createAppleDto();
        Food food = createApple();

        when(persistenceStore.load()).thenReturn(List.of(dto));
        when(foodDtoMapper.toEntity(dto)).thenReturn(food);

        foodFileRepository.loadInitialState();

        verify(foodDtoMapper).toEntity(dto);
        verify(foodPool).addFood(food);
    }

    @Test
    void testLoadInitialStateWithMultipleDtosMapsAndAddsEachToPool() {
        FoodId bananaId = new FoodId("Yellow", "Banana");
        FoodDto appleDto = createAppleDto();
        FoodDto bananaDto = new FoodDto("Yellow", "Banana", 118.0, 89.0, 0.3, 1.1, 23.0);
        Food appleFood = createApple();
        Food bananaFood = Food.builder(bananaId, 118.0, 89.0).build();

        when(persistenceStore.load()).thenReturn(List.of(appleDto, bananaDto));
        when(foodDtoMapper.toEntity(appleDto)).thenReturn(appleFood);
        when(foodDtoMapper.toEntity(bananaDto)).thenReturn(bananaFood);

        foodFileRepository.loadInitialState();

        verify(foodPool).addFood(appleFood);
        verify(foodPool).addFood(bananaFood);
    }

    @Test
    void testAddFoodDelegatesToInnerPoolAndPersistsCurrentState() {
        Food food = createApple();
        List<Food> allFoodsAfterAdd = List.of(food);
        List<FoodDto> allDtosAfterAdd = List.of(createAppleDto());

        when(foodPool.getAllFoods()).thenReturn(allFoodsAfterAdd);
        when(foodDtoMapper.toDtos(allFoodsAfterAdd)).thenReturn(allDtosAfterAdd);

        foodFileRepository.addFood(food);

        verify(foodPool).addFood(food);
        verify(persistenceStore).save(allDtosAfterAdd);
    }

    @Test
    void testAddFoodSavesAllCurrentFoodsAfterAddedOne() {
        Food existingFood = createApple();
        FoodId newFoodId = new FoodId("Yellow", "Banana");
        Food newFood = Food.builder(newFoodId, 118.0, 89.0).build();

        List<Food> allFoodsAfterAdd = List.of(existingFood, newFood);
        List<FoodDto> expectedDtos = List.of(createAppleDto(),
                new FoodDto("Yellow", "Banana", 118.0, 89.0, null, null, null));

        when(foodPool.getAllFoods()).thenReturn(allFoodsAfterAdd);
        when(foodDtoMapper.toDtos(allFoodsAfterAdd)).thenReturn(expectedDtos);

        foodFileRepository.addFood(newFood);

        verify(persistenceStore).save(expectedDtos);
    }

    @Test
    void testGetFoodDelegatesToInnerPoolAndReturnsResult() {
        Food expectedFood = createApple();
        when(foodPool.getFood(APPLE_ID)).thenReturn(expectedFood);

        Food actualFood = foodFileRepository.getFood(APPLE_ID);

        assertEquals(expectedFood, actualFood,
                "getFood should return the result delegated from the inner FoodPool");
        verify(foodPool).getFood(APPLE_ID);
    }

    @Test
    void testGetAllFoodsDelegatesToInnerPoolAndReturnsAllResults() {
        List<Food> expectedFoods = List.of(createApple());
        when(foodPool.getAllFoods()).thenReturn(expectedFoods);

        List<Food> actualFoods = foodFileRepository.getAllFoods();

        assertEquals(expectedFoods, actualFoods,
                "getAllFoods should return the list delegated from the inner FoodPool");
        verify(foodPool).getAllFoods();
    }

    private static FoodDto createAppleDto() {
        return new FoodDto("Golden", "Apple", 182.0, 95.0, 0.3, 0.5, 25.0);
    }

    private static Food createApple() {
        return Food.builder(APPLE_ID, 182.0, 95.0)
                .setFats(Optional.of(0.3))
                .setProtein(Optional.of(0.5))
                .setCarbs(Optional.of(25.0))
                .build();
    }
}
