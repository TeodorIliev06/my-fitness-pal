package com.fmi.myfitnesspal.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fmi.myfitnesspal.user.exception.SecondaryGoalNotApplicableException;
import com.fmi.myfitnesspal.user.goal.Goal;
import com.fmi.myfitnesspal.user.goal.GoalType;
import com.fmi.myfitnesspal.user.goal.PossibleGoals;

@ExtendWith(MockitoExtension.class)
public final class GoalTest {
    @Mock
    private PossibleGoals possibleGoalsMock;

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "", "  " })
    void testAddGoalThrowsExceptionInvalidArguments(String secondaryGoal) {
        Goal goal = new Goal(GoalType.LOSE_WEIGHT, possibleGoalsMock);
        assertThrows(IllegalArgumentException.class, () -> goal.addSecondaryGoal(secondaryGoal), "The method should "
                + "throw IllegalArgumentException when the function is called with null, empty or blank string");
    }

    @ParameterizedTest
    @MethodSource("provideImpossibleGoalsCombinations")
    void testAddGoalThrowsExceptionImpossibleGoalCombination(GoalType primaryGoal, String secondaryGoal) {
        Goal goal = new Goal(primaryGoal, possibleGoalsMock);
        when(possibleGoalsMock.isPossibleGoal(primaryGoal, secondaryGoal)).thenReturn(false);

        assertThrows(SecondaryGoalNotApplicableException.class, () -> goal.addSecondaryGoal(secondaryGoal),
                "The method should throw SecondaryGoalNotApplicableException "
                        + "when the function is called with not applicable secondary goal");

    }

    @ParameterizedTest
    @MethodSource("providePossibleGoalsCombinations")
    void testAddGoalPossibleGoalCombination(GoalType primaryGoal, String secondaryGoal) {
        Goal goal = new Goal(primaryGoal, possibleGoalsMock);
        when(possibleGoalsMock.isPossibleGoal(primaryGoal, secondaryGoal)).thenReturn(true);

        goal.addSecondaryGoal(secondaryGoal);
        assertEquals(1, goal.getSecondaryGoals().size(), "The secondary goal is not stored");
        assertTrue(goal.getSecondaryGoals().contains(secondaryGoal), "The secondary goal is not added properly");
    }

    private static Stream<Arguments> provideImpossibleGoalsCombinations() {
        return Stream.of(
                Arguments.of(GoalType.LOSE_WEIGHT, "less sugar"),
                Arguments.of(GoalType.MAINTAIN_WEIGHT, "Impossible"),
                Arguments.of(GoalType.MANAGE_STRESS, "less fat"),
                Arguments.of(GoalType.GAIN_MUSCLE, "eat vegan")
        );
    }

    private static Stream<Arguments> providePossibleGoalsCombinations() {
        return Stream.of(
                Arguments.of(GoalType.LOSE_WEIGHT, "lack of time"),
                Arguments.of(GoalType.MAINTAIN_WEIGHT, "food carving"),
                Arguments.of(GoalType.MANAGE_STRESS, "mediate"),
                Arguments.of(GoalType.GAIN_MUSCLE, "bulk up")
        );
    }
}
