package com.fmi.myfitnesspal.food;

import java.util.Optional;

public final class FoodCalculator {

    private FoodCalculator() {
    }

    public static Food recalculateFoodParameters(Food original, double servingsCount) {

        double newCalories = original.getCalories() * servingsCount;
        Optional<Double> newFats = original.getFats().map(fats -> fats * servingsCount);
        Optional<Double> newProtein = original.getProtein().map(protein -> protein * servingsCount);
        Optional<Double> newCarbs = original.getCarbs().map(carbs -> carbs * servingsCount);
        double newServingsSize = original.getServingSize() * servingsCount;

        return Food.builder(original.getId(), newServingsSize, newCalories)
                .setFats(newFats)
                .setProtein(newProtein)
                .setCarbs(newCarbs)
                .build();
    }
}
