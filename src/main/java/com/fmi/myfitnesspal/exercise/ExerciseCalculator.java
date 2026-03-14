package com.fmi.myfitnesspal.exercise;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;

public final class ExerciseCalculator {

    private ExerciseCalculator() {
    }

    public static WeeklyCardioSummary getWeeklyCardio(ExerciseDiary diary, int weekNumber) {
        List<CardioExercise> weeklyCardio = diary.getCardioExercisesByWeekNumber(weekNumber);

        int burnedCalories = weeklyCardio.stream()
                .mapToInt(CardioExercise::burnedCalories)
                .sum();

        int totalMinutes = weeklyCardio.stream()
                .mapToInt(CardioExercise::durationInMinutes)
                .sum();

        return new WeeklyCardioSummary(weekNumber, burnedCalories, totalMinutes);
    }

    public static WeeklyCardioSummary getWeeklyCardio(ExerciseDiary diary, LocalDate date) {
        int weekNumber = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return getWeeklyCardio(diary, weekNumber);
    }
}
