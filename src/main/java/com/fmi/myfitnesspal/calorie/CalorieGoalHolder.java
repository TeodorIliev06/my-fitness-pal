package com.fmi.myfitnesspal.calorie;

import java.util.Optional;

public final class CalorieGoalHolder {

    private CalorieGoal activeCalorieGoal;

    public Optional<CalorieGoal> getActiveCalorieGoal() {
        return Optional.ofNullable(activeCalorieGoal);
    }

    public void setActiveCalorieGoal(CalorieGoal activeCalorieGoal) {
        this.activeCalorieGoal = activeCalorieGoal;
    }
}
