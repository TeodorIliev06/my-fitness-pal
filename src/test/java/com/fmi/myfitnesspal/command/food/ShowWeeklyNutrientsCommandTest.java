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
    private static final String TARGET_WEEK_STR = String.valueOf(TARGET_WEEK);
    private static final String DATE_IN_TARGET_WEEK = "08.01.2024";

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
    void testExecuteByWeekNumberOpensChart() throws InvalidCommandException {
        when(foodDiaryMock.getFoodsByWeekNumber(TARGET_WEEK)).thenReturn(List.of());
        when(sliceMapperMock.fromNutritionSummary(any())).thenReturn(List.of());

        command.execute(List.of(TARGET_WEEK_STR));

        verify(chartDisplayerMock).display(any(), any());
    }

    @Test
    void testExecuteByWeekNumberReturnsChartOpenedMessage() throws InvalidCommandException {
        when(foodDiaryMock.getFoodsByWeekNumber(TARGET_WEEK)).thenReturn(List.of());
        when(sliceMapperMock.fromNutritionSummary(any())).thenReturn(List.of());

        String result = command.execute(List.of(TARGET_WEEK_STR));

        assertTrue(result.startsWith("Chart opened:"),
                "execute() should return a message starting with \"Chart opened:\"");
    }

    @Test
    void testExecuteByDateResolvesToCorrectWeek() throws InvalidCommandException {
        when(foodDiaryMock.getFoodsByWeekNumber(TARGET_WEEK)).thenReturn(List.of());
        when(sliceMapperMock.fromNutritionSummary(any())).thenReturn(List.of());

        command.execute(List.of(DATE_IN_TARGET_WEEK));

        verify(foodDiaryMock).getFoodsByWeekNumber(TARGET_WEEK);
    }

    @Test
    void testExecuteChartTitleContainsWeekNumber() throws InvalidCommandException {
        when(foodDiaryMock.getFoodsByWeekNumber(TARGET_WEEK)).thenReturn(List.of());
        when(sliceMapperMock.fromNutritionSummary(any())).thenReturn(List.of());

        command.execute(List.of(TARGET_WEEK_STR));

        verify(chartDisplayerMock).display(titleCaptor.capture(), any());
        assertTrue(titleCaptor.getValue().contains(TARGET_WEEK_STR),
                "Chart title should contain the week number");
    }

    @Test
    void testExecutePassesWeeklySummaryToMapper() throws InvalidCommandException {
        when(foodDiaryMock.getFoodsByWeekNumber(TARGET_WEEK)).thenReturn(List.of());
        when(sliceMapperMock.fromNutritionSummary(any())).thenReturn(List.of());

        command.execute(List.of(TARGET_WEEK_STR));

        verify(sliceMapperMock).fromNutritionSummary(any(WeeklyNutritionSummary.class));
    }

    @Test
    void testExecutePassesMapperSlicesToDisplayer() throws InvalidCommandException {
        List<PieSlice> expectedSlices = List.of(
                new PieSlice("Protein", 12.0),
                new PieSlice("Carbs", 60.0),
                new PieSlice("Fats", 6.0)
        );
        when(foodDiaryMock.getFoodsByWeekNumber(TARGET_WEEK)).thenReturn(List.of());
        when(sliceMapperMock.fromNutritionSummary(any())).thenReturn(expectedSlices);

        command.execute(List.of(TARGET_WEEK_STR));

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
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_WEEK_STR, DATE_IN_TARGET_WEEK)),
                "execute() with more than one argument should throw InvalidCommandException");
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
                () -> command.execute(List.of("0")),
                "execute() with week number below 1 should throw InvalidCommandException");
        verify(chartDisplayerMock, never()).display(any(), any());
    }

    @Test
    void testExecuteWeekNumberAboveMaxIsInvalid() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of("54")),
                "execute() with week number above 53 should throw InvalidCommandException");
        verify(chartDisplayerMock, never()).display(any(), any());
    }
}
