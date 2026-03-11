package com.fmi.myfitnesspal.food;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class DailyFoodDiaryTest extends FoodDiaryBase {

    @Test
    void testAddFood() {
        diary.addFood(EatingTime.BREAKFAST, firstFood, 1);
        assertTrue(diary.getFoodsByEatingTime(EatingTime.BREAKFAST).stream()
                .anyMatch(f -> f.getId().equals(firstFood.getId())));
    }

    @Test
    void testAddFoodRecalculatesServings() {
        diary.addFood(EatingTime.BREAKFAST, firstFood, 2);
        Food addedFood = diary.getFoodsByEatingTime(EatingTime.BREAKFAST).get(0);
        assertEquals(firstFood.getCalories() * 2, addedFood.getCalories());
        assertEquals(firstFood.getServingSize() * 2, addedFood.getServingSize());
    }

    @Test
    void testFoodIsIsolatedByEatingTime() {
        diary.addFood(EatingTime.BREAKFAST, firstFood, 1);
        assertTrue(diary.getFoodsByEatingTime(EatingTime.LUNCH).isEmpty());
    }

    @Test
    void testAddMeal() {
        diary.addMeal(EatingTime.LUNCH, firstMeal);
        assertTrue(diary.getMealsByEatingTime(EatingTime.LUNCH).contains(firstMeal));
    }

    @Test
    void testRemoveFood() {
        diary.addFood(EatingTime.BREAKFAST, firstFood, 1);
        diary.removeFood(EatingTime.BREAKFAST, firstFood.getId());
        assertTrue(diary.getFoodsByEatingTime(EatingTime.BREAKFAST).isEmpty());
    }

    @Test
    void testRemoveFoodThrowsWhenNotPresent() {
        assertThrows(IllegalArgumentException.class,
                () -> diary.removeFood(EatingTime.BREAKFAST, firstFood.getId()));
    }

    @Test
    void testRemoveFoodThrowsWhenNotPresentInPopulatedDiary() {
        diary.addFood(EatingTime.BREAKFAST, firstFood, 1);
        assertThrows(IllegalArgumentException.class,
                () -> diary.removeFood(EatingTime.BREAKFAST, secondFood.getId()));
    }

    @Test
    void testRemoveFoodDoesNotAffectOtherFoodsInSameEatingTime() {
        diary.addFood(EatingTime.BREAKFAST, firstFood, 1);
        diary.addFood(EatingTime.BREAKFAST, secondFood, 1);
        diary.removeFood(EatingTime.BREAKFAST, firstFood.getId());

        assertTrue(diary.getFoodsByEatingTime(EatingTime.BREAKFAST).stream()
                .anyMatch(f -> f.getId().equals(secondFood.getId())));
    }

    @Test
    void testRemoveMeal() {
        diary.addMeal(EatingTime.LUNCH, firstMeal);
        diary.removeMeal(EatingTime.LUNCH, firstMeal.getId());
        assertTrue(diary.getMealsByEatingTime(EatingTime.LUNCH).isEmpty());
    }

    @Test
    void testRemoveMealThrowsWhenNotPresent() {
        assertThrows(IllegalArgumentException.class,
                () -> diary.removeMeal(EatingTime.LUNCH, firstMeal.getId()));
    }

    @Test
    void testGetAllFoodsAggregatesAcrossEatingTimes() {
        Food mealIngredient = Food.builder(new FoodId("pasta", "white"), 100, 350)
                .setProtein(Optional.of(12.0))
                .build();
        firstMeal.addFood(mealIngredient, 1);

        diary.addFood(EatingTime.BREAKFAST, firstFood, 1);
        diary.addFood(EatingTime.LUNCH, secondFood, 1);
        diary.addMeal(EatingTime.DINNER, firstMeal);

        assertTrue(diary.getAllFoods().stream().anyMatch(f -> f.getId().equals(firstFood.getId())));
        assertTrue(diary.getAllFoods().stream().anyMatch(f -> f.getId().equals(secondFood.getId())));
        assertTrue(diary.getAllFoods().stream().anyMatch(f -> f.getId().equals(mealIngredient.getId())));
    }
}
