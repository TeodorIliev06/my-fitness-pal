package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.CardioExercise;
import com.fmi.myfitnesspal.exercise.Exercise;
import com.fmi.myfitnesspal.exercise.ExercisePool;
import com.fmi.myfitnesspal.exercise.StrengthExercise;
import com.fmi.myfitnesspal.exercise.Workout;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShowWorkoutsCommandTest {
    static ExercisePool poolMock = mock(ExercisePool.class);
    static Map<String, Workout> exercises = new HashMap<>();
    static ExecutableCommand command;
    private static final String WORKOUT_NAME = "workout";
    static Exercise cardioEx = new CardioExercise("cardio_ex", 10, 15, LocalTime.now());
    static Exercise strengthEx = new StrengthExercise("strength_ex", 3, 8, 50, 20);
        static Workout workout = new Workout(WORKOUT_NAME, List.of(cardioEx, strengthEx));

    @BeforeAll
    static void initialize() {
        exercises.put(WORKOUT_NAME, workout);
        when(poolMock.getWorkouts()).thenReturn(exercises);
        command = new ShowWorkoutsCommand(poolMock);
    }

    @Test
    void testExecuteValidCommand() throws InvalidCommandException {
        String expected = workout.toString();
        String actual = command.execute(List.of());
        assertEquals(expected, actual);
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("arg1")),
            "The method should throw InvalidCommandException when the count of the arguments is not 0");
    }
}
