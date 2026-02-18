package com.fmi.myfitnesspal.exercise;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExercisePoolTest {
    private ExercisePool testPool = new ExercisePool();
    static final CardioExercise CARDIO1 = new CardioExercise("FstC", 10, 10, LocalTime.now());
    static final StrengthExercise STRENGTH1 = new StrengthExercise("FstStr", 10, 10, 0, 0);

    @Test
    void testCreateExercise() {
        testPool.createExercise(CARDIO1);
        testPool.createExercise(STRENGTH1);

        assertEquals(2, testPool.getExercises().size());
        assertTrue(testPool.getExercises().containsKey(CARDIO1.name()));
        assertTrue(testPool.getExercises().containsKey(STRENGTH1.name()));
    }

    @Test
    void testGetCardioExercise() {
        testPool.createExercise(CARDIO1);
        testPool.createExercise(STRENGTH1);

        assertEquals(testPool.getCardioExercises().size(), 1);
        assertTrue(testPool.getCardioExercises().containsKey(CARDIO1.name()));
    }

    @Test
    void testGetStrengthExercise() {
        testPool.createExercise(CARDIO1);
        testPool.createExercise(STRENGTH1);

        Map<String, StrengthExercise> strengthExercises = testPool.getStrengthExercises();
        assertEquals(1, strengthExercises.size());
        assertTrue(strengthExercises.containsKey(STRENGTH1.name()));
    }

}
