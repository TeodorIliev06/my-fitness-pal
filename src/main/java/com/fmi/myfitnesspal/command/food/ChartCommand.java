package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.BoundedCommand;
import com.fmi.myfitnesspal.command.utility.NutritionSliceMapper;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.chart.PieChartDisplayer;
import com.fmi.myfitnesspal.chart.PieSlice;

import java.util.List;

public abstract class ChartCommand extends BoundedCommand {

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
    public final String doExecute(List<String> arguments) throws InvalidCommandException {
        ChartData chart = buildChart(arguments);
        chartDisplayer.display(chart.title(), chart.slices());

        return CHART_OPENED_PREFIX + chart.title();
    }

    protected abstract ChartData buildChart(List<String> arguments) throws InvalidCommandException;

    protected record ChartData(String title, List<PieSlice> slices) {

    }
}
