package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exception.UnknownExerciseException;
import com.fmi.myfitnesspal.exercise.ExerciseDiary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public final class LogExerciseCommandTest {
    ExerciseDiary diaryMock = mock(ExerciseDiary.class);
    ExecutableCommand command = new LogExerciseCommand(diaryMock);
    @Test
    void testExecuteValidCommand() throws InvalidCommandException {
        String message = command.execute(List.of("12.12.2012", "ex1"));
        assertEquals("ex1 was logged successfully!", message);
    }

    @Test
    void testExecuteLessArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("arg1")),
            "The method should throw InvalidCommandException when the count of the arguments is not 2");
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class,
            () -> command.execute(List.of("arg1", "arg2", "arg3")),
            "The method should throw InvalidCommandException when the count of the arguments is not 2");
    }

    @Test
    void testExecuteInvalidDate() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("invalid-date", "ex1")),
            "The method should throw InvalidCommandException when the date is not in valid format");
    }

    @Test
    void testExecuteLogNonexistentExercise() throws InvalidCommandException, UnknownExerciseException {
        doThrow(new UnknownExerciseException("")).when(diaryMock).logExercise(any(), eq("ex3"));
        assertEquals(GlobalConstants.NOT_EXISTING_EXERCISE_MESSAGE, command.execute(List.of("12.12.2012", "ex3")));
    }
}
