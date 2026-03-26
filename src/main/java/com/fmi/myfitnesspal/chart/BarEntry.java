package com.fmi.myfitnesspal.chart;

public record BarEntry(String label, double value) {

    public BarEntry {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("BarEntry label cannot be null or blank");
        }
        if (value < 0) {
            throw new IllegalArgumentException("BarEntry value cannot be negative");
        }
    }
}
