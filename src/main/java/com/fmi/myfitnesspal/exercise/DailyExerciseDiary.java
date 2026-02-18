package com.fmi.myfitnesspal.exercise;

import com.fmi.myfitnesspal.exception.UnknownExerciseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DailyExerciseDiary implements ExerciseDiaryAPI {
    private Map<String, Exercise> dailyExercises = new HashMap<>();
    private final ExercisePool exercisePool;
    private int burnedDailyCalories;

    public DailyExerciseDiary(ExercisePool exercisePool) {
        this.exercisePool = exercisePool;
        burnedDailyCalories = 0;
    }

    @Override
    public void logExercise(String exerciseName)
        throws UnknownExerciseException {
        Exercise newExercise = exercisePool.getExerciseByName(exerciseName);

        dailyExercises.put(exerciseName, newExercise);
        burnedDailyCalories += newExercise.burnedCalories();
    }

    @Override
    public void removeExercise(String exerciseName) throws UnknownExerciseException {
        if (!dailyExercises.containsKey(exerciseName)) {
            throw new UnknownExerciseException(
                String.format("Exercise with name %s is not logged in the diary.", exerciseName));
        }

        burnedDailyCalories -= dailyExercises.get(exerciseName).burnedCalories();
        dailyExercises.remove(exerciseName);
    }

    @Override
    public List<Exercise> getDailyExercise() {
        return List.copyOf(dailyExercises.values());
    }

    public int getBurnedDailyCalories() {
        return burnedDailyCalories;
    }
}
