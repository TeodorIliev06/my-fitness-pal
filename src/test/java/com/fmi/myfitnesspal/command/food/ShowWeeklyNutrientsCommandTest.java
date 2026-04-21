package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.utility.NutritionSliceMapper;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.WeeklyNutritionSummary;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.chart.PieChartDisplayer;
import com.fmi.myfitnesspal.chart.PieSlice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.fmi.myfitnesspal.utility.DateHelper.MIN_SUPPORTED_WEEK_NUMBER;
import static com.fmi.myfitnesspal.utility.DateHelper.MAX_SUPPORTED_WEEK_NUMBER;
import static com.fmi.myfitnesspal.utility.DateHelper.MIN_SUPPORTED_YEAR;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ShowWeeklyNutrientsCommandTest {

    private static final int TARGET_WEEK = 2;
    private static final int TARGET_YEAR = 2024;
    private static final String TARGET_WEEK_STR = String.valueOf(TARGET_WEEK);
    private static final String TARGET_YEAR_STR = String.valueOf(TARGET_YEAR);
    private static final String DATE_IN_TARGET_WEEK = "08.01.2024";

    private static final WeeklyNutritionSummary EMPTY_SUMMARY = new WeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR,
            0, Optional.empty(), Optional.empty(), Optional.empty());

    @Mock
    private FoodDiary foodDiaryMock;
    @Mock
    private PieChartDisplayer chartDisplayerMock;
    @Mock
    private NutritionSliceMapper sliceMapperMock;

    @Captor
    private ArgumentCaptor<String> titleCaptor;

    @InjectMocks
    private ShowWeeklyNutrientsCommand command;

    @Test
    void testExecuteByWeekNumberAndYearOpensChart() throws InvalidCommandException {
        stubDiaryForWeekYear();
        when(sliceMapperMock.fromNutritionSummary(any())).thenReturn(List.of());

        command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        verify(chartDisplayerMock).display(any(), any());
    }

    @Test
    void testExecuteByWeekNumberAndYearReturnsChartOpenedMessage() throws InvalidCommandException {
        stubDiaryForWeekYear();
        when(sliceMapperMock.fromNutritionSummary(any())).thenReturn(List.of());

        String result = command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        assertTrue(result.startsWith("Chart opened:"),
                "execute() should return a message starting with \"Chart opened:\"");
    }

    @Test
    void testExecuteByWeekNumberAndYearDelegatesToDiary() throws InvalidCommandException {
        stubDiaryForWeekYear();
        when(sliceMapperMock.fromNutritionSummary(any())).thenReturn(List.of());

        command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        verify(foodDiaryMock).getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR);
    }

    @Test
    void testExecuteByDateResolvesToCorrectWeekAndYear() throws InvalidCommandException {
        stubDiaryForDate();
        when(sliceMapperMock.fromNutritionSummary(any())).thenReturn(List.of());

        command.execute(List.of(DATE_IN_TARGET_WEEK));

        verify(foodDiaryMock).getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR);
    }

    @Test
    void testExecuteChartTitleContainsWeekNumber() throws InvalidCommandException {
        stubDiaryForWeekYear();
        when(sliceMapperMock.fromNutritionSummary(any())).thenReturn(List.of());

        command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        verify(chartDisplayerMock).display(titleCaptor.capture(), any());
        assertTrue(titleCaptor.getValue().contains(TARGET_WEEK_STR),
                "Chart title should contain the week number");
    }

    @Test
    void testExecutePassesWeeklySummaryToMapper() throws InvalidCommandException {
        stubDiaryForWeekYear();
        when(sliceMapperMock.fromNutritionSummary(any())).thenReturn(List.of());

        command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        verify(sliceMapperMock).fromNutritionSummary(EMPTY_SUMMARY);
    }

    @Test
    void testExecutePassesMapperSlicesToDisplayer() throws InvalidCommandException {
        List<PieSlice> expectedSlices = List.of(
                new PieSlice("Protein", 12.0),
                new PieSlice("Carbs", 60.0),
                new PieSlice("Fats", 6.0)
        );
        stubDiaryForWeekYear();
        when(sliceMapperMock.fromNutritionSummary(any())).thenReturn(expectedSlices);

        command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        verify(chartDisplayerMock).display(any(), eq(expectedSlices));
    }

    @Test
    void testExecuteLessArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of()),
                "execute() with no arguments should throw InvalidCommandException");
        verify(chartDisplayerMock, never()).display(any(), any());
    }

    @Test
    void testExecuteWeekNumberWithoutYearIsRejected() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_WEEK_STR)),
                "A week number without a year should be rejected");
        verify(chartDisplayerMock, never()).display(any(), any());
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR, DATE_IN_TARGET_WEEK)),
                "execute() with three arguments should throw InvalidCommandException");
        verify(chartDisplayerMock, never()).display(any(), any());
    }

    @Test
    void testExecuteInvalidDate() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of("not-a-date")),
                "execute() with an invalid date format should throw InvalidCommandException");
        verify(chartDisplayerMock, never()).display(any(), any());
    }

    @Test
    void testExecuteWeekNumberBelowMinIsInvalid() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(String.valueOf(MIN_SUPPORTED_WEEK_NUMBER - 1), TARGET_YEAR_STR)),
                "execute() with week number below 1 should throw InvalidCommandException");
        verify(chartDisplayerMock, never()).display(any(), any());
    }

    @Test
    void testExecuteWeekNumberAboveMaxIsInvalid() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(String.valueOf(MAX_SUPPORTED_WEEK_NUMBER + 1), TARGET_YEAR_STR)),
                "execute() with week number above the supported maximum should throw InvalidCommandException");
        verify(chartDisplayerMock, never()).display(any(), any());
    }

    @Test
    void testExecuteYearBelowMinIsInvalid() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_WEEK_STR, String.valueOf(MIN_SUPPORTED_YEAR - 1))),
                "execute() with a year below the supported minimum should throw InvalidCommandException");
        verify(chartDisplayerMock, never()).display(any(), any());
    }

    private void stubDiaryForWeekYear() {
        when(foodDiaryMock.getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR))
                .thenReturn(EMPTY_SUMMARY);
    }

    private void stubDiaryForDate() {
        when(foodDiaryMock.getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR))
                .thenReturn(EMPTY_SUMMARY);
    }
}
