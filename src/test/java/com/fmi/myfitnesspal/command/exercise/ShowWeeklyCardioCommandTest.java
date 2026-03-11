package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.CardioExercise;
import com.fmi.myfitnesspal.exercise.ExerciseDiary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ShowWeeklyCardioCommandTest {

    private static final int TARGET_WEEK = 2;
    private static final String DATE_IN_TARGET_WEEK = "08.01.2024";
    private static final String TARGET_WEEK_STR = String.valueOf(TARGET_WEEK);

    private static final String RUN_EXERCISE_NAME = "run";
    private static final String BIKE_EXERCISE_NAME = "bike";

    private static final CardioExercise SHORT_CARDIO =
            new CardioExercise(RUN_EXERCISE_NAME, 30, 300, LocalTime.of(7, 0));
    private static final CardioExercise LONG_CARDIO =
            new CardioExercise(BIKE_EXERCISE_NAME, 90, 600, LocalTime.of(9, 0));

    @Mock
    private ExerciseDiary diaryMock;

    @InjectMocks
    private ShowWeeklyCardioCommand command;

    @Test
    void testExecuteEmptyWeekByWeekNumber() throws InvalidCommandException {
        when(diaryMock.getCardioExercisesByWeekNumber(TARGET_WEEK)).thenReturn(List.of());

        String result = command.execute(List.of(TARGET_WEEK_STR));
        assertEquals("Week 2: burned 0 calories, time spent 0 min", result,
                "An empty week should report 0 calories and 0 min");
        verify(diaryMock).getCardioExercisesByWeekNumber(TARGET_WEEK);
    }

    @Test
    void testExecuteEmptyWeekByDate() throws InvalidCommandException {
        when(diaryMock.getCardioExercisesByWeekNumber(TARGET_WEEK)).thenReturn(List.of());

        String result = command.execute(List.of(DATE_IN_TARGET_WEEK));
        assertEquals("Week 2: burned 0 calories, time spent 0 min", result,
                "Passing a date should resolve to the same week number as passing the number directly");
        verify(diaryMock).getCardioExercisesByWeekNumber(TARGET_WEEK);
    }

    @Test
    void testExecuteWithDurationUnderOneHour() throws InvalidCommandException {
        when(diaryMock.getCardioExercisesByWeekNumber(TARGET_WEEK)).thenReturn(List.of(SHORT_CARDIO));

        String result = command.execute(List.of(TARGET_WEEK_STR));
        assertEquals("Week 2: burned 300 calories, time spent 30 min", result,
                "Duration under 60 minutes should be displayed as minutes only");
        verify(diaryMock).getCardioExercisesByWeekNumber(TARGET_WEEK);
    }

    @Test
    void testExecuteWithDurationOverOneHour() throws InvalidCommandException {
        when(diaryMock.getCardioExercisesByWeekNumber(TARGET_WEEK)).thenReturn(List.of(LONG_CARDIO));

        String result = command.execute(List.of(TARGET_WEEK_STR));
        assertEquals("Week 2: burned 600 calories, time spent 1 h 30 min", result,
                "Duration of 60 or more minutes should be formatted as hours and minutes");
        verify(diaryMock).getCardioExercisesByWeekNumber(TARGET_WEEK);
    }

    @Test
    void testExecuteAccumulatesMultipleExercisesInSameWeek() throws InvalidCommandException {
        when(diaryMock.getCardioExercisesByWeekNumber(TARGET_WEEK)).thenReturn(List.of(SHORT_CARDIO, LONG_CARDIO));

        String result = command.execute(List.of(TARGET_WEEK_STR));
        assertEquals("Week 2: burned 900 calories, time spent 2 h 0 min", result,
                "Calories and minutes from multiple exercises in the same week should be summed together");
        verify(diaryMock).getCardioExercisesByWeekNumber(TARGET_WEEK);
    }

    @Test
    void testExecuteLessArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of()),
                "The method should throw InvalidCommandException when the count of the arguments is not 1");
        verify(diaryMock, never()).getCardioExercisesByWeekNumber(anyInt());
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_WEEK_STR, DATE_IN_TARGET_WEEK)),
                "The method should throw InvalidCommandException when the count of the arguments is not 1");
        verify(diaryMock, never()).getCardioExercisesByWeekNumber(anyInt());
    }

    @Test
    void testExecuteInvalidDate() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("not-a-date")),
                "The method should throw InvalidCommandException when the date is not in valid format");
        verify(diaryMock, never()).getCardioExercisesByWeekNumber(anyInt());
    }

    @Test
    void testExecuteWeekNumberBelowMinIsInvalid() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("0")),
                "The method should throw InvalidCommandException for week number below 1");
        verify(diaryMock, never()).getCardioExercisesByWeekNumber(anyInt());
    }

    @Test
    void testExecuteWeekNumberAboveMaxIsInvalid() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("54")),
                "The method should throw InvalidCommandException for week number above 53");
        verify(diaryMock, never()).getCardioExercisesByWeekNumber(anyInt());
    }
}
