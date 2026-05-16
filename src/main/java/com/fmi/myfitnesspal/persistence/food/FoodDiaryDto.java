package com.fmi.myfitnesspal.persistence.food;

import java.util.List;

public record FoodDiaryDto(
    List<DailyFoodEntryDto> foods
) {

}
