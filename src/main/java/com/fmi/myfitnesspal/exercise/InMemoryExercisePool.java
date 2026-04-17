package com.fmi.myfitnesspal.exercise;

import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.UnknownExerciseException;

import java.util.HashMap;
import java.util.Map;

public final class InMemoryExercisePool implements ExercisePool {
    private final Map<String, Exercise> exercises = new HashMap<>();

    @Override
    public Exercise createExercise(Exercise newExercise) {
        return exercises.put(newExercise.name(), newExercise);
    }

    @Override
    public Map<String, CardioExercise> getCardioExercises() {
        Map<String, CardioExercise> toReturn = new HashMap<>();

        for (Exercise e : exercises.values()) {
            if (e instanceof CardioExercise) {
                toReturn.put(e.name(), (CardioExercise) e);
            }
        }

        return toReturn;
    }

    @Override
    public Map<String, StrengthExercise> getStrengthExercises() {
        Map<String, StrengthExercise> toReturn = new HashMap<>();

        for (Exercise e : exercises.values()) {
            if (e instanceof StrengthExercise) {
                toReturn.put(e.name(), (StrengthExercise) e);
            }
        }

        return toReturn;
    }

    @Override
    public Map<String, Workout> getWorkouts() {
        Map<String, Workout> toReturn = new HashMap<>();

        for (Exercise e : exercises.values()) {
            if (e instanceof Workout) {
                toReturn.put(e.name(), (Workout) e);
            }
        }

        return toReturn;
    }

    @Override
    public Map<String, Exercise> getExercises() {
        return exercises;
    }

    @Override
    public Exercise getExerciseByName(String name) throws UnknownExerciseException {
        Exercise exercise = exercises.get(name);

        if (exercise == null) {
            throw new UnknownExerciseException(GlobalConstants.NOT_EXISTING_EXERCISE_MESSAGE);
        }

        return exercise;
    }
}
