package com.fmi.myfitnesspal.exercise;

import com.fmi.myfitnesspal.exception.UnknownExerciseException;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WorkoutTest {
    private static final CardioExercise CARDIO1 = new CardioExercise("FstC", 10, 10, LocalTime.now());
    private static final StrengthExercise STRENGTH1 = new StrengthExercise("Fst", 10, 10, 0, 15);
    private static final CardioExercise CARDIO2 = new CardioExercise("Snd", 10, 10, LocalTime.now());

    private final List<Exercise> exercises = new ArrayList<>(List.of(CARDIO1, STRENGTH1));
    private Workout testWorkout = new Workout("MyWorkout", exercises);

    @Test
    void testAddExercise() {
        int oldCalories = testWorkout.burnedCalories();
        int newCalories = oldCalories + CARDIO2.burnedCalories();
        assertTrue(testWorkout.addExercise(CARDIO2));
        assertEquals(newCalories, testWorkout.burnedCalories());
    }

    @Test
    void testRemoveExercise() throws UnknownExerciseException {
        int oldCalories = testWorkout.burnedCalories();
        int newCalories = oldCalories - STRENGTH1.burnedCalories();

        testWorkout.removeExercise(STRENGTH1.name());
        assertEquals(newCalories, testWorkout.burnedCalories());
    }

    @Test
    void testRemoveExerciseThrowUnknownExercise() {
        assertThrows(UnknownExerciseException.class, () -> testWorkout.removeExercise("UNKNOWN"));
    }

    @Test
    void getTotalCalories() {
        assertEquals(25, testWorkout.burnedCalories());
    }
}

