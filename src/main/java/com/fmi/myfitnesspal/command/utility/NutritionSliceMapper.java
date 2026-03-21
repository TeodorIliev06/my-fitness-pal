package com.fmi.myfitnesspal.command.utility;

import com.fmi.myfitnesspal.food.DailyMealCaloriesSummary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.NutritionSummary;
import com.fmi.myfitnesspal.chart.PieSlice;

import java.util.List;
import java.util.stream.Stream;

public final class NutritionSliceMapper {

    public List<PieSlice> fromNutritionSummary(NutritionSummary summary) {
        return List.of(
                new PieSlice("Protein", summary.protein().orElse(0.0)),
                new PieSlice("Carbs",   summary.carbs().orElse(0.0)),
                new PieSlice("Fats",    summary.fats().orElse(0.0))
        );
    }

    public List<PieSlice> fromDailyMealCaloriesSummary(DailyMealCaloriesSummary summary) {
        return Stream.of(
                        new PieSlice(EatingTime.BREAKFAST.getLabel(), summary.breakfastCalories()),
                        new PieSlice(EatingTime.LUNCH.getLabel(), summary.lunchCalories()),
                        new PieSlice(EatingTime.DINNER.getLabel(), summary.dinnerCalories()),
                        new PieSlice(EatingTime.SNACKS.getLabel(), summary.snackCalories())
                )
                .filter(slice -> slice.value() > 0)
                .toList();
    }
}
