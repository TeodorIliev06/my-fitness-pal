package com.fmi.myfitnesspal.exercise;

import com.fmi.myfitnesspal.exception.UnknownExerciseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class DailyExerciseDiary {
    private final Map<String, Exercise> dailyExercises = new HashMap<>();
    private final ExercisePool exercisePool;
    private int burnedDailyCalories;

    public DailyExerciseDiary(ExercisePool exercisePool) {
        this.exercisePool = exercisePool;
        burnedDailyCalories = 0;
    }

    public void logExercise(String exerciseName) throws UnknownExerciseException {
        Exercise newExercise = exercisePool.getExerciseByName(exerciseName);

        dailyExercises.put(exerciseName, newExercise);
        burnedDailyCalories += newExercise.burnedCalories();
    }

    public void removeExercise(String exerciseName) throws UnknownExerciseException {
        if (!dailyExercises.containsKey(exerciseName)) {
            throw new UnknownExerciseException(
                String.format("Exercise with name %s is not logged in the diary.", exerciseName));
        }

        burnedDailyCalories -= dailyExercises.get(exerciseName).burnedCalories();
        dailyExercises.remove(exerciseName);
    }

    public List<Exercise> getDailyExercise() {
        return List.copyOf(dailyExercises.values());
    }

    public int getBurnedDailyCalories() {
        return burnedDailyCalories;
    }

    public List<CardioExercise> getDailyCardioExercises() {
        return dailyExercises.values().stream()
                .filter(exercise -> exercise instanceof CardioExercise)
                .map(exercise -> (CardioExercise) exercise)
                .collect(Collectors.toList());
    }
}
