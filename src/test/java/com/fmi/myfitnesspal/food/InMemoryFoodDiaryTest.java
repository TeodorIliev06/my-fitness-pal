package com.fmi.myfitnesspal.food;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.fmi.myfitnesspal.utility.DateHelper.getYearFrom;
import static com.fmi.myfitnesspal.utility.DateHelper.getWeekNumberFrom;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class InMemoryFoodDiaryTest {

    private static final LocalDate CONSUMPTION_DATE = LocalDate.of(2025, 5, 26);
    private static final LocalDate ANOTHER_DATE_IN_SAME_WEEK = LocalDate.of(2025, 5, 28);
    private static final LocalDate DATE_IN_DIFFERENT_WEEK = LocalDate.of(2025, 6, 2);
    private static final LocalDate SUNDAY_OF_CONSUMPTION_WEEK = LocalDate.of(2025, 6, 1);
    private static final LocalDate DATE_IN_SAME_WEEK_DIFFERENT_YEAR = LocalDate.of(2024, 5, 27);

    private static final int CONSUMPTION_WEEK = getWeekNumberFrom(CONSUMPTION_DATE);
    private static final int CONSUMPTION_YEAR = getYearFrom(CONSUMPTION_DATE);
    private static final int DAYS_IN_WEEK = 7;

    private static final Food APPLE =
            Food.builder(new FoodId("Apple", "green"), 50, 10).build();
    private static final Food BANANA =
            Food.builder(new FoodId("Banana", "yellow"), 100, 90).build();
    private static final Food CHICKEN_BREAST = Food.builder(
                    new FoodId("Chicken", "grilled breast"), 100, 165)
            .setProtein(Optional.of(31.0))
            .setFats(Optional.of(3.6))
            .setCarbs(Optional.of(0.0))
            .build();
    private static final Food WHITE_RICE = Food.builder(
                    new FoodId("Rice", "white cooked"), 200, 260)
            .setProtein(Optional.of(5.4))
            .setFats(Optional.of(0.6))
            .setCarbs(Optional.of(57.0))
            .build();

    private InMemoryFoodDiary foodDiary;

    @BeforeEach
    public void setUp() {
        foodDiary = new InMemoryFoodDiary();
    }

    @Test
    public void testAddFood() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);

        List<Food> breakfastFoods = foodDiary.getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.BREAKFAST);

        assertEquals(1, breakfastFoods.size(),
                "Exactly one food item must be logged at breakfast after adding food");
        assertEquals(APPLE.getId(), breakfastFoods.get(0).getId(),
                "The stored food must be the food that was added");
    }

    @Test
    public void testAddFoodRecalculatesNutritionByServingsCount() {
        int servingsCount = 2;
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, servingsCount);

        Food storedFood = foodDiary.getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.BREAKFAST).get(0);

        assertEquals(APPLE.getCalories() * servingsCount, storedFood.getCalories(),
                "Calories must be scaled by the serving count");
        assertEquals(APPLE.getServingSize() * servingsCount, storedFood.getServingSize(),
                "Serving size must be scaled by the serving count");
    }

    @Test
    public void testAddFoodCreatesNewDailyEntryWhenNoExistingDiary() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);

        assertFalse(foodDiary.getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.BREAKFAST).isEmpty(),
                "A new daily entry must be created and must contain the added food");
    }

    @Test
    public void testRemoveFood() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.DINNER, APPLE, 1);
        foodDiary.removeFood(CONSUMPTION_DATE, EatingTime.DINNER, APPLE.getId());

        assertTrue(foodDiary.getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.DINNER).isEmpty(),
                "Dinner must be empty after the only food is removed");
    }

    @Test
    public void testRemoveFoodThrowsWhenDateHasNoEntry() {
        assertThrows(IllegalArgumentException.class,
                () -> foodDiary.removeFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE.getId()),
                "Removing food for a date with no diary entry must throw IllegalArgumentException");
    }

    @Test
    public void testGetAllFoodsByDateReturnsEmptyListWhenDateHasNoEntry() {
        List<Food> foods = foodDiary.getAllFoodsByDate(CONSUMPTION_DATE);

        assertTrue(foods.isEmpty(),
                "getAllFoodsByDate must return an empty list when no diary entry exists for that date");
    }

    @Test
    public void testGetAllFoodsByDateAggregatesFoodsAcrossAllEatingTimes() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.LUNCH, BANANA, 1);

        List<Food> allFoods = foodDiary.getAllFoodsByDate(CONSUMPTION_DATE);

        assertEquals(2, allFoods.size(),
                "getAllFoodsByDate must aggregate foods from all eating times");
    }

    @Test
    public void testGetAllFoodsByDateAndEatingTimeReturnsEmptyListWhenDateHasNoEntry() {
        List<Food> foods = foodDiary.getAllFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.BREAKFAST);

        assertTrue(foods.isEmpty(),
                "getAllFoodsByDateAndEatingTime must return an empty list when no diary entry exists");
    }

    @Test
    public void testGetAllFoodsByDateAndEatingTimeReturnsFoodsForThatEatingTimeOnly() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.LUNCH, BANANA, 1);

        List<Food> breakfastFoods = foodDiary.getAllFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.BREAKFAST);

        assertEquals(1, breakfastFoods.size(),
                "getAllFoodsByDateAndEatingTime must return only foods for the requested eating time");
        assertEquals(APPLE.getId(), breakfastFoods.get(0).getId(),
                "The returned food must be the one logged at breakfast");
    }

    @Test
    public void testGetFoodsByWeekNumberReturnsEmptyListWhenWeekHasNoEntries() {
        List<Food> foods = foodDiary.getFoodsByWeekNumber(CONSUMPTION_WEEK, CONSUMPTION_YEAR);

        assertTrue(foods.isEmpty(),
                "getFoodsByWeekNumber must return an empty list when no foods are logged for that week");
    }

    @Test
    public void testGetFoodsByWeekNumberAggregatesFoodsFromAllDatesInThatWeek() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);
        foodDiary.addFood(ANOTHER_DATE_IN_SAME_WEEK, EatingTime.LUNCH, BANANA, 1);

        List<Food> weekFoods = foodDiary.getFoodsByWeekNumber(CONSUMPTION_WEEK, CONSUMPTION_YEAR);

        assertEquals(2, weekFoods.size(),
                "getFoodsByWeekNumber must aggregate foods logged on different days of the same week");
    }

    @Test
    public void testGetFoodsByWeekNumberExcludesFoodsLoggedInDifferentWeek() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);
        foodDiary.addFood(DATE_IN_DIFFERENT_WEEK, EatingTime.BREAKFAST, BANANA, 1);

        List<Food> weekFoods = foodDiary.getFoodsByWeekNumber(CONSUMPTION_WEEK, CONSUMPTION_YEAR);

        assertEquals(1, weekFoods.size(),
                "getFoodsByWeekNumber must exclude foods logged in a different week");
        assertEquals(APPLE.getId(), weekFoods.get(0).getId(),
                "The only returned food must be the one logged in the target week");
    }

    @Test
    public void testGetFoodsByWeekNumberExcludesFoodsFromSameWeekNumberInDifferentYear() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);
        foodDiary.addFood(DATE_IN_SAME_WEEK_DIFFERENT_YEAR, EatingTime.BREAKFAST, BANANA, 1);

        List<Food> weekFoods = foodDiary.getFoodsByWeekNumber(CONSUMPTION_WEEK, CONSUMPTION_YEAR);

        assertEquals(1, weekFoods.size(),
                "getFoodsByWeekNumber must exclude foods from a different year even when the week number matches");
        assertEquals(APPLE.getId(), weekFoods.get(0).getId(),
                "The only returned food must be from the target year");
    }

    @Test
    public void testGetDailyNutritionSummaryReturnsCorrectConsumptionDate() {
        DailyNutritionSummary summary = foodDiary.getDailyNutritionSummary(CONSUMPTION_DATE);

        assertEquals(CONSUMPTION_DATE, summary.date(),
                "The summary date must match the date that was queried");
    }

    @Test
    public void testGetDailyNutritionSummaryReturnsZeroCaloriesForDateWithNoEntries() {
        DailyNutritionSummary summary = foodDiary.getDailyNutritionSummary(CONSUMPTION_DATE);

        assertEquals(0.0, summary.calories(),
                "Calories must be 0.0 when no food has been logged for that date");
    }

    @Test
    public void testGetDailyNutritionSummaryReturnsTotalCaloriesFromAllEatingTimes() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.LUNCH, BANANA, 1);

        double expectedCalories = APPLE.getCalories() + BANANA.getCalories();
        DailyNutritionSummary summary = foodDiary.getDailyNutritionSummary(CONSUMPTION_DATE);

        assertEquals(expectedCalories, summary.calories(),
                "Calories must be summed across all eating times for the given date");
    }

    @Test
    public void testGetDailyNutritionSummaryReturnsSummedMacrosWhenPresent() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, CHICKEN_BREAST, 1);
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.LUNCH, WHITE_RICE, 1);

        double expectedProtein = CHICKEN_BREAST.getProtein().orElseThrow() + WHITE_RICE.getProtein().orElseThrow();
        double expectedFats = CHICKEN_BREAST.getFats().orElseThrow() + WHITE_RICE.getFats().orElseThrow();
        double expectedCarbs = CHICKEN_BREAST.getCarbs().orElseThrow() + WHITE_RICE.getCarbs().orElseThrow();

        DailyNutritionSummary summary = foodDiary.getDailyNutritionSummary(CONSUMPTION_DATE);

        assertEquals(expectedProtein, summary.protein().orElseThrow(),
                "Protein must be summed across all foods logged on that date");
        assertEquals(expectedFats, summary.fats().orElseThrow(),
                "Fats must be summed across all foods logged on that date");
        assertEquals(expectedCarbs, summary.carbs().orElseThrow(),
                "Carbs must be summed across all foods logged on that date");
    }

    @Test
    public void testGetDailyNutritionSummaryReturnsMissingMacrosWhenNoFoodTracksNutrition() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);

        DailyNutritionSummary summary = foodDiary.getDailyNutritionSummary(CONSUMPTION_DATE);

        assertTrue(summary.protein().isEmpty(),
                "Protein must be absent when no logged food tracks that macro");
        assertTrue(summary.fats().isEmpty(),
                "Fats must be absent when no logged food tracks that macro");
        assertTrue(summary.carbs().isEmpty(),
                "Carbs must be absent when no logged food tracks that macro");
    }

    @Test
    public void testGetWeeklyNutritionSummaryByWeekNumberReturnsCorrectWeekNumber() {
        WeeklyNutritionSummary summary = foodDiary.getWeeklyNutritionSummary(CONSUMPTION_WEEK, CONSUMPTION_YEAR);

        assertEquals(CONSUMPTION_WEEK, summary.weekNumber(),
                "The summary weekNumber must match the week that was queried");
    }

    @Test
    public void testGetWeeklyNutritionSummaryByWeekNumberReturnsCorrectYear() {
        WeeklyNutritionSummary summary = foodDiary.getWeeklyNutritionSummary(CONSUMPTION_WEEK, CONSUMPTION_YEAR);

        assertEquals(CONSUMPTION_YEAR, summary.year(),
                "The summary year must match the year that was queried");
    }

    @Test
    public void testGetWeeklyNutritionSummaryByDateResolvesToCorrectWeekNumber() {
        WeeklyNutritionSummary summary = foodDiary.getWeeklyNutritionSummary(CONSUMPTION_DATE);

        assertEquals(CONSUMPTION_WEEK, summary.weekNumber(),
                "The summary weekNumber must match the week that was queried");
    }

    @Test
    public void testGetWeeklyNutritionSummaryByWeekNumberReturnsZeroCaloriesForEmptyWeek() {
        WeeklyNutritionSummary summary = foodDiary.getWeeklyNutritionSummary(CONSUMPTION_WEEK, CONSUMPTION_YEAR);

        assertEquals(0.0, summary.calories(),
                "Calories must be 0.0 when no food has been logged in that week");
    }

    @Test
    public void testGetWeeklyNutritionSummaryByWeekNumberReturnsTotalCaloriesAcrossMultipleDays() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);
        foodDiary.addFood(ANOTHER_DATE_IN_SAME_WEEK, EatingTime.LUNCH, BANANA, 1);

        double expectedCalories = APPLE.getCalories() + BANANA.getCalories();
        WeeklyNutritionSummary summary = foodDiary.getWeeklyNutritionSummary(CONSUMPTION_WEEK, CONSUMPTION_YEAR);

        assertEquals(expectedCalories, summary.calories(),
                "Calories must be summed across all days that belong to the queried week");
    }

    @Test
    public void testGetWeeklyNutritionSummaryByDateReturnsTotalCaloriesAcrossMultipleDays() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);
        foodDiary.addFood(ANOTHER_DATE_IN_SAME_WEEK, EatingTime.LUNCH, BANANA, 1);

        double expectedCalories = APPLE.getCalories() + BANANA.getCalories();
        WeeklyNutritionSummary summary = foodDiary.getWeeklyNutritionSummary(CONSUMPTION_DATE);

        assertEquals(expectedCalories, summary.calories(),
                "Calories must be summed across all days that belong to the queried week");
    }

    @Test
    public void testGetWeeklyNutritionSummaryExcludesFoodsFromSameWeekNumberInDifferentYear() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);
        foodDiary.addFood(DATE_IN_SAME_WEEK_DIFFERENT_YEAR, EatingTime.BREAKFAST, BANANA, 1);

        WeeklyNutritionSummary summary = foodDiary.getWeeklyNutritionSummary(CONSUMPTION_WEEK, CONSUMPTION_YEAR);

        assertEquals(APPLE.getCalories(), summary.calories(),
                "The weekly summary must exclude calories from the same week number in a different year");
    }

    @Test
    public void testGetDailyNutritionSummariesForWeekReturnsSevenSummaries() {
        List<DailyNutritionSummary> summaries = foodDiary.getDailyNutritionSummariesForWeek(CONSUMPTION_DATE);

        assertEquals(DAYS_IN_WEEK, summaries.size(),
                "getDailyNutritionSummariesForWeek must return exactly 7 summaries — one per day");
    }

    @Test
    public void testGetDailyNutritionSummariesForWeekRangesFromMondayToSundayOfTargetWeek() {
        List<DailyNutritionSummary> summaries = foodDiary.getDailyNutritionSummariesForWeek(CONSUMPTION_DATE);

        assertEquals(DayOfWeek.MONDAY, summaries.get(0).date().getDayOfWeek(),
                "The first summary must always be for Monday of the target week");
        assertEquals(CONSUMPTION_DATE, summaries.get(0).date(),
                "The first summary date must be the Monday of CONSUMPTION_DATE's week");
        assertEquals(SUNDAY_OF_CONSUMPTION_WEEK, summaries.get(DAYS_IN_WEEK - 1).date(),
                "The last summary date must be the Sunday of CONSUMPTION_DATE's week");
    }

    @Test
    public void testGetDailyNutritionSummariesForWeekIncludesCaloriesOnlyForDaysWithEntries() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);

        List<DailyNutritionSummary> summaries = foodDiary.getDailyNutritionSummariesForWeek(CONSUMPTION_DATE);

        DailyNutritionSummary mondaySummary = summaries.get(0);
        DailyNutritionSummary tuesdaySummary = summaries.get(1);

        assertEquals(APPLE.getCalories(), mondaySummary.calories(),
                "Monday's summary must reflect the food logged on that day");
        assertEquals(0.0, tuesdaySummary.calories(),
                "Days with no logged food must contribute 0 calories to the weekly summaries");
    }

    @Test
    public void testGetDailyMealCaloriesSummaryReturnsCorrectConsumptionDate() {
        DailyMealCaloriesSummary summary = foodDiary.getDailyMealCaloriesSummary(CONSUMPTION_DATE);

        assertEquals(CONSUMPTION_DATE, summary.date(),
                "The summary date must match the date that was queried");
    }

    @Test
    public void testGetDailyMealCaloriesSummaryContainsEntriesForAllEatingTimes() {
        DailyMealCaloriesSummary summary = foodDiary.getDailyMealCaloriesSummary(CONSUMPTION_DATE);

        long presentEatingTimes = java.util.Arrays.stream(EatingTime.values())
                .filter(time -> summary.getCaloriesFor(time) >= 0.0)
                .count();

        assertEquals(EatingTime.values().length, presentEatingTimes,
                "The summary must contain a calorie entry for every EatingTime value");
    }

    @Test
    public void testGetDailyMealCaloriesSummaryReturnsAllZerosForDateWithNoEntries() {
        DailyMealCaloriesSummary summary = foodDiary.getDailyMealCaloriesSummary(CONSUMPTION_DATE);

        for (EatingTime eatingTime : EatingTime.values()) {
            assertEquals(0.0, summary.getCaloriesFor(eatingTime),
                    eatingTime.getLabel() + " must have 0 calories when no food is logged for that date");
        }
    }

    @Test
    public void testGetDailyMealCaloriesSummaryReturnsCorrectCaloriesPerEatingTime() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.LUNCH, BANANA, 1);

        DailyMealCaloriesSummary summary = foodDiary.getDailyMealCaloriesSummary(CONSUMPTION_DATE);

        assertEquals(APPLE.getCalories(), summary.getCaloriesFor(EatingTime.BREAKFAST),
                "Breakfast calories must match the total of foods logged at breakfast");
        assertEquals(BANANA.getCalories(), summary.getCaloriesFor(EatingTime.LUNCH),
                "Lunch calories must match the total of foods logged at lunch");
    }

    @Test
    public void testGetDailyMealCaloriesSummaryReturnsZeroForEatingTimesWithNoFood() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);

        DailyMealCaloriesSummary summary = foodDiary.getDailyMealCaloriesSummary(CONSUMPTION_DATE);

        assertEquals(0.0, summary.getCaloriesFor(EatingTime.DINNER),
                "Eating times with no logged food must contribute 0 calories to the summary");
        assertEquals(0.0, summary.getCaloriesFor(EatingTime.SNACKS),
                "Eating times with no logged food must contribute 0 calories to the summary");
    }
}
