package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.FoodId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ShowWeeklyCaloriesCommandTest {

    private static final int TARGET_WEEK = 2;
    private static final String TARGET_WEEK_STR = String.valueOf(TARGET_WEEK);
    private static final String DATE_IN_TARGET_WEEK = "08.01.2024";

    @Mock
    private FoodDiary diaryMock;

    @InjectMocks
    private ShowWeeklyCaloriesCommand command;

    @Test
    void testExecuteEmptyWeekByWeekNumber() throws InvalidCommandException {
        when(diaryMock.getFoodsByWeekNumber(TARGET_WEEK)).thenReturn(List.of());

        String result = command.execute(List.of(TARGET_WEEK_STR));

        assertEquals("Week 2: 0 calories", result,
                "An empty week should report 0 calories with no macros block");
        verify(diaryMock).getFoodsByWeekNumber(TARGET_WEEK);
    }

    @Test
    void testExecuteEmptyWeekByDate() throws InvalidCommandException {
        when(diaryMock.getFoodsByWeekNumber(TARGET_WEEK)).thenReturn(List.of());

        String result = command.execute(List.of(DATE_IN_TARGET_WEEK));

        assertEquals("Week 2: 0 calories", result,
                "Passing a date should resolve to the same week number as passing the number directly");
        verify(diaryMock).getFoodsByWeekNumber(TARGET_WEEK);
    }

    @Test
    void testExecuteWithCaloriesOnlyFood() throws InvalidCommandException {
        Food food = Food.builder(new FoodId("rice", "white"), 100, 130).build();
        when(diaryMock.getFoodsByWeekNumber(TARGET_WEEK)).thenReturn(List.of(food));

        String result = command.execute(List.of(TARGET_WEEK_STR));

        assertEquals("Week 2: 130 calories", result,
                "Food with no macros should have only calories in the summary");
        verify(diaryMock).getFoodsByWeekNumber(TARGET_WEEK);
    }

    @Test
    void testExecuteWithMacrosProducesFullSummary() throws InvalidCommandException {
        Food food = Food.builder(new FoodId("chicken", "breast"), 100, 165)
                .setProtein(Optional.of(31.0))
                .setCarbs(Optional.of(0.0))
                .setFats(Optional.of(3.6))
                .build();
        when(diaryMock.getFoodsByWeekNumber(TARGET_WEEK)).thenReturn(List.of(food));

        String result = command.execute(List.of(TARGET_WEEK_STR));

        assertEquals("Week 2: 165 calories, {31.0 protein, 0.0 carbs, 3.6 fats}", result,
                "Food with macros should produce a full summary including the macros block");
        verify(diaryMock).getFoodsByWeekNumber(TARGET_WEEK);
    }

    @Test
    void testExecuteWithMultipleFoods() throws InvalidCommandException {
        Food food = Food.builder(new FoodId("rice", "white"), 100, 200).build();
        Food mealIngredient = Food.builder(new FoodId("pasta", "whole grain"), 100, 350)
                .setProtein(Optional.of(12.0))
                .setCarbs(Optional.of(60.0))
                .setFats(Optional.of(3.0))
                .build();
        when(diaryMock.getFoodsByWeekNumber(TARGET_WEEK)).thenReturn(List.of(food, mealIngredient));

        String result = command.execute(List.of(TARGET_WEEK_STR));

        assertEquals("Week 2: 550 calories, {12.0 protein, 60.0 carbs, 3.0 fats}", result,
                "Calories from both standalone foods and meals should be summed correctly");
        verify(diaryMock).getFoodsByWeekNumber(TARGET_WEEK);
    }

    @Test
    void testExecuteAccumulatesMultipleDaysInSameWeek() throws InvalidCommandException {
        Food monday = Food.builder(new FoodId("oats", "plain"), 100, 200).build();
        Food wednesday = Food.builder(new FoodId("pasta", "whole grain"), 100, 350).build();
        when(diaryMock.getFoodsByWeekNumber(TARGET_WEEK)).thenReturn(List.of(monday, wednesday));

        String result = command.execute(List.of(TARGET_WEEK_STR));

        assertEquals("Week 2: 550 calories", result,
                "Calories from multiple days in the same week should be summed together");
        verify(diaryMock).getFoodsByWeekNumber(TARGET_WEEK);
    }

    @Test
    void testExecuteLessArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of()),
                "The method should throw InvalidCommandException when the count of the arguments is not 1");
        verify(diaryMock, never()).getFoodsByWeekNumber(anyInt());
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_WEEK_STR, DATE_IN_TARGET_WEEK)),
                "The method should throw InvalidCommandException when the count of the arguments is not 1");
        verify(diaryMock, never()).getFoodsByWeekNumber(anyInt());
    }

    @Test
    void testExecuteInvalidDate() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("not-a-date")),
                "The method should throw InvalidCommandException when the date format is invalid");
        verify(diaryMock, never()).getFoodsByWeekNumber(anyInt());
    }

    @Test
    void testExecuteWeekNumberBelowMinIsInvalid() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("0")),
                "The method should throw InvalidCommandException for week number below 1");
        verify(diaryMock, never()).getFoodsByWeekNumber(anyInt());
    }

    @Test
    void testExecuteWeekNumberAboveMaxIsInvalid() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("54")),
                "The method should throw InvalidCommandException for week number above 53");
        verify(diaryMock, never()).getFoodsByWeekNumber(anyInt());
    }
}
