package com.fmi.myfitnesspal.exercise;

public abstract class Exercise {
    private String name;
    private int burnedCalories;

    Exercise(String name, int burnedCalories) {
        this.name = name;
        this.burnedCalories = burnedCalories;
    }

    /**
     * @return name of the exercise
     */
    String name() {
        return name;
    }

    /**
     * calculate the calories that the exercise burns
     * @return calories of the exercise
     */
    public int burnedCalories() {
        return burnedCalories;
    };

    /**
     * @param description
     * sets name of the exercise
     */
    void setName(String description) {
        this.name = description;
    }

    /**
     * @param calories
     * sets calorie amount of the exercise
     */
    void setCalories(int calories) {
        this.burnedCalories = calories;
    }
}
