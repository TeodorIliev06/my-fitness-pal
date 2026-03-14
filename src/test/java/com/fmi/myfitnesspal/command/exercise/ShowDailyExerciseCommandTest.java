package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.CardioExercise;
import com.fmi.myfitnesspal.exercise.ExerciseDiary;
import com.fmi.myfitnesspal.exercise.StrengthExercise;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ShowDailyExerciseCommandTest {

    private static final String CARDIO_NAME = "run";
    private static final String STRENGTH_NAME = "squat";
    private static final String DATE = "08.01.2024";
    private static final LocalDate LOCAL_DATE = LocalDate.of(2024, 1, 8);

    private static final CardioExercise CARDIO = new CardioExercise(CARDIO_NAME, 30, 300, LocalTime.of(7, 0));
    private static final StrengthExercise STRENGTH = new StrengthExercise(STRENGTH_NAME, 4, 200, 10, 60);

    @Mock
    private ExerciseDiary diaryMock;

    @InjectMocks
    private ShowDailyExerciseCommand command;

    @Test
    void testExecuteEmptyDay() throws InvalidCommandException {
        when(diaryMock.getDailyExercise(LOCAL_DATE)).thenReturn(List.of());

        String result = command.execute(List.of(DATE));
        assertTrue(result.isEmpty(),
                "An empty day should return an empty string");
        verify(diaryMock).getDailyExercise(LOCAL_DATE);
    }

    @Test
    void testExecuteValidCommand() throws InvalidCommandException {
        when(diaryMock.getDailyExercise(LOCAL_DATE)).thenReturn(List.of(CARDIO));

        String result = command.execute(List.of(DATE));
        assertEquals(CARDIO.toString(), result);
        verify(diaryMock).getDailyExercise(LOCAL_DATE);
    }

    @Test
    void testExecuteMultipleExercises() throws InvalidCommandException {
        when(diaryMock.getDailyExercise(LOCAL_DATE)).thenReturn(List.of(CARDIO, STRENGTH));

        String result = command.execute(List.of(DATE));
        assertTrue(result.contains(CARDIO.toString()));
        assertTrue(result.contains(STRENGTH.toString()));
        verify(diaryMock).getDailyExercise(LOCAL_DATE);
    }

    @Test
    void testExecuteLessArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of()),
                "The method should throw InvalidCommandException when the count of the arguments is not 1");
        verify(diaryMock, never()).getDailyExercise(any());
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of(DATE, DATE)),
                "The method should throw InvalidCommandException when the count of the arguments is not 1");
        verify(diaryMock, never()).getDailyExercise(any());
    }

    @Test
    void testExecuteInvalidDate() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("not-a-date")),
                "The method should throw InvalidCommandException when the date is not in valid format");
        verify(diaryMock, never()).getDailyExercise(any());
    }
}
