package com.fmi.myfitnesspal.exercise;

import com.fmi.myfitnesspal.exception.UnknownExerciseException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ExerciseDiary {
    Map<LocalDate, DailyExerciseDiary> diary;
    ExercisePool exercisePool;

    public ExerciseDiary(ExercisePool exercisePool) {
        this.diary = new HashMap<>();
        this.exercisePool = exercisePool;
    }

    public void logExercise(LocalDate date, String exerciseName) throws UnknownExerciseException {
        if (isDiaryEmptyOn(date)) {
            diary.put(date, new DailyExerciseDiary(exercisePool));
        }
        diary.get(date).logExercise(exerciseName);
    }

    public void removeExercise(LocalDate date, String exerciseName) throws UnknownExerciseException {
        if (isDiaryEmptyOn(date)) {
            diary.put(date, new DailyExerciseDiary(exercisePool));
        }
        diary.get(date).removeExercise(exerciseName);
    }

    public List<Exercise> getDailyExercise(LocalDate date) {
        if (isDiaryEmptyOn(date)) {
            diary.put(date, new DailyExerciseDiary(exercisePool));
        }
        return diary.get(date).getDailyExercise();
    }

    public int getBurnedDailyCalories(LocalDate date) {
        if (isDiaryEmptyOn(date)) {
            diary.put(date, new DailyExerciseDiary(exercisePool));
        }
        return diary.get(date).getBurnedDailyCalories();
    }

    private boolean isDiaryEmptyOn(LocalDate date) {
        return !diary.containsKey(date);
    }
}
