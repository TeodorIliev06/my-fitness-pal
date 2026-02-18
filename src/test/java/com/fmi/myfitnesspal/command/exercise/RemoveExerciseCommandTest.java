package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.command.utility.CommandUtilities;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exception.UnknownExerciseException;
import com.fmi.myfitnesspal.exercise.CardioExercise;
import com.fmi.myfitnesspal.exercise.Exercise;
import com.fmi.myfitnesspal.exercise.ExerciseDiary;
import com.fmi.myfitnesspal.exercise.ExercisePool;
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
public final class RemoveExerciseCommandTest {
    ExercisePool poolMock = mock(ExercisePool.class);
    ExerciseDiary diary = new ExerciseDiary(poolMock);
    ExecutableCommand command = new RemoveExerciseCommand(diary);
    CardioExercise ex1 = new CardioExercise("ex1", 1, 1, LocalTime.now());

    @Test
    void testExecuteValidCommand() throws InvalidCommandException, UnknownExerciseException {
        Map<String, Exercise> exercises = new HashMap<>();
        exercises.put("ex1", ex1);
        when(poolMock.getExercises()).thenReturn(exercises);
        when(poolMock.getExerciseByName("ex1")).thenReturn(ex1);

        diary.logExercise(CommandUtilities.parseDate("12.12.2012"), "ex1");
        String message = command.execute(List.of("12.12.2012", "ex1"));
        assertEquals("ex1 was removed successfully!", message);
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
    void testExecuteRemoveNonexistentExercise() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("12.12.2012", "3ex3")),
            "The method should throw InvalidCommandException when the exercise does not exist");
    }
}
