package com.fmi.myfitnesspal.command.calorie;

import com.fmi.myfitnesspal.calorie.CalorieGoal;
import com.fmi.myfitnesspal.calorie.CalorieGoalHolder;
import com.fmi.myfitnesspal.calorie.CalorieGoalType;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.user.sex.Sex;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public final class SetCalorieGoalCommandTest {

    private static final Sex TARGET_SEX = Sex.MALE;
    private static final CalorieGoalType TARGET_GOAL_TYPE = CalorieGoalType.LOSE_WEIGHT;
    private static final String TARGET_SEX_ARG = TARGET_SEX.name();
    private static final String TARGET_GOAL_TYPE_ARG = TARGET_GOAL_TYPE.name();
    private static final int EXPECTED_CALORIE_TARGET =
            TARGET_SEX.getBaseBmr() + TARGET_GOAL_TYPE.getCalorieAdjustment();

    @Mock
    private CalorieGoalHolder calorieGoalHolderMock;

    @InjectMocks
    private SetCalorieGoalCommand command;

    @Test
    void testExecuteWithValidArgumentsDelegatesToHolder() throws InvalidCommandException {
        String result = command.execute(List.of(TARGET_SEX_ARG, TARGET_GOAL_TYPE_ARG));

        verify(calorieGoalHolderMock).setActiveCalorieGoal(new CalorieGoal(EXPECTED_CALORIE_TARGET));
        assertEquals(
                String.format("Calorie goal set to %d.", EXPECTED_CALORIE_TARGET), result,
                "Command must return the formatted confirmation message with the calculated target");
    }

    @Test
    void testExecuteCallsSetGoalForEachInvocation() throws InvalidCommandException {
        int firstTarget = Sex.MALE.getBaseBmr() + CalorieGoalType.LOSE_WEIGHT.getCalorieAdjustment();
        int secondTarget = Sex.FEMALE.getBaseBmr() + CalorieGoalType.GAIN_WEIGHT.getCalorieAdjustment();

        command.execute(List.of("MALE", "LOSE_WEIGHT"));
        command.execute(List.of("FEMALE", "GAIN_WEIGHT"));

        verify(calorieGoalHolderMock).setActiveCalorieGoal(new CalorieGoal(firstTarget));
        verify(calorieGoalHolderMock).setActiveCalorieGoal(new CalorieGoal(secondTarget));
    }

    @Test
    void testExecuteWithInvalidArgumentsCountThrows() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_SEX_ARG)),
                "Command must throw when argument count is not exactly 2");
        verify(calorieGoalHolderMock, never()).setActiveCalorieGoal(any());
    }

    @Test
    void testExecuteWithInvalidSexThrows() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of("INVALID_SEX", TARGET_GOAL_TYPE_ARG)),
                "Command must throw when the sex argument cannot be parsed");
        verify(calorieGoalHolderMock, never()).setActiveCalorieGoal(any());
    }

    @Test
    void testExecuteWithInvalidGoalTypeThrows() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(TARGET_SEX_ARG, "INVALID_GOAL_TYPE")),
                "Command must throw when the goal type argument cannot be parsed");
        verify(calorieGoalHolderMock, never()).setActiveCalorieGoal(any());
    }
}
