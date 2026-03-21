package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.command.utility.NutritionSliceMapper;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.chart.PieChartDisplayer;
import com.fmi.myfitnesspal.chart.PieSlice;

import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public abstract class ChartCommand implements ExecutableCommand {

    private static final int ARGUMENTS_COUNT = 1;
    private static final String CHART_OPENED_PREFIX = "Chart opened: ";

    protected final FoodDiary foodDiary;
    protected final PieChartDisplayer chartDisplayer;
    protected final NutritionSliceMapper sliceMapper;

    protected ChartCommand(FoodDiary foodDiary,
                           PieChartDisplayer chartDisplayer,
                           NutritionSliceMapper sliceMapper) {
        this.foodDiary = foodDiary;
        this.chartDisplayer = chartDisplayer;
        this.sliceMapper = sliceMapper;
    }

    @Override
    public final String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        ChartData chart = buildChart(arguments.get(0));
        chartDisplayer.display(chart.title(), chart.slices());

        return CHART_OPENED_PREFIX + chart.title();
    }

    protected abstract ChartData buildChart(String argument) throws InvalidCommandException;

    protected record ChartData(String title, List<PieSlice> slices) {

    }
}
