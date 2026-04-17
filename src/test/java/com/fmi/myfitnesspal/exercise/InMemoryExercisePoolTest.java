package com.fmi.myfitnesspal.exercise;

import com.fmi.myfitnesspal.exception.UnknownExerciseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class InMemoryExercisePoolTest {

    private static final String NON_EXISTENT_EXERCISE_NAME = "invalid-exercise-name";

    private static final CardioExercise FAST_CYCLING = new CardioExercise("Fast Cycling", 10, 10, LocalTime.of(0, 30));
    private static final StrengthExercise HEAVY_SQUATS = new StrengthExercise("Heavy Squats", 10, 10, 3, 12);
    private static final Workout MONDAY_ROUTINE = new Workout("Monday Routine", List.of(FAST_CYCLING));

    private InMemoryExercisePool exerciseRegistry;

    @BeforeEach
    void setUp() {
        exerciseRegistry = new InMemoryExercisePool();
    }

    @Test
    void testCreateExerciseStoresMultipleExerciseTypes() {
        exerciseRegistry.createExercise(FAST_CYCLING);
        exerciseRegistry.createExercise(HEAVY_SQUATS);

        Map<String, Exercise> allExercises = exerciseRegistry.getExercises();

        assertEquals(2, allExercises.size(), "Registry should contain exactly two exercises");
        assertTrue(allExercises.containsKey(FAST_CYCLING.name()), "Registry should contain cardio exercise");
        assertTrue(allExercises.containsKey(HEAVY_SQUATS.name()), "Registry should contain strength exercise");
    }

    @Test
    void testGetCardioExercisesFiltersOnlyCardioType() {
        exerciseRegistry.createExercise(FAST_CYCLING);
        exerciseRegistry.createExercise(HEAVY_SQUATS);

        Map<String, CardioExercise> cardioResults = exerciseRegistry.getCardioExercises();

        assertEquals(1, cardioResults.size(), "Only one cardio exercise should be returned");
        assertTrue(cardioResults.containsKey(FAST_CYCLING.name()),
                "The returned exercise must be the cycling instance");
    }

    @Test
    void testGetWorkoutsReturnsOnlyWorkoutInstances() {
        exerciseRegistry.createExercise(FAST_CYCLING);
        exerciseRegistry.createExercise(MONDAY_ROUTINE);

        Map<String, Workout> workoutResults = exerciseRegistry.getWorkouts();

        assertEquals(1, workoutResults.size(),
                "Registry should filter and return exactly one workout");
        assertTrue(workoutResults.containsKey(MONDAY_ROUTINE.name()));
    }

    @Test
    void testGetWorkoutsReturnsEmptyMapWhenNoWorkoutsExist() {
        exerciseRegistry.createExercise(FAST_CYCLING);
        exerciseRegistry.createExercise(HEAVY_SQUATS);

        assertTrue(exerciseRegistry.getWorkouts().isEmpty(),
                "Should return an empty collection rather than null when no workouts are present");
    }

    @Test
    void testGetExerciseByNameReturnsCorrectInstance() throws UnknownExerciseException {
        exerciseRegistry.createExercise(FAST_CYCLING);

        Exercise retrieved = exerciseRegistry.getExerciseByName(FAST_CYCLING.name());

        assertEquals(FAST_CYCLING, retrieved, "Retrieved exercise must match the one created");
    }

    @Test
    void testExecuteGetExerciseByNameWithInvalidName() {
        assertThrows(UnknownExerciseException.class,
                () -> exerciseRegistry.getExerciseByName(NON_EXISTENT_EXERCISE_NAME),
                "Querying a name not in the registry must throw UnknownExerciseException");
    }
}
