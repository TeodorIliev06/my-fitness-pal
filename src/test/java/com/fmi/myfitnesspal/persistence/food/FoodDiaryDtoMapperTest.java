package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.DailyFoodEntry;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.Meal;
import com.fmi.myfitnesspal.food.DailyMealEntry;
import com.fmi.myfitnesspal.food.MealId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class FoodDiaryDtoMapperTest {

    private static final LocalDate CONSUMPTION_DATE = LocalDate.of(2026, 4, 1);
    private static final EatingTime EATING_TIME = EatingTime.LUNCH;
    private static final FoodId APPLE_ID = new FoodId("none", "apple");
    private static final MealId BREAKFAST_ID = new MealId("breakfast", "american");

    @Mock
    private FoodDtoMapper foodDtoMapper;

    @InjectMocks
    private FoodDiaryDtoMapper foodDiaryDtoMapper;

    @Test
    void testToDailyFoodEntryDtoMapsDateAndEatingTime() {
        Food apple = createFood();
        FoodDto appleDto = createFoodDto();
        DailyFoodEntry entry = new DailyFoodEntry(CONSUMPTION_DATE, EATING_TIME, apple);
        when(foodDtoMapper.toDto(apple)).thenReturn(appleDto);

        DailyFoodEntryDto dto = foodDiaryDtoMapper.toDailyFoodEntryDto(entry);

        assertEquals(CONSUMPTION_DATE, dto.date(),
                "toDailyFoodEntryDto should preserve the consumption date");
        assertEquals(EATING_TIME, dto.eatingTime(),
                "toDailyFoodEntryDto should preserve the eating time");
    }

    @Test
    void testToDailyFoodEntryDtoDelegatesToFoodDtoMapper() {
        Food apple = createFood();
        FoodDto appleDto = createFoodDto();
        DailyFoodEntry entry = new DailyFoodEntry(CONSUMPTION_DATE, EATING_TIME, apple);
        when(foodDtoMapper.toDto(apple)).thenReturn(appleDto);

        DailyFoodEntryDto dto = foodDiaryDtoMapper.toDailyFoodEntryDto(entry);

        assertEquals(appleDto, dto.food(),
                "toDailyFoodEntryDto should delegate food conversion to FoodDtoMapper");
    }

    @Test
    void testToDailyFoodEntryRestoresDateAndEatingTime() {
        Food apple = createFood();
        FoodDto appleDto = createFoodDto();
        DailyFoodEntryDto dto = new DailyFoodEntryDto(CONSUMPTION_DATE, EATING_TIME, appleDto);
        when(foodDtoMapper.toEntity(appleDto)).thenReturn(apple);

        DailyFoodEntry entry = foodDiaryDtoMapper.toDailyFoodEntry(dto);

        assertEquals(CONSUMPTION_DATE, entry.consumptionDate(),
                "toDailyFoodEntry should restore the consumption date from the DTO");
        assertEquals(EATING_TIME, entry.eatingTime(),
                "toDailyFoodEntry should restore the eating time from the DTO");
    }

    @Test
    void testToDailyFoodEntryDelegatesToFoodDtoMapper() {
        Food apple = createFood();
        FoodDto appleDto = createFoodDto();
        DailyFoodEntryDto dto = new DailyFoodEntryDto(CONSUMPTION_DATE, EATING_TIME, appleDto);
        when(foodDtoMapper.toEntity(appleDto)).thenReturn(apple);

        DailyFoodEntry entry = foodDiaryDtoMapper.toDailyFoodEntry(dto);

        assertEquals(apple, entry.food(),
                "toDailyFoodEntry should delegate food reconstruction to FoodDtoMapper");
    }

    @Test
    void testToDailyMealEntryDtoMapsDateEatingTimeAndMealIdentity() {
        Meal breakfast = createBreakfast();
        FoodDto appleDto = createFoodDto();
        DailyFoodEntry foodEntry = new DailyFoodEntry(CONSUMPTION_DATE, EATING_TIME, createFood());
        DailyMealEntry mealEntry = new DailyMealEntry(CONSUMPTION_DATE, EATING_TIME, breakfast);
        when(foodDtoMapper.toDto(breakfast.getFoods().get(0))).thenReturn(appleDto);

        DailyMealEntryDto dto = foodDiaryDtoMapper.toDailyMealEntryDto(mealEntry);

        assertEquals(CONSUMPTION_DATE, dto.date(),
                "toDailyMealEntryDto should preserve the consumption date");
        assertEquals(EATING_TIME, dto.eatingTime(),
                "toDailyMealEntryDto should preserve the eating time");
        assertEquals(BREAKFAST_ID.name(), dto.name(),
                "toDailyMealEntryDto should preserve the meal name from MealId");
        assertEquals(BREAKFAST_ID.description(), dto.description(),
                "toDailyMealEntryDto should preserve the meal description from MealId");
    }

    @Test
    void testToDailyMealEntryDtoMapsEachFoodInMeal() {
        Meal breakfast = createBreakfast();
        FoodDto appleDto = createFoodDto();
        DailyMealEntry mealEntry = new DailyMealEntry(CONSUMPTION_DATE, EATING_TIME, breakfast);
        when(foodDtoMapper.toDto(breakfast.getFoods().get(0))).thenReturn(appleDto);

        DailyMealEntryDto dto = foodDiaryDtoMapper.toDailyMealEntryDto(mealEntry);

        assertEquals(1, dto.foods().size(),
                "toDailyMealEntryDto should produce one food DTO per food in the meal");
        assertEquals(appleDto, dto.foods().get(0),
                "toDailyMealEntryDto should delegate each food conversion to FoodDtoMapper");
    }

    @Test
    void testToDailyMealEntryRestoresMealIdentityAndEatingTime() {
        FoodDto appleDto = createFoodDto();
        Food apple = createFood();
        DailyMealEntryDto dto = createDailyMealEntryDto(appleDto);
        when(foodDtoMapper.toEntity(appleDto)).thenReturn(apple);

        DailyMealEntry entry = foodDiaryDtoMapper.toDailyMealEntry(dto);

        assertEquals(CONSUMPTION_DATE, entry.consumptionDate(),
                "toDailyMealEntry should restore the consumption date");
        assertEquals(EATING_TIME, entry.eatingTime(),
                "toDailyMealEntry should restore the eating time");
        assertEquals(BREAKFAST_ID, entry.meal().getId(),
                "toDailyMealEntry should reconstruct the MealId from the DTO name and description");
    }

    @Test
    void testToDailyMealEntryDoesNotDoubleScaleFoods() {
        double storedCalories = 180.0;
        Food apple = Food.builder(APPLE_ID, 2.0, storedCalories).build();
        FoodDto appleDto = new FoodDto("none", "apple", 2.0, storedCalories, null, null, null);
        DailyMealEntryDto dto = createDailyMealEntryDto(appleDto);
        when(foodDtoMapper.toEntity(appleDto)).thenReturn(apple);

        DailyMealEntry entry = foodDiaryDtoMapper.toDailyMealEntry(dto);

        Food restoredFood = entry.meal().getFoods().get(0);
        assertEquals(storedCalories, restoredFood.getCalories(),
                "toDailyMealEntry must reconstruct meals with servings=1.0 to avoid double-scaling "
                        + "already-scaled calories stored in the file");
    }

    @Test
    void testToDtoProducesRootDtoWithCorrectFoodAndMealLists() {
        Food apple = createFood();
        Meal breakfast = createBreakfast();
        DailyFoodEntry foodEntry = new DailyFoodEntry(CONSUMPTION_DATE, EATING_TIME, apple);
        DailyMealEntry mealEntry = new DailyMealEntry(CONSUMPTION_DATE, EATING_TIME, breakfast);
        FoodDto appleDto = createFoodDto();
        when(foodDtoMapper.toDto(apple)).thenReturn(appleDto);
        when(foodDtoMapper.toDto(breakfast.getFoods().get(0))).thenReturn(appleDto);

        FoodDiaryDto diaryDto = foodDiaryDtoMapper.toDto(List.of(foodEntry), List.of(mealEntry));

        assertEquals(1, diaryDto.foods().size(),
                "toDto should produce one DailyFoodEntryDto per DailyFoodEntry");
        assertEquals(1, diaryDto.meals().size(),
                "toDto should produce one DailyMealEntryDto per DailyMealEntry");
    }

    @Test
    void testToDtoWithNoInputsProducesEmptyLists() {
        FoodDiaryDto diaryDto = foodDiaryDtoMapper.toDto(List.of(), List.of());

        assertTrue(diaryDto.foods().isEmpty(),
                "toDto with no food entries should produce an empty foods list");
        assertTrue(diaryDto.meals().isEmpty(),
                "toDto with no meal entries should produce an empty meals list");
    }

    private Food createFood() {
        return Food.builder(APPLE_ID, 2.0, 180.0).build();
    }

    private FoodDto createFoodDto() {
        return new FoodDto("none", "apple", 2.0, 180.0, null, null, null);
    }

    private DailyMealEntryDto createDailyMealEntryDto(FoodDto foodDto) {
        return new DailyMealEntryDto(
                CONSUMPTION_DATE,
                EATING_TIME,
                BREAKFAST_ID.name(), BREAKFAST_ID.description(),
                List.of(foodDto)
        );
    }

    private Meal createBreakfast() {
        Meal meal = new Meal(BREAKFAST_ID);
        meal.addFood(createFood(), 1.0);
        return meal;
    }
}
