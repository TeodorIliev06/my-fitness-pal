package com.fmi.myfitnesspal.food;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;

public final class FoodDiaryTest {

    private FoodDiary foodDiary;

    @BeforeEach
    public void setUp() {
        foodDiary = new FoodDiary();
    }

    @Test
    public void testAddFood() {
        LocalDate date = LocalDate.now();
        EatingTime eatingTime = EatingTime.BREAKFAST;
        Food food = Food.builder(new FoodId("Apple", "green"), 50, 10).build();

        foodDiary.addFood(date, eatingTime, food, 2);

        List<Food> foods = foodDiary.getFoodsByDateAndEatingTime(date, eatingTime);

        assertEquals(foods.size(), 1);
        assertEquals(food.getId(), foods.get(0).getId());
        assertEquals(100, foods.get(0).getServingSize());
        assertEquals(20, foods.get(0).getCalories());
    }

    @Test
    public void testAddMeal() {
        LocalDate date = LocalDate.now();
        EatingTime eatingTime = EatingTime.LUNCH;
        Meal meal = new Meal(new MealId("Salad", "greek"));

        foodDiary.addMeal(date, eatingTime, meal);

        List<Meal> meals = foodDiary.getMealsByDateAndEatingTime(date, eatingTime);

        assertFalse(meals.isEmpty());
        assertEquals(meal.getId(), meals.get(0).getId());
    }

    @Test
    public void testRemoveFood() {
        LocalDate date = LocalDate.now();
        EatingTime eatingTime = EatingTime.DINNER;
        Food food = Food.builder(new FoodId("Apple", "green"), 50, 10).build();

        foodDiary.addFood(date, eatingTime, food, 1);
        foodDiary.removeFood(date, eatingTime, food.getId());

        List<Food> foods = foodDiary.getFoodsByDateAndEatingTime(date, eatingTime);

        assertTrue(foods.isEmpty());
    }

    @Test
    public void testRemoveMeal() {
        LocalDate date = LocalDate.now();
        EatingTime eatingTime = EatingTime.BREAKFAST;
        Meal meal = new Meal(new MealId("English breakfast", "eggs and sausages"));

        foodDiary.addMeal(date, eatingTime, meal);
        foodDiary.removeMeal(date, eatingTime, meal.getId());

        List<Meal> meals = foodDiary.getMealsByDateAndEatingTime(date, eatingTime);

        assertTrue(meals.isEmpty());
    }

    @Test
    public void testRemoveFoodWithNonExistingDateThrowsException() {
        LocalDate date = LocalDate.now();
        EatingTime eatingTime = EatingTime.BREAKFAST;
        FoodId foodId = new FoodId("Apple", "green");

        assertThrows(IllegalArgumentException.class, () -> foodDiary.removeFood(date, eatingTime, foodId));
    }

    @Test
    public void testRemoveMealWithNonExistingDateThrowsException() {
        LocalDate date = LocalDate.now();
        EatingTime eatingTime = EatingTime.BREAKFAST;
        MealId mealId = new MealId("Salad", "greek");

        assertThrows(IllegalArgumentException.class, () -> foodDiary.removeMeal(date, eatingTime, mealId));
    }
}
