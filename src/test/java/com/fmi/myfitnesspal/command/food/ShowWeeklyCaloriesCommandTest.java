package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.WeeklyNutritionSummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.fmi.myfitnesspal.utility.DateHelper.MIN_SUPPORTED_WEEK_NUMBER;
import static com.fmi.myfitnesspal.utility.DateHelper.MAX_SUPPORTED_WEEK_NUMBER;
import static com.fmi.myfitnesspal.utility.DateHelper.MIN_SUPPORTED_YEAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ShowWeeklyCaloriesCommandTest {

    private static final int TARGET_WEEK = 2;
    private static final int TARGET_YEAR = 2024;
    private static final String TARGET_WEEK_STR = String.valueOf(TARGET_WEEK);
    private static final String TARGET_YEAR_STR = String.valueOf(TARGET_YEAR);
    private static final String DATE_IN_TARGET_WEEK = "08.01.2024";

    private static final WeeklyNutritionSummary EMPTY_SUMMARY = new WeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR,
            0, Optional.empty(), Optional.empty(), Optional.empty());

    @Mock
    private FoodDiary foodDiaryMock;

    @InjectMocks
    private ShowWeeklyCaloriesCommand command;

    @Test
    void testExecuteEmptyWeekByWeekNumber() throws InvalidCommandException {
        stubDiaryReturnsEmptySummary();

        String result = command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        assertEquals("Week 2: 0 calories", result,
                "An empty week should report 0 calories with no macros block");
    }

    @Test
    void testExecuteEmptyWeekByDate() throws InvalidCommandException {
        stubDiaryReturnsEmptySummary();

        String result = command.execute(List.of(DATE_IN_TARGET_WEEK));

        assertEquals("Week 2: 0 calories", result,
                "Passing a date should resolve to the same week and year as passing them directly");
    }

    @Test
    void testExecuteWithCaloriesOnlyFood() throws InvalidCommandException {
        when(foodDiaryMock.getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR))
                .thenReturn(new WeeklyNutritionSummary(
                        TARGET_WEEK, TARGET_YEAR, 130, Optional.empty(), Optional.empty(), Optional.empty()));

        String result = command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        assertEquals("Week 2: 130 calories", result,
                "Food with no macros should have only calories in the summary");
    }

    @Test
    void testExecuteWithMacrosProducesFullSummary() throws InvalidCommandException {
        when(foodDiaryMock.getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR))
                .thenReturn(new WeeklyNutritionSummary(
                        TARGET_WEEK, TARGET_YEAR, 165, Optional.of(31.0), Optional.of(0.0), Optional.of(3.6)));

        String result = command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        assertEquals("Week 2: 165 calories, {31.0 protein, 0.0 carbs, 3.6 fats}", result,
                "Food with macros should produce a full summary including the macros block");
    }

    @Test
    void testExecuteDelegatesToFoodDiary() throws InvalidCommandException {
        stubDiaryReturnsEmptySummary();

        command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        verify(foodDiaryMock).getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR);
    }

    @Test
    void testExecuteSumsMultipleFoods() throws InvalidCommandException {
        when(foodDiaryMock.getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR))
                .thenReturn(new WeeklyNutritionSummary(
                        TARGET_WEEK, TARGET_YEAR, 550, Optional.empty(), Optional.empty(), Optional.empty()));

        String result = command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR));

        assertEquals("Week 2: 550 calories", result,
                "Summed calories returned by the diary should appear in the output");
    }

    @Test
    void testExecuteLessArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of()),
                "The method should throw InvalidCommandException when no arguments are provided");
        verify(foodDiaryMock, never()).getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR);
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_WEEK_STR, TARGET_YEAR_STR, DATE_IN_TARGET_WEEK)),
                "The method should throw InvalidCommandException when more than 2 arguments are provided");
        verify(foodDiaryMock, never()).getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR);
    }

    @Test
    void testExecuteInvalidDate() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("not-a-date")),
                "The method should throw InvalidCommandException when the date format is invalid");
        verify(foodDiaryMock, never()).getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR);
    }

    @Test
    void testExecuteWeekNumberWithoutYearIsRejected() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of(TARGET_WEEK_STR)),
                "A week number without a year should be rejected");
        verify(foodDiaryMock, never()).getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR);
    }

    @Test
    void testExecuteWeekNumberBelowMinIsInvalid() {
        assertThrows(InvalidCommandException.class, () -> command.execute(
                List.of(String.valueOf(MIN_SUPPORTED_WEEK_NUMBER - 1), TARGET_YEAR_STR)),
                "The method should throw InvalidCommandException for week number below the supported minimum");
        verify(foodDiaryMock, never()).getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR);
    }

    @Test
    void testExecuteWeekNumberAboveMaxIsInvalid() {
        assertThrows(InvalidCommandException.class, () -> command.execute(
                List.of(String.valueOf(MAX_SUPPORTED_WEEK_NUMBER + 1), TARGET_YEAR_STR)),
                "The method should throw InvalidCommandException for week number above the supported maximum");
        verify(foodDiaryMock, never()).getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR);
    }

    @Test
    void testExecuteYearBelowMinIsInvalid() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of(TARGET_WEEK_STR,
                        String.valueOf(MIN_SUPPORTED_YEAR - 1))),
                "The method should throw InvalidCommandException for a year below the supported minimum");
        verify(foodDiaryMock, never()).getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR);
    }

    private void stubDiaryReturnsEmptySummary() {
        when(foodDiaryMock.getWeeklyNutritionSummary(TARGET_WEEK, TARGET_YEAR))
                .thenReturn(EMPTY_SUMMARY);
    }
}
