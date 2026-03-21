package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.utility.NutritionSliceMapper;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.DailyMealCaloriesSummary;
import com.fmi.myfitnesspal.food.FoodCalculator;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.chart.PieChartDisplayer;
import com.fmi.myfitnesspal.chart.PieSlice;

import java.time.LocalDate;
import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.DATE_FORMATTER;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseDate;

public final class ShowDailyMealCaloriesCommand extends ChartCommand {

    private static final String COMMAND_NAME = "show-daily-meal-calories";

    public ShowDailyMealCaloriesCommand(FoodDiary foodDiary,
                                        PieChartDisplayer chartDisplayer, NutritionSliceMapper sliceMapper) {
        super(foodDiary, chartDisplayer, sliceMapper);
    }

    @Override
    protected ChartData buildChart(String argument) throws InvalidCommandException {
        LocalDate date = parseDate(argument);
        DailyMealCaloriesSummary summary = FoodCalculator.getDailyMealCaloriesSummary(foodDiary, date);

        String title = "Daily Calories by Meal Time — " + date.format(DATE_FORMATTER);
        List<PieSlice> slices = sliceMapper.fromDailyMealCaloriesSummary(summary);

        return new ChartData(title, slices);
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <date>";
    }
}
