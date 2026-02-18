package com.fmi.myfitnesspal.user.goal;

import java.util.Set;

public enum GoalType {
    LOSE_WEIGHT(-100, CompatibilityType.SINGLE),
    MAINTAIN_WEIGHT(0, CompatibilityType.SINGLE),
    GAIN_WEIGHT(+100, CompatibilityType.SINGLE),
    GAIN_MUSCLE(0, CompatibilityType.MULTIPLE),
    MODIFY_MY_DIET(0, CompatibilityType.MULTIPLE),
    MANAGE_STRESS(0, CompatibilityType.MULTIPLE);

    /**
     * The {@code CompatibilityType} enum defines the compatibility of a goal,
     * indicating whether it can be combined with other goals.
     */
    enum CompatibilityType {
        SINGLE,
        MULTIPLE;
    }

    private final int calories;
    private final CompatibilityType type;

    GoalType(int calories, CompatibilityType type) {
        this.calories = calories;
        this.type = type;
    }

    public int getCalories() {
        return calories;
    }

    public static boolean checkGoalsCompatibility(Set<GoalType> primaryGoals) {
        return primaryGoals.stream().filter(arg -> arg.type.equals(CompatibilityType.SINGLE)).count() <= 1;
    }
}
