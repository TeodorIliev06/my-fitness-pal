package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.utility.NutritionSliceMapper;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodCalculator;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.WeeklyNutritionSummary;
import com.fmi.myfitnesspal.chart.PieChartDisplayer;
import com.fmi.myfitnesspal.chart.PieSlice;

import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.getWeekNumber;

public final class ShowWeeklyNutrientsCommand extends ChartCommand {

    private static final String COMMAND_NAME = "show-weekly-nutrients";

    public ShowWeeklyNutrientsCommand(FoodDiary foodDiary, PieChartDisplayer chartDisplayer,
                                      NutritionSliceMapper sliceMapper) {
        super(foodDiary, chartDisplayer, sliceMapper);
    }

    @Override
    protected ChartData buildChart(String argument) throws InvalidCommandException {
        int weekNumber = getWeekNumber(argument);
        WeeklyNutritionSummary summary = FoodCalculator.getWeeklyNutritionSummary(foodDiary, weekNumber);

        String title = "Weekly Nutrients — Week " + weekNumber;

        List<PieSlice> slices = sliceMapper.fromNutritionSummary(summary);
        return new ChartData(title, slices);
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <weekNumber | date>";
    }
}
