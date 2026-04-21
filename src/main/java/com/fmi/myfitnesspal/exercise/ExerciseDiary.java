package com.fmi.myfitnesspal.exercise;

import com.fmi.myfitnesspal.exception.UnknownExerciseException;

import java.time.LocalDate;
import java.util.Collection;

public interface ExerciseDiary {

    void logExercise(LocalDate date, String exerciseName) throws UnknownExerciseException;

    void removeExercise(LocalDate date, String exerciseName) throws UnknownExerciseException;

    Collection<Exercise> getDailyExercise(LocalDate date);

    WeeklyCardioSummary getWeeklyCardioSummary(int weekNumber, int year);

    WeeklyCardioSummary getWeeklyCardioSummary(LocalDate consumptionDate);
}
