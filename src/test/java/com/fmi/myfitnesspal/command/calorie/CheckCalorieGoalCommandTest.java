package com.fmi.myfitnesspal.command.calorie;

import com.fmi.myfitnesspal.calorie.CalorieGoal;
import com.fmi.myfitnesspal.calorie.CalorieGoalHolder;
import com.fmi.myfitnesspal.chart.BarChartDisplayer;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.DailyNutritionSummary;
import com.fmi.myfitnesspal.food.FoodDiary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.fmi.myfitnesspal.utility.DateHelper.DATE_FORMATTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class CheckCalorieGoalCommandTest {

    private static final LocalDate TARGET_DATE = LocalDate.of(2026, 3, 16);
    private static final String TARGET_DATE_STR = TARGET_DATE.format(DATE_FORMATTER);
    private static final int DAILY_CALORIE_GOAL = 2000;
    private static final CalorieGoal ACTIVE_GOAL = new CalorieGoal(DAILY_CALORIE_GOAL);

    @Mock
    private FoodDiary foodDiaryMock;
    @Mock
    private CalorieGoalHolder calorieGoalHolderMock;
    @Mock
    private BarChartDisplayer barChartDisplayerMock;

    @InjectMocks
    private CheckCalorieGoalCommand command;

    @Captor
    private ArgumentCaptor<String> titleCaptor;

    @Test
    void testExecuteFormatsAboveGoalMessage() throws InvalidCommandException {
        stubActiveGoal();
        stubWeeklySummariesWithDailyCalories(DAILY_CALORIE_GOAL + 20);

        String result = command.execute(List.of(TARGET_DATE_STR));

        assertEquals(
                "This week you had an average of 20 calories more than your 2000 calorie goal.",
                result,
                "Command must report the positive deviation when average exceeds the goal");
    }

    @Test
    void testExecuteFormatsBelowGoalMessage() throws InvalidCommandException {
        stubActiveGoal();
        stubWeeklySummariesWithDailyCalories(DAILY_CALORIE_GOAL - 300);

        String result = command.execute(List.of(TARGET_DATE_STR));

        assertEquals(
                "This week you had an average of 300 calories less than your 2000 calorie goal.",
                result,
                "Command must report the negative deviation when average falls below the goal");
    }

    @Test
    void testExecuteFormatsExactGoalMessage() throws InvalidCommandException {
        stubActiveGoal();
        stubWeeklySummariesWithDailyCalories(DAILY_CALORIE_GOAL);

        String result = command.execute(List.of(TARGET_DATE_STR));

        assertEquals(
                "This week you exactly met your 2000 calorie goal.",
                result,
                "Command must report an exact match when average equals the goal exactly");
    }

    @Test
    void testExecuteDelegatesDisplayToBarChartDisplayer() throws InvalidCommandException {
        stubActiveGoal();
        stubWeeklySummariesWithDailyCalories(DAILY_CALORIE_GOAL);

        command.execute(List.of(TARGET_DATE_STR));

        verify(barChartDisplayerMock).display(anyString(), anyList(), eq(DAILY_CALORIE_GOAL));
    }

    @Test
    void testExecuteChartTitleContainsGoalValue() throws InvalidCommandException {
        stubActiveGoal();
        stubWeeklySummariesWithDailyCalories(DAILY_CALORIE_GOAL);

        command.execute(List.of(TARGET_DATE_STR));

        verify(barChartDisplayerMock).display(titleCaptor.capture(), anyList(), eq(DAILY_CALORIE_GOAL));
        assertTrue(
                titleCaptor.getValue().contains(String.valueOf(DAILY_CALORIE_GOAL)),
                "Chart title must contain the numeric goal value");
    }

    @Test
    void testExecuteThrowsWhenNoGoalIsSet() {
        when(calorieGoalHolderMock.getActiveCalorieGoal()).thenReturn(Optional.empty());

        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_DATE_STR)),
                "Command must throw when no calorie goal has been set yet");
        verify(barChartDisplayerMock, never()).display(any(), any(), anyInt());
        verify(foodDiaryMock, never()).getDailyNutritionSummariesForWeek(any());
    }

    @Test
    void testExecuteNoArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of()),
                "Command must throw when called with no arguments");
        verify(calorieGoalHolderMock, never()).getActiveCalorieGoal();
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_DATE_STR, "extra")),
                "Command must throw when called with more than one argument");
        verify(calorieGoalHolderMock, never()).getActiveCalorieGoal();
    }

    @Test
    void testExecuteInvalidDate() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of("not-a-date")),
                "Command must throw when the date argument cannot be parsed");
        verify(calorieGoalHolderMock, never()).getActiveCalorieGoal();
    }

    private void stubActiveGoal() {
        when(calorieGoalHolderMock.getActiveCalorieGoal()).thenReturn(Optional.of(ACTIVE_GOAL));
    }

    private void stubWeeklySummariesWithDailyCalories(int dailyCalories) {
        LocalDate monday = TARGET_DATE.with(DayOfWeek.MONDAY);
        List<DailyNutritionSummary> summaries = IntStream.range(0, 7)
                .mapToObj(monday::plusDays)
                .map(date -> new DailyNutritionSummary(
                        date, dailyCalories, Optional.empty(), Optional.empty(), Optional.empty()))
                .toList();

        when(foodDiaryMock.getDailyNutritionSummariesForWeek(TARGET_DATE))
                .thenReturn(summaries);
    }
}
