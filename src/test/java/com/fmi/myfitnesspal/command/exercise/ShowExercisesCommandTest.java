package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.CardioExercise;
import com.fmi.myfitnesspal.exercise.Exercise;
import com.fmi.myfitnesspal.exercise.ExercisePool;
import com.fmi.myfitnesspal.exercise.StrengthExercise;
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
public class ShowExercisesCommandTest {
    static ExercisePool poolMock = mock(ExercisePool.class);
    static Map<String, Exercise> exercises = new HashMap<>();
    static ExecutableCommand command;

    private static final String CARDIO_NAME = "cardio_ex";
    private static final String STRENGTH_NAME = "strength_ex";
    static Exercise cardioEx = new CardioExercise(CARDIO_NAME, 10, 15, LocalTime.now());
    static Exercise strengthEx = new StrengthExercise(STRENGTH_NAME, 3, 8, 50, 20);

    @BeforeAll
    static void initialize() {
        exercises.put(CARDIO_NAME, cardioEx);
        exercises.put(STRENGTH_NAME, strengthEx);

        when(poolMock.getExercises()).thenReturn(exercises);
        command = new ShowExercisesCommand(poolMock);
    }

    @Test
    void testExecuteValidCommand() throws InvalidCommandException {
        String expected = cardioEx.toString() + System.lineSeparator() + strengthEx.toString();
        String actual = command.execute(List.of());
        assertEquals(expected, actual);
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("arg1")),
            "The method should throw InvalidCommandException when the count of the arguments is not 0");
    }

}
