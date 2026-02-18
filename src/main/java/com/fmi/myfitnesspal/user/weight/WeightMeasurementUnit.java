package com.fmi.myfitnesspal.user.weight;

public enum WeightMeasurementUnit {
    KILOGRAM(1),
    POUND(2.20462262),
    STONE(0.157473044);

    private final double kilograms;

    WeightMeasurementUnit(double kilograms) {
        this.kilograms = kilograms;
    }

    public double toKilograms() {
        return kilograms;
    }
}
