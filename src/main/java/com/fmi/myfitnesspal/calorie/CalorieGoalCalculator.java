package com.fmi.myfitnesspal.calorie;

import com.fmi.myfitnesspal.user.sex.Sex;

public final class CalorieGoalCalculator {

    private CalorieGoalCalculator() {
    }

    public static CalorieGoal calculateCalories(Sex sex, CalorieGoalType goalType) {
        int dailyCalorieTarget = sex.getBaseBmr() + goalType.getCalorieAdjustment();
        return new CalorieGoal(dailyCalorieTarget);
    }
}
