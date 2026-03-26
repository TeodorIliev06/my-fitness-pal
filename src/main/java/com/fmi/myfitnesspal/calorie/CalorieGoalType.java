package com.fmi.myfitnesspal.calorie;

public enum CalorieGoalType {
    LOSE_WEIGHT(-400),
    MAINTAIN_WEIGHT(0),
    GAIN_WEIGHT(400);

    private final int calorieAdjustment;

    CalorieGoalType(int calorieAdjustment) {
        this.calorieAdjustment = calorieAdjustment;
    }

    public int getCalorieAdjustment() {
        return calorieAdjustment;
    }
}
