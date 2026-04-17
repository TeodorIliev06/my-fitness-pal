package com.fmi.myfitnesspal.exercise;

import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.UnknownExerciseException;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class InMemoryExerciseDiary implements ExerciseDiary {
    private final Map<LocalDate, DailyExerciseDiary> diary;
    private final ExercisePool exercisePool;

    public InMemoryExerciseDiary(ExercisePool exercisePool) {
        this.diary = new HashMap<>();
        this.exercisePool = exercisePool;
    }

    @Override
    public void logExercise(LocalDate date, String exerciseName) throws UnknownExerciseException {
        getOrCreateDailyDiary(date).logExercise(exerciseName);
    }

    public void removeExercise(LocalDate date, String exerciseName) throws UnknownExerciseException {
        getOrCreateDailyDiary(date).removeExercise(exerciseName);
    }

    public List<Exercise> getDailyExercise(LocalDate date) {
        return getOrCreateDailyDiary(date).getDailyExercise();
    }

    public int getBurnedDailyCalories(LocalDate date) {
        return getOrCreateDailyDiary(date).getBurnedDailyCalories();
    }

    public List<CardioExercise> getCardioExercisesByWeekNumber(int weekNumber) {
        return diary.keySet().stream()
                .filter(date -> date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == weekNumber)
                .flatMap(date -> diary.get(date).getDailyCardioExercises().stream())
                .collect(Collectors.toList());
    }

    public WeeklyCardioSummary getWeeklyCardioSummary(int weekNumber) {
        List<CardioExercise> weeklyCardio = getCardioExercisesByWeekNumber(weekNumber);

        int burnedCalories = weeklyCardio.stream()
                .mapToInt(CardioExercise::burnedCalories)
                .sum();

        int totalMinutes = weeklyCardio.stream()
                .mapToInt(CardioExercise::durationInMinutes)
                .sum();

        return new WeeklyCardioSummary(weekNumber, burnedCalories, totalMinutes);
    }

    public WeeklyCardioSummary getWeeklyCardioSummary(LocalDate date) {
        int weekNumber = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return getWeeklyCardioSummary(weekNumber);
    }

    private DailyExerciseDiary getOrCreateDailyDiary(LocalDate date) {
        return this.diary.computeIfAbsent(date, d -> new DailyExerciseDiary(exercisePool));
    }

    private DailyExerciseDiary getValidatedDailyDiary(LocalDate date) {
        validateDate(date);
        return this.diary.get(date);
    }

    private void validateDate(LocalDate exerciseDate) {
        if (!diary.containsKey(exerciseDate)) {
            throw new IllegalArgumentException(GlobalConstants.NOT_EXISTING_DATE_IN_EXERCISE_DIARY_MESSAGE);
        }
    }

    private boolean isDiaryEmptyOn(LocalDate date) {
        return !diary.containsKey(date);
    }
}
