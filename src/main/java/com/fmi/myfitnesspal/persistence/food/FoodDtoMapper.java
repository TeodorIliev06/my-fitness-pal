package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.persistence.PersistenceMapper;

import java.util.Optional;

public final class FoodDtoMapper implements PersistenceMapper<Food, FoodDto> {

    @Override
    public FoodDto toDto(Food food) {
        return new FoodDto(
                food.getId().brand(),
                food.getId().description(),
                food.getServingSize(),
                food.getCalories(),
                food.getFats().orElse(null),
                food.getProtein().orElse(null),
                food.getCarbs().orElse(null)
        );
    }

    @Override
    public Food toEntity(FoodDto dto) {
        FoodId id = new FoodId(dto.brand(), dto.description());
        return Food.builder(id, dto.servingSize(), dto.calories())
                .setFats(Optional.ofNullable(dto.fats()))
                .setProtein(Optional.ofNullable(dto.protein()))
                .setCarbs(Optional.ofNullable(dto.carbs()))
                .build();
    }
}
