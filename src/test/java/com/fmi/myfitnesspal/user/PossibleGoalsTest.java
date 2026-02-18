package com.fmi.myfitnesspal.user;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fmi.myfitnesspal.user.goal.GoalType;
import com.fmi.myfitnesspal.user.goal.PossibleGoals;

public final class PossibleGoalsTest {
    private PossibleGoals possibleGoals;

    @BeforeEach
    void setup() {
        this.possibleGoals = new PossibleGoals();
    }

    @Test
    void testAddGoalNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> possibleGoals.addGoal(null, null),
                "Expected IllegalArgumentException to be thrown when calling addGoal with null");
    }

    @Test
    void testGetGoalsNoGoalsPresent() {
        assertTrue(possibleGoals.getSecondaryGoals(GoalType.LOSE_WEIGHT).isEmpty());
    }

    @Test
    void testAddGoalSecondaryGoalIsAdded() {
        possibleGoals.addGoal(GoalType.LOSE_WEIGHT, "new goal");

        assertTrue(possibleGoals.getSecondaryGoals(GoalType.LOSE_WEIGHT).contains("new goal"), "the goal is not added");
    }
}
