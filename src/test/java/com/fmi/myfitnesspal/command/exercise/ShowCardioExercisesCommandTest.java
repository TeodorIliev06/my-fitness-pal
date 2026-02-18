package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.CardioExercise;
import com.fmi.myfitnesspal.exercise.ExercisePool;
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
public class ShowCardioExercisesCommandTest {
    static ExercisePool poolMock = mock(ExercisePool.class);
    static Map<String, CardioExercise> exercises = new HashMap<>();
    static ExecutableCommand command;
    static CardioExercise cardioEx = new CardioExercise("cardio_ex", 10, 15, LocalTime.now());

    @BeforeAll
    static void initialize() {
        exercises.put("cardio_ex", cardioEx);
        when(poolMock.getCardioExercises()).thenReturn(exercises);
        command = new ShowCardioExercisesCommand(poolMock);
    }

    @Test
    void testExecuteValidCommand() throws InvalidCommandException {
        assertEquals(cardioEx.toString(), command.execute(List.of()));
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("arg1")),
            "The method should throw InvalidCommandException when the count of the arguments is not 0");
    }
}
