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

import static com.fmi.myfitnesspal.utility.DateHelper.MIN_SUPPORTED_WEEK_NUMBER;
import static com.fmi.myfitnesspal.utility.DateHelper.MAX_SUPPORTED_WEEK_NUMBER;
import static com.fmi.myfitnesspal.utility.DateHelper.MIN_SUPPORTED_YEAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ShowWeeklyCardioCommandTest {

    private static final int TARGET_WEEK = 2;
    private static final int TARGET_YEAR = 2024;
    private static final String TARGET_WEEK_STR = String.valueOf(TARGET_WEEK);
    private static final String TARGET_YEAR_STR = String.valueOf(TARGET_YEAR);
    private static final String DATE_IN_TARGET_WEEK = "08.01.2024";

    private static final WeeklyCardioSummary EMPTY_SUMMARY =
            new WeeklyCardioSummary(TARGET_WEEK, TARGET_YEAR, 0, 0);

    @Mock
    private ExerciseDiary diaryMock;

    @InjectMocks
    private ShowWeeklyCardioCommand command;

    @Test
    void testExecuteEmptyWeekByWeekNumber() throws InvalidCommandException {
        stubDiaryReturnsEmptySummary();

        String result = command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        assertEquals("Week 2: burned 0 calories, time spent 0 min", result,
                "An empty week should report 0 calories and 0 min");
    }

    @Test
    void testExecuteEmptyWeekByDate() throws InvalidCommandException {
        stubDiaryReturnsEmptySummary();

        String result = command.execute(List.of(DATE_IN_TARGET_WEEK));

        assertEquals("Week 2: burned 0 calories, time spent 0 min", result,
                "Passing a date should resolve to the same week and year as passing them directly");
    }

    @Test
    void testExecuteWithDurationUnderOneHour() throws InvalidCommandException {
        when(diaryMock.getWeeklyCardioSummary(TARGET_WEEK, TARGET_YEAR))
                .thenReturn(new WeeklyCardioSummary(TARGET_WEEK, TARGET_YEAR, 300, 30));

        String result = command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        assertEquals("Week 2: burned 300 calories, time spent 30 min", result,
                "Duration under 60 minutes should be displayed as minutes only");
    }

    @Test
    void testExecuteWithDurationExactlyOneHour() throws InvalidCommandException {
        when(diaryMock.getWeeklyCardioSummary(TARGET_WEEK, TARGET_YEAR))
                .thenReturn(new WeeklyCardioSummary(TARGET_WEEK, TARGET_YEAR, 400, 60));

        String result = command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        assertEquals("Week 2: burned 400 calories, time spent 1 h 0 min", result,
                "A duration of exactly 60 minutes should trigger the hours format");
    }

    @Test
    void testExecuteWithDurationOverOneHour() throws InvalidCommandException {
        when(diaryMock.getWeeklyCardioSummary(TARGET_WEEK, TARGET_YEAR))
                .thenReturn(new WeeklyCardioSummary(TARGET_WEEK, TARGET_YEAR, 600, 90));

        String result = command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        assertEquals("Week 2: burned 600 calories, time spent 1 h 30 min", result,
                "Duration of 60 or more minutes should be formatted as hours and minutes");
    }

    @Test
    void testExecuteDelegatesToDiary() throws InvalidCommandException {
        stubDiaryReturnsEmptySummary();

        command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        verify(diaryMock).getWeeklyCardioSummary(TARGET_WEEK, TARGET_YEAR);
    }

    @Test
    void testExecuteLessArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of()),
                "The method should throw InvalidCommandException when no arguments are provided");
        verify(diaryMock, never()).getWeeklyCardioSummary(anyInt(), anyInt());
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR, DATE_IN_TARGET_WEEK)),
                "The method should throw InvalidCommandException when more than 2 arguments are provided");
        verify(diaryMock, never()).getWeeklyCardioSummary(anyInt(), anyInt());
    }

    @Test
    void testExecuteInvalidDate() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("not-a-date")),
                "The method should throw InvalidCommandException when the date is not in valid format");
        verify(diaryMock, never()).getWeeklyCardioSummary(anyInt(), anyInt());
    }

    @Test
    void testExecuteWeekNumberWithoutYearIsRejected() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of(TARGET_WEEK_STR)),
                "A week number without a year should be rejected");
        verify(diaryMock, never()).getWeeklyCardioSummary(anyInt(), anyInt());
    }

    @Test
    void testExecuteWeekNumberBelowMinIsInvalid() {
        assertThrows(InvalidCommandException.class, () -> command.execute(
                List.of(String.valueOf(MIN_SUPPORTED_WEEK_NUMBER - 1), TARGET_YEAR_STR)),
                "The method should throw InvalidCommandException for week number below the supported minimum");
        verify(diaryMock, never()).getWeeklyCardioSummary(anyInt(), anyInt());
    }

    @Test
    void testExecuteWeekNumberAboveMaxIsInvalid() {
        assertThrows(InvalidCommandException.class, () -> command.execute(
                List.of(String.valueOf(MAX_SUPPORTED_WEEK_NUMBER + 1), TARGET_YEAR_STR)),
                "The method should throw InvalidCommandException for week number above the supported maximum");
        verify(diaryMock, never()).getWeeklyCardioSummary(anyInt(), anyInt());
    }

    @Test
    void testExecuteYearBelowMinIsInvalid() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of(TARGET_WEEK_STR,
                        String.valueOf(MIN_SUPPORTED_YEAR - 1))),
                "The method should throw InvalidCommandException for a year below the supported minimum");
        verify(diaryMock, never()).getWeeklyCardioSummary(anyInt(), anyInt());
    }

    private void stubDiaryReturnsEmptySummary() {
        when(diaryMock.getWeeklyCardioSummary(TARGET_WEEK, TARGET_YEAR)).thenReturn(EMPTY_SUMMARY);
    }
}
