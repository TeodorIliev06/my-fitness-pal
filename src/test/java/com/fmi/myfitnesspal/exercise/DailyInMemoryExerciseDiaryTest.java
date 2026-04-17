package com.fmi.myfitnesspal.exercise;

import com.fmi.myfitnesspal.exception.UnknownExerciseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DailyInMemoryExerciseDiaryTest {
    static InMemoryExercisePool testPool = mock(InMemoryExercisePool.class);
    DailyExerciseDiary testDiary = new DailyExerciseDiary(testPool);
    private static final String CARDIO_NAME = "FstC";
    private static final String STRENGTH_NAME = "Fst";
    private static final CardioExercise UNKNOWN_EXERCISE = new CardioExercise("", 10, 10, LocalTime.now());
    private static final CardioExercise CARDIO1 = new CardioExercise(CARDIO_NAME, 10, 10, LocalTime.now());
    private static final StrengthExercise STRENGTH1 = new StrengthExercise(STRENGTH_NAME, 10, 10, 0, 0);

    @BeforeAll
    static void setUpExercisePool() throws UnknownExerciseException {
        Map<String, Exercise> exercises = new HashMap<>();
        exercises.put(CARDIO1.name(), CARDIO1);
        exercises.put(STRENGTH1.name(), STRENGTH1);
        when(testPool.getExercises()).thenReturn(exercises);
        when(testPool.getExerciseByName(CARDIO_NAME)).thenReturn(CARDIO1);
        when(testPool.getExerciseByName(STRENGTH_NAME)).thenReturn(STRENGTH1);
        when(testPool.getExerciseByName("")).thenThrow(UnknownExerciseException.class);
    }

    @Test
    void testLogExercise() throws UnknownExerciseException {
        Map<String, Exercise> testMap = new HashMap<>();
        testMap.put(CARDIO1.name(), CARDIO1);
        testMap.put(STRENGTH1.name(), STRENGTH1);

        testDiary.logExercise(CARDIO_NAME);
        testDiary.logExercise(STRENGTH_NAME);

        assertEquals(testDiary.getDailyExercise().size(), testMap.values().size());
        assertTrue(testDiary.getDailyExercise().containsAll(testMap.values()));
        assertTrue(testMap.values().containsAll(testDiary.getDailyExercise()));
    }

    @Test
    void testLogExerciseThrowUnknownExercise() {
        assertThrows(UnknownExerciseException.class, () -> testDiary.logExercise(""),
            "Exception was not thrown when logging an unknown cardio exercise");
    }

    @Test
    public void testRemoveExercise() throws UnknownExerciseException {
        testDiary.logExercise(CARDIO_NAME);
        testDiary.removeExercise(CARDIO_NAME);
        assertEquals(testDiary.getDailyExercise(), List.of());
    }

    @Test
    public void testRemoveExerciseThrowUnknownExercise() {
        assertThrows(UnknownExerciseException.class, () -> testDiary.removeExercise(UNKNOWN_EXERCISE.name()));
    }

    @Test
    void testGetBurnedDailyCalories() throws UnknownExerciseException {
        testDiary.logExercise(CARDIO_NAME);
        testDiary.logExercise(STRENGTH_NAME);
        int expectedCalories = CARDIO1.burnedCalories() + STRENGTH1.burnedCalories();
        assertEquals(expectedCalories, testDiary.getBurnedDailyCalories());
        testDiary.removeExercise(CARDIO1.name());
        assertEquals(STRENGTH1.burnedCalories(), testDiary.getBurnedDailyCalories());
    }
}
