package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.DailyMealCaloriesSummary;
import com.fmi.myfitnesspal.food.DailyNutritionSummary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.DailyFoodEntry;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.Meal;
import com.fmi.myfitnesspal.food.DailyMealEntry;
import com.fmi.myfitnesspal.food.MealId;
import com.fmi.myfitnesspal.food.WeeklyNutritionSummary;
import com.fmi.myfitnesspal.persistence.file.ObjectPersistenceStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class FoodDiaryFileRepositoryTest {

    private static final LocalDate CONSUMPTION_DATE = LocalDate.of(2026, 4, 1);
    private static final EatingTime EATING_TIME = EatingTime.LUNCH;
    private static final FoodId APPLE_ID = new FoodId("none", "apple");
    private static final MealId BREAKFAST_ID = new MealId("breakfast", "american");
    private static final double SINGLE_SERVING = 1.0;

    private static final int WEEKLY_SUMMARY_WEEK = 15;
    private static final int WEEKLY_SUMMARY_YEAR = 2025;

    @Mock
    private FoodDiary foodDiary;
    @Mock
    private FoodDiaryDtoMapper foodDiaryDtoMapper;
    @Mock
    private ObjectPersistenceStore<FoodDiaryDto> persistenceStore;

    @InjectMocks
    private FoodDiaryFileRepository foodDiaryFileRepository;

    @Test
    void testLoadInitialStateWithNoFileAddsNothingToDiary() {
        when(persistenceStore.load()).thenReturn(Optional.empty());

        foodDiaryFileRepository.loadInitialState();

        verify(persistenceStore).load();
        verifyNoInteractions(foodDiaryDtoMapper);
        verifyNoInteractions(foodDiary);
    }

    @Test
    void testLoadInitialStateWithFoodEntryTurnsFoodIntoInnerDiary() {
        Food apple = createApple();
        DailyFoodEntry foodEntry = new DailyFoodEntry(CONSUMPTION_DATE, EATING_TIME, apple);
        DailyFoodEntryDto foodDto = buildDailyFoodEntryDto();
        FoodDiaryDto diaryDto = new FoodDiaryDto(List.of(foodDto), List.of());

        when(persistenceStore.load()).thenReturn(Optional.of(diaryDto));
        when(foodDiaryDtoMapper.toDailyFoodEntry(foodDto)).thenReturn(foodEntry);

        foodDiaryFileRepository.loadInitialState();

        verify(foodDiary).addFood(CONSUMPTION_DATE, EATING_TIME, apple, SINGLE_SERVING);
    }

    @Test
    void testLoadInitialStateWithMealEntryTurnsMealIntoInnerDiary() {
        Meal breakfast = new Meal(BREAKFAST_ID);
        DailyMealEntry mealEntry = new DailyMealEntry(CONSUMPTION_DATE, EATING_TIME, breakfast);
        DailyMealEntryDto mealDto = buildDailyMealEntryDto();
        FoodDiaryDto diaryDto = new FoodDiaryDto(List.of(), List.of(mealDto));

        when(persistenceStore.load()).thenReturn(Optional.of(diaryDto));
        when(foodDiaryDtoMapper.toDailyMealEntry(mealDto)).thenReturn(mealEntry);

        foodDiaryFileRepository.loadInitialState();

        verify(foodDiary).addMeal(CONSUMPTION_DATE, EATING_TIME, breakfast);
    }

    @Test
    void testAddFoodDelegatesToInnerDiaryAndPersistsCurrentState() {
        Food apple = createApple();
        List<DailyFoodEntry> foodEntries = List.of(new DailyFoodEntry(CONSUMPTION_DATE, EATING_TIME, apple));
        List<DailyMealEntry> mealEntries = List.of();
        FoodDiaryDto expectedDto = new FoodDiaryDto(List.of(), List.of());

        when(foodDiary.getAllDailyFoodEntries()).thenReturn(foodEntries);
        when(foodDiary.getAllDailyMealEntries()).thenReturn(mealEntries);
        when(foodDiaryDtoMapper.toDto(foodEntries, mealEntries)).thenReturn(expectedDto);

        foodDiaryFileRepository.addFood(CONSUMPTION_DATE, EATING_TIME, apple, SINGLE_SERVING);

        verify(foodDiary).addFood(CONSUMPTION_DATE, EATING_TIME, apple, SINGLE_SERVING);
        verify(persistenceStore).save(expectedDto);
    }

    @Test
    void testAddMealDelegatesToInnerDiaryAndPersistsCurrentState() {
        Meal breakfast = new Meal(BREAKFAST_ID);
        List<DailyFoodEntry> foodEntries = List.of();
        List<DailyMealEntry> mealEntries = List.of(new DailyMealEntry(CONSUMPTION_DATE, EATING_TIME, breakfast));
        FoodDiaryDto expectedDto = new FoodDiaryDto(List.of(), List.of());

        when(foodDiary.getAllDailyFoodEntries()).thenReturn(foodEntries);
        when(foodDiary.getAllDailyMealEntries()).thenReturn(mealEntries);
        when(foodDiaryDtoMapper.toDto(foodEntries, mealEntries)).thenReturn(expectedDto);

        foodDiaryFileRepository.addMeal(CONSUMPTION_DATE, EATING_TIME, breakfast);

        verify(foodDiary).addMeal(CONSUMPTION_DATE, EATING_TIME, breakfast);
        verify(persistenceStore).save(expectedDto);
    }

    @Test
    void testRemoveFoodDelegatesToInnerDiaryAndPersistsCurrentState() {
        List<DailyFoodEntry> foodEntries = List.of();
        List<DailyMealEntry> mealEntries = List.of();
        FoodDiaryDto expectedDto = new FoodDiaryDto(List.of(), List.of());

        when(foodDiary.getAllDailyFoodEntries()).thenReturn(foodEntries);
        when(foodDiary.getAllDailyMealEntries()).thenReturn(mealEntries);
        when(foodDiaryDtoMapper.toDto(foodEntries, mealEntries)).thenReturn(expectedDto);

        foodDiaryFileRepository.removeFood(CONSUMPTION_DATE, EATING_TIME, APPLE_ID);

        verify(foodDiary).removeFood(CONSUMPTION_DATE, EATING_TIME, APPLE_ID);
        verify(persistenceStore).save(expectedDto);
    }

    @Test
    void testRemoveMealDelegatesToInnerDiaryAndPersistsCurrentState() {
        List<DailyFoodEntry> foodEntries = List.of();
        List<DailyMealEntry> mealEntries = List.of();
        FoodDiaryDto expectedDto = new FoodDiaryDto(List.of(), List.of());

        when(foodDiary.getAllDailyFoodEntries()).thenReturn(foodEntries);
        when(foodDiary.getAllDailyMealEntries()).thenReturn(mealEntries);
        when(foodDiaryDtoMapper.toDto(foodEntries, mealEntries)).thenReturn(expectedDto);

        foodDiaryFileRepository.removeMeal(CONSUMPTION_DATE, EATING_TIME, BREAKFAST_ID);

        verify(foodDiary).removeMeal(CONSUMPTION_DATE, EATING_TIME, BREAKFAST_ID);
        verify(persistenceStore).save(expectedDto);
    }

    @Test
    void testGetFoodsByDateAndEatingTimeDelegatesToInnerDiaryAndReturnsResult() {
        Food apple = createApple();
        when(foodDiary.getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EATING_TIME))
                .thenReturn(List.of(apple));

        List<Food> result = foodDiaryFileRepository.getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EATING_TIME);

        assertEquals(List.of(apple), result,
                "getFoodsByDateAndEatingTime should return the list delegated from the inner FoodDiary");
        verify(foodDiary).getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EATING_TIME);
        verifyNoInteractions(persistenceStore);
    }

    @Test
    void testGetMealsByDateAndEatingTimeDelegatesToInnerDiaryAndReturnsResult() {
        Meal breakfast = new Meal(BREAKFAST_ID);
        when(foodDiary.getMealsByDateAndEatingTime(CONSUMPTION_DATE, EATING_TIME))
                .thenReturn(List.of(breakfast));

        List<Meal> result = foodDiaryFileRepository.getMealsByDateAndEatingTime(CONSUMPTION_DATE, EATING_TIME);

        assertEquals(List.of(breakfast), result,
                "getMealsByDateAndEatingTime should return the list delegated from the inner FoodDiary");
        verify(foodDiary).getMealsByDateAndEatingTime(CONSUMPTION_DATE, EATING_TIME);
        verifyNoInteractions(persistenceStore);
    }

    @Test
    void testGetDailyNutritionSummaryDelegatesToInnerDiaryAndReturnsResult() {
        DailyNutritionSummary expectedSummary = new DailyNutritionSummary(
                CONSUMPTION_DATE, 300.0, Optional.empty(), Optional.empty(), Optional.empty());
        when(foodDiary.getDailyNutritionSummary(CONSUMPTION_DATE)).thenReturn(expectedSummary);

        DailyNutritionSummary result = foodDiaryFileRepository.getDailyNutritionSummary(CONSUMPTION_DATE);

        assertEquals(expectedSummary, result,
                "getDailyNutritionSummary should return the result delegated from the inner FoodDiary");
        verifyNoInteractions(persistenceStore);
    }

    @Test
    void testGetWeeklyNutritionSummaryByWeekNumberDelegatesToInnerDiaryAndReturnsResult() {
        WeeklyNutritionSummary expectedSummary = new WeeklyNutritionSummary(
                WEEKLY_SUMMARY_WEEK, WEEKLY_SUMMARY_YEAR,
                2100.0, Optional.empty(), Optional.empty(), Optional.empty());
        when(foodDiary.getWeeklyNutritionSummary(WEEKLY_SUMMARY_WEEK, WEEKLY_SUMMARY_YEAR))
                .thenReturn(expectedSummary);

        WeeklyNutritionSummary result =
                foodDiaryFileRepository.getWeeklyNutritionSummary(WEEKLY_SUMMARY_WEEK, WEEKLY_SUMMARY_YEAR);

        assertEquals(expectedSummary, result,
                "getWeeklyNutritionSummary(int, int) should return the result delegated from the inner FoodDiary");
        verifyNoInteractions(persistenceStore);
    }

    @Test
    void testGetDailyMealCaloriesSummaryDelegatesToInnerDiaryAndReturnsResult() {
        DailyMealCaloriesSummary expectedSummary =
                new DailyMealCaloriesSummary(CONSUMPTION_DATE, new EnumMap<>(EatingTime.class));
        when(foodDiary.getDailyMealCaloriesSummary(CONSUMPTION_DATE)).thenReturn(expectedSummary);

        DailyMealCaloriesSummary result = foodDiaryFileRepository.getDailyMealCaloriesSummary(CONSUMPTION_DATE);

        assertEquals(expectedSummary, result,
                "getDailyMealCaloriesSummary should return the result delegated from the inner FoodDiary");
        verifyNoInteractions(persistenceStore);
    }

    private Food createApple() {
        return Food.builder(APPLE_ID, 2.0, 180.0).build();
    }

    private DailyFoodEntryDto buildDailyFoodEntryDto() {
        return new DailyFoodEntryDto(CONSUMPTION_DATE, EATING_TIME, null);
    }

    private DailyMealEntryDto buildDailyMealEntryDto() {
        return new DailyMealEntryDto(CONSUMPTION_DATE, EATING_TIME,
                BREAKFAST_ID.name(), BREAKFAST_ID.description(), List.of());
    }
}
