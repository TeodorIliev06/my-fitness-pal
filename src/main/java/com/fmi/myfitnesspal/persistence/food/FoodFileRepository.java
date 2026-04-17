package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;

import java.util.List;

public final class FoodFileRepository implements FoodPool {

    private final FoodPool foodPool;
    private final FoodDtoMapper foodDtoMapper;
    private final PersistenceStore<FoodDto> persistenceStore;

    public FoodFileRepository(FoodPool foodPool, FoodDtoMapper foodDtoMapper,
                              PersistenceStore<FoodDto> persistenceStore) {
        this.foodPool = foodPool;
        this.foodDtoMapper = foodDtoMapper;
        this.persistenceStore = persistenceStore;
    }

    @Override
    public void addFood(Food food) {
        foodPool.addFood(food);
        saveCurrentState();
    }

    @Override
    public Food getFood(FoodId targetId) {
        return foodPool.getFood(targetId);
    }

    @Override
    public List<Food> getAllFoods() {
        return foodPool.getAllFoods();
    }

    void loadInitialState() {
        persistenceStore.load()
                .stream()
                .map(foodDtoMapper::toEntity)
                .forEach(foodPool::addFood);
    }

    private void saveCurrentState() {
        persistenceStore.save(foodDtoMapper.toDtos(foodPool.getAllFoods()));
    }
}
