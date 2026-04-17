package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.ExerciseDiary;
import com.fmi.myfitnesspal.exercise.WeeklyCardioSummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private static final String TARGET_WEEK_STR = String.valueOf(TARGET_WEEK);
    private static final String DATE_IN_TARGET_WEEK = "08.01.2024";

    private static final WeeklyCardioSummary EMPTY_SUMMARY =
            new WeeklyCardioSummary(TARGET_WEEK, 0, 0);

    @Mock
    private ExerciseDiary diaryMock;

    @InjectMocks
    private ShowWeeklyCardioCommand command;

    @Test
    void testExecuteEmptyWeekByWeekNumber() throws InvalidCommandException {
        stubDiaryReturnsEmptySummary();

        String result = command.execute(List.of(TARGET_WEEK_STR));

        assertEquals("Week 2: burned 0 calories, time spent 0 min", result,
                "An empty week should report 0 calories and 0 min");
    }

    @Test
    void testExecuteEmptyWeekByDate() throws InvalidCommandException {
        stubDiaryReturnsEmptySummary();

        String result = command.execute(List.of(DATE_IN_TARGET_WEEK));

        assertEquals("Week 2: burned 0 calories, time spent 0 min", result,
                "Passing a date should resolve to the same week number as passing the number directly");
    }

    @Test
    void testExecuteWithDurationUnderOneHour() throws InvalidCommandException {
        when(diaryMock.getWeeklyCardioSummary(TARGET_WEEK))
                .thenReturn(new WeeklyCardioSummary(TARGET_WEEK, 300, 30));

        String result = command.execute(List.of(TARGET_WEEK_STR));

        assertEquals("Week 2: burned 300 calories, time spent 30 min", result,
                "Duration under 60 minutes should be displayed as minutes only");
    }

    @Test
    void testExecuteWithDurationExactlyOneHour() throws InvalidCommandException {
        when(diaryMock.getWeeklyCardioSummary(TARGET_WEEK))
                .thenReturn(new WeeklyCardioSummary(TARGET_WEEK, 400, 60));

        String result = command.execute(List.of(TARGET_WEEK_STR));

        assertEquals("Week 2: burned 400 calories, time spent 1 h 0 min", result,
                "A duration of exactly 60 minutes should trigger the hours format, not the minutes-only format");
    }

    @Test
    void testExecuteWithDurationOverOneHour() throws InvalidCommandException {
        when(diaryMock.getWeeklyCardioSummary(TARGET_WEEK))
                .thenReturn(new WeeklyCardioSummary(TARGET_WEEK, 600, 90));

        String result = command.execute(List.of(TARGET_WEEK_STR));

        assertEquals("Week 2: burned 600 calories, time spent 1 h 30 min", result,
                "Duration of 60 or more minutes should be formatted as hours and minutes");
    }

    @Test
    void testExecuteDelegatesToDiary() throws InvalidCommandException {
        stubDiaryReturnsEmptySummary();

        command.execute(List.of(TARGET_WEEK_STR));

        verify(diaryMock).getWeeklyCardioSummary(TARGET_WEEK);
    }

    @Test
    void testExecuteLessArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of()),
                "The method should throw InvalidCommandException when the count of the arguments is not 1");
        verify(diaryMock, never()).getWeeklyCardioSummary(anyInt());
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_WEEK_STR, DATE_IN_TARGET_WEEK)),
                "The method should throw InvalidCommandException when the count of the arguments is not 1");
        verify(diaryMock, never()).getWeeklyCardioSummary(anyInt());
    }

    @Test
    void testExecuteInvalidDate() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("not-a-date")),
                "The method should throw InvalidCommandException when the date is not in valid format");
        verify(diaryMock, never()).getWeeklyCardioSummary(anyInt());
    }

    @Test
    void testExecuteWeekNumberBelowMinIsInvalid() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("0")),
                "The method should throw InvalidCommandException for week number below 1");
        verify(diaryMock, never()).getWeeklyCardioSummary(anyInt());
    }

    @Test
    void testExecuteWeekNumberAboveMaxIsInvalid() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("54")),
                "The method should throw InvalidCommandException for week number above 53");
        verify(diaryMock, never()).getWeeklyCardioSummary(anyInt());
    }

    private void stubDiaryReturnsEmptySummary() {
        when(diaryMock.getWeeklyCardioSummary(TARGET_WEEK)).thenReturn(EMPTY_SUMMARY);
    }
}
