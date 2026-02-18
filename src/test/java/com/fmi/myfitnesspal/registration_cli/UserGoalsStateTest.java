package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.exception.SecondaryGoalNotApplicableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class UserGoalsStateTest {

    private UserGoalsState userGoalsState;

    private UserBuilder builder;

    @BeforeEach
    public void setUp() {
        userGoalsState = new UserGoalsState();
        builder = new UserBuilder();
    }

    @Test
    public void testPrimaryGoalsInputInvalidInput() {
        Scanner scanner = new Scanner(new ByteArrayInputStream("LOSE_WEIGHT,INVALID_GOAL".getBytes()));
        assertThrows(IllegalArgumentException.class, () -> userGoalsState.performState(builder, scanner));
    }

    @Test
    public void testPerformStateValidInput() {
        String input = "LOSE_WEIGHT\nlack of time";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        userGoalsState.performState(builder, scanner);
        // Validate secondary goals
        Set<String> expectedGoal = new HashSet<>();
        expectedGoal.add("lack of time");
        assertEquals(expectedGoal, builder.build().goals().get(0).getSecondaryGoals());
    }

    @Test
    public void testPerformStateInputInvalidInput() {
        String input = "LOSE_WEIGHT,GAIN_MUSCLE\ninvalid\nbulk up";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        assertThrows(SecondaryGoalNotApplicableException.class, () -> userGoalsState.performState(builder, scanner));
    }
}
