package com.fmi.myfitnesspal.exercise;

import com.fmi.myfitnesspal.exception.UnknownExerciseException;

import java.util.Map;

public interface ExercisePoolAPI {

    Exercise createExercise(Exercise newExercise);

    Map<String, CardioExercise> getCardioExercises();

    Map<String, StrengthExercise> getStrengthExercises();
    Map<String, Workout> getWorkouts();
    Map<String, Exercise> getExercises();
    Exercise getExerciseByName(String name) throws UnknownExerciseException;
}
