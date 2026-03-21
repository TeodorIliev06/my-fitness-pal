package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.utility.NutritionSliceMapper;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.DailyMealCaloriesSummary;
import com.fmi.myfitnesspal.food.EatingTime;
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

import java.time.LocalDate;
import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.DATE_FORMATTER;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ShowDailyMealCaloriesCommandTest {

    private static final LocalDate TARGET_DATE = LocalDate.of(2025, 5, 25);
    private static final String TARGET_DATE_STR = TARGET_DATE.format(DATE_FORMATTER);

    @Mock
    private FoodDiary foodDiaryMock;
    @Mock
    private PieChartDisplayer chartDisplayerMock;
    @Mock
    private NutritionSliceMapper sliceMapperMock;

    @Captor
    private ArgumentCaptor<String> titleCaptor;

    @InjectMocks
    private ShowDailyMealCaloriesCommand command;

    @Test
    void testExecuteOpensChartForValidDate() throws InvalidCommandException {
        stubAllMealTimesEmpty();
        when(sliceMapperMock.fromDailyMealCaloriesSummary(any())).thenReturn(List.of());

        command.execute(List.of(TARGET_DATE_STR));

        verify(chartDisplayerMock).display(any(), any());
    }

    @Test
    void testExecuteReturnsChartOpenedMessage() throws InvalidCommandException {
        stubAllMealTimesEmpty();
        when(sliceMapperMock.fromDailyMealCaloriesSummary(any())).thenReturn(List.of());

        String result = command.execute(List.of(TARGET_DATE_STR));

        assertTrue(result.startsWith("Chart opened:"),
                "execute() should return a message starting with \"Chart opened:\"");
    }

    @Test
    void testExecuteChartTitleContainsFormattedDate() throws InvalidCommandException {
        stubAllMealTimesEmpty();
        when(sliceMapperMock.fromDailyMealCaloriesSummary(any())).thenReturn(List.of());

        command.execute(List.of(TARGET_DATE_STR));

        verify(chartDisplayerMock).display(titleCaptor.capture(), any());
        assertTrue(titleCaptor.getValue().contains(TARGET_DATE_STR),
                "Chart title should contain the formatted date");
    }

    @Test
    void testExecutePassesSummaryToMapper() throws InvalidCommandException {
        stubAllMealTimesEmpty();
        when(sliceMapperMock.fromDailyMealCaloriesSummary(any())).thenReturn(List.of());

        command.execute(List.of(TARGET_DATE_STR));

        verify(sliceMapperMock).fromDailyMealCaloriesSummary(any(DailyMealCaloriesSummary.class));
    }

    @Test
    void testExecutePassesMapperSlicesToDisplayer() throws InvalidCommandException {
        List<PieSlice> expectedSlices = List.of(new PieSlice("Breakfast", 300.0));
        stubAllMealTimesEmpty();
        when(sliceMapperMock.fromDailyMealCaloriesSummary(any())).thenReturn(expectedSlices);

        command.execute(List.of(TARGET_DATE_STR));

        verify(chartDisplayerMock).display(any(), eq(expectedSlices));
    }

    @Test
    void testExecuteNoArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of()),
                "execute() with no arguments should throw InvalidCommandException");
        verify(chartDisplayerMock, never()).display(any(), any());
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_DATE_STR, "extra")),
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

    private void stubAllMealTimesEmpty() {
        for (EatingTime mealTime : EatingTime.values()) {
            when(foodDiaryMock.getAllFoodsByDateAndEatingTime(TARGET_DATE, mealTime))
                    .thenReturn(List.of());
        }
    }
}
