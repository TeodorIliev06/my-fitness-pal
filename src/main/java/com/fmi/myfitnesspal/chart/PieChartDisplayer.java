package com.fmi.myfitnesspal.chart;

import java.util.List;

@FunctionalInterface
public interface PieChartDisplayer {
    void display(String title, List<PieSlice> slices);
}
