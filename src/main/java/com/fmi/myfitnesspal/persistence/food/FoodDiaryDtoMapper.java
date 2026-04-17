package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.DailyFoodEntry;
import com.fmi.myfitnesspal.food.Meal;
import com.fmi.myfitnesspal.food.MealId;
import com.fmi.myfitnesspal.food.DailyMealEntry;

import java.util.List;

public final class FoodDiaryDtoMapper {

    private final FoodDtoMapper foodDtoMapper;

    public FoodDiaryDtoMapper(FoodDtoMapper foodDtoMapper) {
        this.foodDtoMapper = foodDtoMapper;
    }

    public DailyFoodEntryDto toDailyFoodEntryDto(DailyFoodEntry entry) {
        return new DailyFoodEntryDto(
                entry.consumptionDate(),
                entry.eatingTime(),
                foodDtoMapper.toDto(entry.food())
        );
    }

    public DailyFoodEntry toDailyFoodEntry(DailyFoodEntryDto dto) {
        return new DailyFoodEntry(
                dto.date(),
                dto.eatingTime(),
                foodDtoMapper.toEntity(dto.food())
        );
    }

    public DailyMealEntryDto toDailyMealEntryDto(DailyMealEntry entry) {
        List<FoodDto> foodDtos = entry.meal().getFoods().stream()
                .map(foodDtoMapper::toDto)
                .toList();

        return new DailyMealEntryDto(
                entry.consumptionDate(),
                entry.eatingTime(),
                entry.meal().getId().name(),
                entry.meal().getId().description(),
                foodDtos
        );
    }

    public DailyMealEntry toDailyMealEntry(DailyMealEntryDto dto) {
        MealId mealId = new MealId(dto.name(), dto.description());
        Meal meal = new Meal(mealId);

        dto.foods().stream()
                .map(foodDtoMapper::toEntity)
                .forEach(food -> addFoodWithSingleServing(meal, food));

        return new DailyMealEntry(dto.date(), dto.eatingTime(), meal);
    }

    public FoodDiaryDto toDto(List<DailyFoodEntry> foodEntries, List<DailyMealEntry> mealEntries) {
        List<DailyFoodEntryDto> foodDtos = foodEntries.stream()
                .map(this::toDailyFoodEntryDto)
                .toList();

        List<DailyMealEntryDto> mealDtos = mealEntries.stream()
                .map(this::toDailyMealEntryDto)
                .toList();

        return new FoodDiaryDto(foodDtos, mealDtos);
    }

    /* Calories stored in the DTO are already scaled. Re-adding with servings=1.0
       prevents double-multiplication when the Meal reconstructs the total. */
    private void addFoodWithSingleServing(Meal targetMeal, Food food) {
        targetMeal.addFood(food, 1.0);
    }
}
