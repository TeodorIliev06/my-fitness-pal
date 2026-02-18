package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.ExercisePool;
import com.fmi.myfitnesspal.exercise.StrengthExercise;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShowStrengthExercisesCommandTest {
    static ExercisePool poolMock = mock(ExercisePool.class);
    static Map<String, StrengthExercise> exercises = new HashMap<>();
    static ExecutableCommand command;
    static StrengthExercise strengthEx = new StrengthExercise("strength_ex", 3, 8, 50, 20);

    @BeforeAll
    static void initialize() {
        exercises.put("strength_ex", strengthEx);
        when(poolMock.getStrengthExercises()).thenReturn(exercises);
        command = new ShowStrengthExercisesCommand(poolMock);
    }

    @Test
    void testExecuteValidCommand() throws InvalidCommandException {
        String expected = strengthEx.toString();
        String actual = command.execute(List.of());
        assertEquals(expected, actual);
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("arg1")),
            "The method should throw InvalidCommandException when the count of the arguments is not 0");
    }
}
