package com.fmi.myfitnesspal.food;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class FoodDiaryTest {

    private static final LocalDate CONSUMPTION_DATE = LocalDate.of(2025, 5, 26);
    private static final LocalDate ANOTHER_DATE_IN_SAME_WEEK = LocalDate.of(2025, 5, 28);
    private static final LocalDate DATE_IN_DIFFERENT_WEEK = LocalDate.of(2025, 6, 2);
    private static final int CONSUMPTION_WEEK = CONSUMPTION_DATE.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

    private static final Food APPLE =
            Food.builder(new FoodId("Apple", "green"), 50, 10).build();
    private static final Food BANANA =
            Food.builder(new FoodId("Banana", "yellow"), 100, 90).build();

    private FoodDiary foodDiary;

    @BeforeEach
    public void setUp() {
        foodDiary = new FoodDiary();
    }

    @Test
    public void testAddFood() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);

        List<Food> breakfastFoods = foodDiary.getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.BREAKFAST);

        assertEquals(1, breakfastFoods.size());
        assertEquals(APPLE.getId(), breakfastFoods.get(0).getId());
    }

    @Test
    public void testAddFoodRecalculatesNutritionByServingsCount() {
        int servingsCount = 2;
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, servingsCount);

        Food storedFood = foodDiary.getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.BREAKFAST).get(0);

        assertEquals(APPLE.getCalories() * servingsCount, storedFood.getCalories());
        assertEquals(APPLE.getServingSize() * servingsCount, storedFood.getServingSize());
    }

    @Test
    public void testAddFoodCreatesNewDailyEntryWhenNoExistingDiary() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);

        assertFalse(foodDiary.getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.BREAKFAST).isEmpty());
    }

    @Test
    public void testAddMeal() {
        Meal greekSalad = createMealWithoutFood();
        foodDiary.addMeal(CONSUMPTION_DATE, EatingTime.LUNCH, greekSalad);

        List<Meal> lunchMeals = foodDiary.getMealsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.LUNCH);

        assertFalse(lunchMeals.isEmpty());
        assertEquals(greekSalad.getId(), lunchMeals.get(0).getId());
    }

    @Test
    public void testRemoveFood() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.DINNER, APPLE, 1);
        foodDiary.removeFood(CONSUMPTION_DATE, EatingTime.DINNER, APPLE.getId());

        assertTrue(foodDiary.getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.DINNER).isEmpty());
    }

    @Test
    public void testRemoveFoodThrowsWhenDateHasNoEntry() {
        assertThrows(IllegalArgumentException.class,
                () -> foodDiary.removeFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE.getId()));
    }

    @Test
    public void testRemoveMeal() {
        Meal greekSalad = createMealWithoutFood();
        foodDiary.addMeal(CONSUMPTION_DATE, EatingTime.BREAKFAST, greekSalad);
        foodDiary.removeMeal(CONSUMPTION_DATE, EatingTime.BREAKFAST, greekSalad.getId());

        assertTrue(foodDiary.getMealsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.BREAKFAST).isEmpty());
    }

    @Test
    public void testRemoveMealThrowsWhenDateHasNoEntry() {
        Meal greekSalad = createMealWithoutFood();
        assertThrows(IllegalArgumentException.class,
                () -> foodDiary.removeMeal(CONSUMPTION_DATE, EatingTime.BREAKFAST, greekSalad.getId()));
    }

    @Test
    public void testGetAllFoodsByDateReturnsEmptyListWhenDateHasNoEntry() {
        List<Food> foods = foodDiary.getAllFoodsByDate(CONSUMPTION_DATE);

        assertTrue(foods.isEmpty());
    }

    @Test
    public void testGetAllFoodsByDateAggregatesFoodsAcrossAllEatingTimes() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.LUNCH, BANANA, 1);

        List<Food> allFoods = foodDiary.getAllFoodsByDate(CONSUMPTION_DATE);

        assertEquals(2, allFoods.size());
    }

    @Test
    public void testGetAllFoodsByDateIncludesFoodsFromMeals() {
        Meal mealContainingApple = createMealContaining(APPLE);
        foodDiary.addMeal(CONSUMPTION_DATE, EatingTime.DINNER, mealContainingApple);

        List<Food> allFoods = foodDiary.getAllFoodsByDate(CONSUMPTION_DATE);

        assertTrue(allFoods.stream().anyMatch(f -> f.getId().equals(APPLE.getId())));
    }

    @Test
    public void testGetAllFoodsByDateAndEatingTimeReturnsEmptyListWhenDateHasNoEntry() {
        List<Food> foods = foodDiary.getAllFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.BREAKFAST);

        assertTrue(foods.isEmpty());
    }

    @Test
    public void testGetAllFoodsByDateAndEatingTimeReturnsFoodsForThatEatingTimeOnly() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.LUNCH, BANANA, 1);

        List<Food> breakfastFoods = foodDiary.getAllFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.BREAKFAST);

        assertEquals(1, breakfastFoods.size());
        assertEquals(APPLE.getId(), breakfastFoods.get(0).getId());
    }

    @Test
    public void testGetAllFoodsByDateAndEatingTimeIncludesFoodsFromMeals() {
        Meal mealContainingApple = createMealContaining(APPLE);
        foodDiary.addMeal(CONSUMPTION_DATE, EatingTime.DINNER, mealContainingApple);

        List<Food> dinnerFoods = foodDiary.getAllFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.DINNER);

        assertTrue(dinnerFoods.stream().anyMatch(f -> f.getId().equals(APPLE.getId())));
    }

    @Test
    public void testGetFoodsByWeekNumberReturnsEmptyListWhenWeekHasNoEntries() {
        List<Food> foods = foodDiary.getFoodsByWeekNumber(CONSUMPTION_WEEK);

        assertTrue(foods.isEmpty());
    }

    @Test
    public void testGetFoodsByWeekNumberAggregatesFoodsFromAllDatesInThatWeek() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);
        foodDiary.addFood(ANOTHER_DATE_IN_SAME_WEEK, EatingTime.LUNCH, BANANA, 1);

        List<Food> weekFoods = foodDiary.getFoodsByWeekNumber(CONSUMPTION_WEEK);

        assertEquals(2, weekFoods.size());
    }

    @Test
    public void testGetFoodsByWeekNumberExcludesFoodsLoggedInDifferentWeek() {
        foodDiary.addFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE, 1);
        foodDiary.addFood(DATE_IN_DIFFERENT_WEEK, EatingTime.BREAKFAST, BANANA, 1);

        List<Food> weekFoods = foodDiary.getFoodsByWeekNumber(CONSUMPTION_WEEK);

        assertEquals(1, weekFoods.size());
        assertEquals(APPLE.getId(), weekFoods.get(0).getId());
    }

    private Meal createMealWithoutFood() {
        return new Meal(new MealId("Greek Salad", "feta and olives"));
    }

    private Meal createMealContaining(Food food) {
        Meal meal = new Meal(new MealId("Mixed Bowl", "varied ingredients"));
        meal.addFood(food, 1);
        return meal;
    }
}
