package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.weight.Weight;
import com.fmi.myfitnesspal.user.weight.WeightMeasurementUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class WeeklyWeightGoalStateTest {
    private WeeklyWeightGoalState weeklyWeightGoalState;

    @BeforeEach
    public void setUp() {
        weeklyWeightGoalState = new WeeklyWeightGoalState();
    }


    @Test
    public void testPerformStateValidInput() {
        UserBuilder builder = new UserBuilder();
        Scanner scanner = new Scanner(new ByteArrayInputStream("KILOGRAM\n3".getBytes()));
        weeklyWeightGoalState.performState(builder, scanner);
        assertEquals(new Weight(3, WeightMeasurementUnit.KILOGRAM), builder.build().weeklyWeightGoal());
    }

    @Test
    public void testPerformStateInvalidInput() {
        UserBuilder builder = new UserBuilder();
        Scanner scanner = new Scanner(new ByteArrayInputStream("POUND\n-1\n5".getBytes()));
        weeklyWeightGoalState.performState(builder, scanner);
        assertEquals(new Weight(5, WeightMeasurementUnit.POUND), builder.build().weeklyWeightGoal());
    }

    @Test
    public void testPerformStateThrowsNumberFormatException() {
        UserBuilder builder = new UserBuilder();
        Scanner scanner = new Scanner(new ByteArrayInputStream("KILOGRAM\nabc\n3".getBytes()));
        assertThrows(NumberFormatException.class, () -> weeklyWeightGoalState.performState(builder, scanner));
    }
}
