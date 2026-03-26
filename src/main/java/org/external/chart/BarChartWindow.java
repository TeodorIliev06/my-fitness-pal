package org.external.chart;

import com.fmi.myfitnesspal.chart.BarChartDisplayer;
import com.fmi.myfitnesspal.chart.BarEntry;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

import javax.swing.JFrame;
import java.util.Collections;
import java.util.List;

public final class BarChartWindow implements BarChartDisplayer {

    private static final int DEFAULT_WIDTH = 700;
    private static final int DEFAULT_HEIGHT = 500;

    @Override
    public void display(String title, List<BarEntry> entries, int goalLine) {
        CategoryChart barChart = buildChart(title, entries, goalLine);
        renderChart(barChart);
    }

    public CategoryChart buildChart(String title, List<BarEntry> entries, int goalLine) {
        CategoryChart chart = new CategoryChartBuilder()
                .width(DEFAULT_WIDTH)
                .height(DEFAULT_HEIGHT)
                .title(title)
                .build();

        List<String> dayLabels = entries.stream().map(BarEntry::label).toList();
        List<Double> dailyCalories = entries.stream().map(BarEntry::value).toList();
        List<Double> goalValues = Collections.nCopies(dayLabels.size(), (double) goalLine);

        chart.addSeries("Calories", dayLabels, dailyCalories);
        chart.addSeries("Goal", dayLabels, goalValues);

        return chart;
    }

    private void renderChart(CategoryChart chart) {
        JFrame frame = new SwingWrapper<>(chart).displayChart();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
