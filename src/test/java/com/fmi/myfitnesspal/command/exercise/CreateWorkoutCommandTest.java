package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exception.UnknownExerciseException;
import com.fmi.myfitnesspal.exercise.CardioExercise;
import com.fmi.myfitnesspal.exercise.ExercisePool;
import com.fmi.myfitnesspal.exercise.StrengthExercise;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class CreateWorkoutCommandTest {

    private static final String CARDIO_NAME = "run";
    private static final String STRENGTH_NAME = "squat";
    private static final String WORKOUT_NAME = "myWorkout";
    private static final String UNKNOWN_NAME = "unknownExercise";

    private static final CardioExercise CARDIO = new CardioExercise(CARDIO_NAME, 30, 300, LocalTime.of(7, 0));
    private static final StrengthExercise STRENGTH = new StrengthExercise(STRENGTH_NAME, 4, 200, 10, 60);

    @Mock
    private ExercisePool poolMock;

    @InjectMocks
    private CreateWorkoutCommand command;

    @Test
    void testExecuteValidCommandReturnsMessage() throws InvalidCommandException, UnknownExerciseException {
        when(poolMock.getExerciseByName(CARDIO_NAME)).thenReturn(CARDIO);
        when(poolMock.getExerciseByName(STRENGTH_NAME)).thenReturn(STRENGTH);

        String message = command.execute(List.of(WORKOUT_NAME, CARDIO_NAME, STRENGTH_NAME));
        assertEquals(WORKOUT_NAME + " was created successfully!", message);
    }

    @Test
    void testExecuteValidCommandAddsToPool() throws InvalidCommandException, UnknownExerciseException {
        when(poolMock.getExerciseByName(CARDIO_NAME)).thenReturn(CARDIO);
        when(poolMock.getExerciseByName(STRENGTH_NAME)).thenReturn(STRENGTH);

        command.execute(List.of(WORKOUT_NAME, CARDIO_NAME, STRENGTH_NAME));
        verify(poolMock).createExercise(any());
    }

    @Test
    void testExecuteLessArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of(WORKOUT_NAME)),
                "The method should throw InvalidCommandException when a workout name and no exercises are provided");
        verify(poolMock, never()).createExercise(any());
    }

    @Test
    void testExecuteNoArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of()),
                "The method should throw InvalidCommandException when no arguments are provided");
        verify(poolMock, never()).createExercise(any());
    }

    @Test
    void testExecuteUnknownExercise() throws InvalidCommandException, UnknownExerciseException {
        when(poolMock.getExerciseByName(UNKNOWN_NAME)).thenThrow(UnknownExerciseException.class);

        String message = command.execute(List.of(WORKOUT_NAME, UNKNOWN_NAME));
        assertEquals(GlobalConstants.NOT_EXISTING_EXERCISE_MESSAGE, message);
        verify(poolMock, never()).createExercise(any());
    }
}
