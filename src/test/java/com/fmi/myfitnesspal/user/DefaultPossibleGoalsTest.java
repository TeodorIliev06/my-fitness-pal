package com.fmi.myfitnesspal.user;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.fmi.myfitnesspal.user.goal.DefaultPossibleGoals;
import com.fmi.myfitnesspal.user.goal.GoalType;
import com.fmi.myfitnesspal.user.goal.PossibleGoals;

public final class DefaultPossibleGoalsTest {
    private PossibleGoals possibleGoals;

    @BeforeEach
    void setup() {
        this.possibleGoals = new DefaultPossibleGoals();
    }

    @Test
    void testAddGoalNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> possibleGoals.addGoal(null, null),
                "Expected IllegalArgumentException to be thrown when calling addGoal with null");
    }

    @Test
    void testAddGoalSecondaryGoalIsAdded() {
        possibleGoals.addGoal(GoalType.LOSE_WEIGHT, "new goal");

        assertTrue(possibleGoals.getSecondaryGoals(GoalType.LOSE_WEIGHT).contains("new goal"), "the goal is not added");
    }

    @ParameterizedTest
    @EnumSource(GoalType.class)
    void testInitialGoalsArePresent(GoalType goalType) {
        assertFalse(possibleGoals.getSecondaryGoals(goalType).isEmpty(), "The initial goals are not added");
    }
}
