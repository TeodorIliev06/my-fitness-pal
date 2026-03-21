package com.fmi.myfitnesspal.chart;

public record PieSlice(String label, double value) {

    public PieSlice {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("PieSlice label cannot be null or blank");
        }
        if (value < 0) {
            throw new IllegalArgumentException("PieSlice value cannot be negative");
        }
    }
}
