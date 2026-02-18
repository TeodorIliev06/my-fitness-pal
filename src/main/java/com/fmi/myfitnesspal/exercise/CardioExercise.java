package com.fmi.myfitnesspal.exercise;

import java.time.LocalTime;

public final class CardioExercise extends Exercise {
    private final int durationInMinutes;
    private final LocalTime startTime;

    public CardioExercise(String name, int durationInMinutes, int burnedCalories, LocalTime startTime) {
        super(name, burnedCalories);
        this.durationInMinutes = durationInMinutes;
        this.startTime = startTime;
    }

    public int durationInMinutes() {
        return durationInMinutes;
    }

    public LocalTime startTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "CardioExercise{"
            + "name=" + super.name()
            + ", durationInMinutes=" + durationInMinutes
            + ", burnedCalories=" + super.burnedCalories()
            + ", startTime=" + startTime
            + '}';
    }
}
