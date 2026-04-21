package com.fmi.myfitnesspal.exercise;

public record WeeklyCardioSummary(
        int weekNumber,
        int year,
        int burnedCalories,
        int totalMinutes
) {

}
