package com.fmi.myfitnesspal.user.activitylevel;

public enum ActivityLevel {
    NOT_VERY_ACTIVE("Spend most of the day sitting"),
    LIGHTLY_ACTIVE("Spend a good part of the day on your feet"),
    ACTIVE("Spend a good part of the day doing some physical activity"),
    VERY_ACTIVE("Spend a good part of the day doing heavy physical activity");

    private final String description;

    ActivityLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
