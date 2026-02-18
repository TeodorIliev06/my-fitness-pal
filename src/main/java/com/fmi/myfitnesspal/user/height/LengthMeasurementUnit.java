package com.fmi.myfitnesspal.user.height;

public enum LengthMeasurementUnit {
    CENTIMETER(1),
    INCH(2.54);

    private final double centimeters;

    LengthMeasurementUnit(double centimeters) {
        this.centimeters = centimeters;
    }

    public double toCentimeters() {
        return centimeters;
    }
}
