package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.DailyFoodEntry;

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

    public FoodDiaryDto toDto(List<DailyFoodEntry> foodEntries) {
        List<DailyFoodEntryDto> foodDtos = foodEntries.stream()
                .map(this::toDailyFoodEntryDto)
                .toList();

        return new FoodDiaryDto(foodDtos);
    }
}
