package com.fmi.myfitnesspal.food;
public enum EatingTime {

    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACKS("Snacks");

    private final String label;

    EatingTime(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
