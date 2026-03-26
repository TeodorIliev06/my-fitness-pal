package org.external.chart;

import com.fmi.myfitnesspal.chart.PieChartDisplayer;
import com.fmi.myfitnesspal.chart.PieSlice;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;

import javax.swing.JFrame;
import java.util.List;

public final class PieChartWindow implements PieChartDisplayer {

    private static final int DEFAULT_WIDTH  = 600;
    private static final int DEFAULT_HEIGHT = 500;

    @Override
    public void display(String title, List<PieSlice> slices) {
        PieChart pieChart = buildChart(title, slices);
        renderChart(pieChart);
    }

    public PieChart buildChart(String title, List<PieSlice> slices) {
        PieChart chart = new PieChartBuilder()
                .width(DEFAULT_WIDTH)
                .height(DEFAULT_HEIGHT)
                .title(title)
                .build();

        slices.forEach(slice -> chart.addSeries(slice.label(), slice.value()));

        return chart;
    }

    private void renderChart(PieChart chart) {
        JFrame frame = new SwingWrapper<>(chart).displayChart();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
