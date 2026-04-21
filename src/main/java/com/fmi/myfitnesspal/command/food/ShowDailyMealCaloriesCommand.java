package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.utility.NutritionSliceMapper;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.DailyMealCaloriesSummary;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.chart.PieChartDisplayer;
import com.fmi.myfitnesspal.chart.PieSlice;

import java.time.LocalDate;
import java.util.List;

import static com.fmi.myfitnesspal.utility.DateHelper.parseDate;
import static com.fmi.myfitnesspal.utility.DateHelper.DATE_FORMATTER;

public final class ShowDailyMealCaloriesCommand extends ChartCommand {

    @Override protected int minArgCount() {
        return 1;
    }
    @Override protected int maxArgCount() {
        return 1;
    }

    private static final String COMMAND_NAME = "show-daily-meal-calories";

    public ShowDailyMealCaloriesCommand(FoodDiary foodDiary,
                                        PieChartDisplayer chartDisplayer, NutritionSliceMapper sliceMapper) {
        super(foodDiary, chartDisplayer, sliceMapper);
    }

    @Override
    protected ChartData buildChart(List<String> arguments) throws InvalidCommandException {
        LocalDate date = parseDate(arguments.get(0));
        DailyMealCaloriesSummary summary = foodDiary.getDailyMealCaloriesSummary(date);

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
