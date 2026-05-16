package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.DailyMealCaloriesSummary;
import com.fmi.myfitnesspal.food.DailyNutritionSummary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodPortion;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.DailyFoodEntry;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.WeeklyNutritionSummary;
import com.fmi.myfitnesspal.persistence.file.ObjectPersistenceStore;

import java.time.LocalDate;
import java.util.List;

public final class FoodDiaryFileRepository implements FoodDiary {

    private final FoodDiary foodDiary;
    private final FoodDiaryDtoMapper foodDiaryDtoMapper;
    private final ObjectPersistenceStore<FoodDiaryDto> persistenceStore;

    public FoodDiaryFileRepository(FoodDiary foodDiary, FoodDiaryDtoMapper foodDiaryDtoMapper,
                                   ObjectPersistenceStore<FoodDiaryDto> persistenceStore) {
        this.foodDiary = foodDiary;
        this.foodDiaryDtoMapper = foodDiaryDtoMapper;
        this.persistenceStore = persistenceStore;
    }

    @Override
    public void addFood(LocalDate consumptionDate, EatingTime eatingTime, Food food, double numberOfServings) {
        foodDiary.addFood(consumptionDate, eatingTime, food, numberOfServings);
        saveCurrentState();
    }

    @Override
    public void addFoodPortions(LocalDate date, EatingTime eatingTime, List<FoodPortion> foodPortions) {
        foodPortions.forEach(portion -> addFood(date, eatingTime, portion.food(), portion.servingsUsed()));
        saveCurrentState();
    }

    @Override
    public void removeFood(LocalDate consumptionDate, EatingTime eatingTime, FoodId foodId) {
        foodDiary.removeFood(consumptionDate, eatingTime, foodId);
        saveCurrentState();
    }

    @Override
    public List<Food> getFoodsByDateAndEatingTime(LocalDate consumptionDate, EatingTime eatingTime) {
        return foodDiary.getFoodsByDateAndEatingTime(consumptionDate, eatingTime);
    }

    @Override
    public List<Food> getFoodsByWeekNumber(int weekNumber, int year) {
        return foodDiary.getFoodsByWeekNumber(weekNumber, year);
    }

    @Override
    public List<Food> getAllFoodsByDate(LocalDate consumptionDate) {
        return foodDiary.getAllFoodsByDate(consumptionDate);
    }

    @Override
    public List<Food> getAllFoodsByDateAndEatingTime(LocalDate consumptionDate, EatingTime eatingTime) {
        return foodDiary.getAllFoodsByDateAndEatingTime(consumptionDate, eatingTime);
    }

    @Override
    public WeeklyNutritionSummary getWeeklyNutritionSummary(int weekNumber, int year) {
        return foodDiary.getWeeklyNutritionSummary(weekNumber, year);
    }

    @Override
    public WeeklyNutritionSummary getWeeklyNutritionSummary(LocalDate consumptionDate) {
        return foodDiary.getWeeklyNutritionSummary(consumptionDate);
    }

    @Override
    public DailyNutritionSummary getDailyNutritionSummary(LocalDate consumptionDate) {
        return foodDiary.getDailyNutritionSummary(consumptionDate);
    }

    @Override
    public List<DailyNutritionSummary> getDailyNutritionSummariesForWeek(LocalDate targetDate) {
        return foodDiary.getDailyNutritionSummariesForWeek(targetDate);
    }

    @Override
    public DailyMealCaloriesSummary getDailyMealCaloriesSummary(LocalDate consumptionDate) {
        return foodDiary.getDailyMealCaloriesSummary(consumptionDate);
    }

    @Override
    public List<DailyFoodEntry> getAllDailyFoodEntries() {
        return foodDiary.getAllDailyFoodEntries();
    }

    void loadInitialState() {
        persistenceStore.load().ifPresent(this::restoreFromDto);
    }

    private void restoreFromDto(FoodDiaryDto dto) {
        dto.foods().stream()
                .map(foodDiaryDtoMapper::toDailyFoodEntry)
                .forEach(entry -> foodDiary.addFood(
                        entry.consumptionDate(), entry.eatingTime(), entry.food(), 1.0));
    }

    private void saveCurrentState() {
        FoodDiaryDto dto = foodDiaryDtoMapper.toDto(foodDiary.getAllDailyFoodEntries());
        persistenceStore.save(dto);
    }
}
