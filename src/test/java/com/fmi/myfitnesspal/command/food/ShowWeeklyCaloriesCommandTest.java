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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ShowWeeklyCaloriesCommandTest {

    private static final int TARGET_WEEK = 2;
    private static final String TARGET_WEEK_STR = String.valueOf(TARGET_WEEK);
    private static final String DATE_IN_TARGET_WEEK = "08.01.2024";
    private static final WeeklyNutritionSummary EMPTY_SUMMARY =
            new WeeklyNutritionSummary(TARGET_WEEK, 0, Optional.empty(), Optional.empty(), Optional.empty());

    @Mock
    private FoodDiary foodDiaryMock;

    @InjectMocks
    private ShowWeeklyCaloriesCommand command;

    @Test
    void testExecuteEmptyWeekByWeekNumber() throws InvalidCommandException {
        stubDiaryReturnsEmptySummary();

        String result = command.execute(List.of(TARGET_WEEK_STR));

        assertEquals("Week 2: 0 calories", result,
                "An empty week should report 0 calories with no macros block");
    }

    @Test
    void testExecuteEmptyWeekByDate() throws InvalidCommandException {
        stubDiaryReturnsEmptySummary();

        String result = command.execute(List.of(DATE_IN_TARGET_WEEK));

        assertEquals("Week 2: 0 calories", result,
                "Passing a date should resolve to the same week number as passing the number directly");
    }

    @Test
    void testExecuteWithCaloriesOnlyFood() throws InvalidCommandException {
        when(foodDiaryMock.getWeeklyNutritionSummary(TARGET_WEEK))
                .thenReturn(new WeeklyNutritionSummary(
                        TARGET_WEEK, 130, Optional.empty(), Optional.empty(), Optional.empty()));

        String result = command.execute(List.of(TARGET_WEEK_STR));

        assertEquals("Week 2: 130 calories", result,
                "Food with no macros should have only calories in the summary");
    }

    @Test
    void testExecuteWithMacrosProducesFullSummary() throws InvalidCommandException {
        when(foodDiaryMock.getWeeklyNutritionSummary(TARGET_WEEK))
                .thenReturn(new WeeklyNutritionSummary(
                        TARGET_WEEK, 165, Optional.of(31.0), Optional.of(0.0), Optional.of(3.6)));

        String result = command.execute(List.of(TARGET_WEEK_STR));

        assertEquals("Week 2: 165 calories, {31.0 protein, 0.0 carbs, 3.6 fats}", result,
                "Food with macros should produce a full summary including the macros block");
    }

    @Test
    void testExecuteDelegatesToFoodDiary() throws InvalidCommandException {
        stubDiaryReturnsEmptySummary();

        command.execute(List.of(TARGET_WEEK_STR));

        verify(foodDiaryMock).getWeeklyNutritionSummary(TARGET_WEEK);
    }

    @Test
    void testExecuteSumsMultipleFoods() throws InvalidCommandException {
        when(foodDiaryMock.getWeeklyNutritionSummary(TARGET_WEEK))
                .thenReturn(new WeeklyNutritionSummary(
                        TARGET_WEEK, 550, Optional.empty(), Optional.empty(), Optional.empty()));

        String result = command.execute(List.of(TARGET_WEEK_STR));

        assertEquals("Week 2: 550 calories", result,
                "Summed calories returned by the calculator should appear in the output");
    }

    @Test
    void testExecuteLessArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of()),
                "The method should throw InvalidCommandException when the count of the arguments is not 1");
        verify(foodDiaryMock, never()).getWeeklyNutritionSummary(TARGET_WEEK);
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_WEEK_STR, DATE_IN_TARGET_WEEK)),
                "The method should throw InvalidCommandException when the count of the arguments is not 1");
        verify(foodDiaryMock, never()).getWeeklyNutritionSummary(TARGET_WEEK);
    }

    @Test
    void testExecuteInvalidDate() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("not-a-date")),
                "The method should throw InvalidCommandException when the date format is invalid");
        verify(foodDiaryMock, never()).getWeeklyNutritionSummary(TARGET_WEEK);
    }

    @Test
    void testExecuteWeekNumberBelowMinIsInvalid() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("0")),
                "The method should throw InvalidCommandException for week number below 1");
        verify(foodDiaryMock, never()).getWeeklyNutritionSummary(TARGET_WEEK);
    }

    @Test
    void testExecuteWeekNumberAboveMaxIsInvalid() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("54")),
                "The method should throw InvalidCommandException for week number above 53");
        verify(foodDiaryMock, never()).getWeeklyNutritionSummary(TARGET_WEEK);
    }

    private void stubDiaryReturnsEmptySummary() {
        when(foodDiaryMock.getWeeklyNutritionSummary(TARGET_WEEK))
                .thenReturn(EMPTY_SUMMARY);
    }
}
