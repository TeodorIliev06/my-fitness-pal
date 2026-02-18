package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.ExercisePool;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class CreateCardioExerciseCommandTest {
    ExercisePool pool = new ExercisePool();
    ExecutableCommand command = new CreateCardioExerciseCommand(pool);

    @Test
    void testExecuteValidCommand() throws InvalidCommandException {
        String message = command.execute(List.of("new", "10", "100", "12:00"));
        assertTrue(pool.getExercises().containsKey("new"));
        assertEquals("new was created successfully!", message);
    }

    @Test
    void testExecuteLessArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("arg1")),
            "The method should throw InvalidCommandException when the count of the arguments is not 4");
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class,
            () -> command.execute(List.of("arg1", "arg2", "arg3", "arg4", "arg5")),
            "The method should throw InvalidCommandException when the count of the arguments is not 4");
    }

    @Test
    void testExecuteInvalidArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("inv", "-17", "0", "12:00")),
            "The method should throw InvalidCommandException when the duration is not in valid format");
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("inv", "17", "-1", "12:00")),
            "The method should throw InvalidCommandException when the calories are not in valid format");
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("inv", "17", "0", "00")),
            "The method should throw InvalidCommandException when the time is not in valid format");
    }

}
