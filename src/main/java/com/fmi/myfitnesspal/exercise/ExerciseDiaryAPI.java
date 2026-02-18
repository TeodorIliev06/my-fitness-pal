package com.fmi.myfitnesspal.exercise;

import com.fmi.myfitnesspal.exception.UnknownExerciseException;

import java.util.Collection;

public interface ExerciseDiaryAPI {

    /**
     * Logs an exercise in daily diary after checking if it exists.
     *
     * @throws UnknownExerciseException
     */
    void logExercise(String exerciseName)
        throws UnknownExerciseException;

    /**
     * Removes an exercise in daily diary after checking if it is already logged in the diary.
     *
     * @throws UnknownExerciseException
     */
    void removeExercise(String exerciseName) throws UnknownExerciseException;

    /**
     * @return exercises for the day. If no cardio exercises have been logged, returns empty collection
     */
    Collection<Exercise> getDailyExercise();
}
