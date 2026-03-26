package com.fmi.myfitnesspal.chart;

import java.util.List;

@FunctionalInterface
public interface BarChartDisplayer {
    void display(String title, List<BarEntry> entries, int goalLine);
}
