package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.ExercisePool;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public final class CreateStrengthExerciseCommandTest {
    ExercisePool pool = new ExercisePool();
    ExecutableCommand command = new CreateStrengthExerciseCommand(pool);

    @ParameterizedTest
    @MethodSource("provideArgsForInvalidArguments")
    void testExecuteInvalidArguments(List<String> input, String message) {
        assertThrows(InvalidCommandException.class, () -> command.execute(input), message);
    }

    private static Stream<Arguments> provideArgsForInvalidArguments() {
        return Stream.of(
            Arguments.of(List.of("arg1"),
                "The method should throw InvalidCommandException when the count of the arguments is not 5"),
            Arguments.of(List.of("arg1", "arg2", "arg3", "arg4", "arg5", "arg6"),
                "The method should throw InvalidCommandException when the count of the arguments is not 5"),
            Arguments.of(List.of("inv", "-17", "0", "10", "0"),
                "The method should throw InvalidCommandException when the sets are not in valid format"),
            Arguments.of(List.of("inv", "17", "-1", "12:00"),
                "The method should throw InvalidCommandException when the reps are not in valid format"),
            Arguments.of(List.of("inv", "17", "0", "-10", "0"),
                "The method should throw InvalidCommandException when the weight is not in valid format"),
            Arguments.of(List.of("inv", "17", "0", "0", "-20"),
                "The method should throw InvalidCommandException when the calories is not in valid format"));
    }

    @Test
    void testExecuteValidCommand() throws InvalidCommandException {
        String message = command.execute(List.of("new", "10", "3", "10", "100"));
        assertTrue(pool.getExercises().containsKey("new"));
        assertEquals("new was created successfully!", message);
    }
}
