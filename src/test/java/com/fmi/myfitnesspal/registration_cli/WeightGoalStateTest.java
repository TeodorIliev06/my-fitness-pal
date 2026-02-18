package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.weight.Weight;
import com.fmi.myfitnesspal.user.weight.WeightMeasurementUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WeightGoalStateTest {


    private WeightGoalState weightGoalState;

    @BeforeEach
    public void setUp() {
        weightGoalState = new WeightGoalState();
    }

    @Test
    public void testPerformStateValidInput() {
        Scanner scanner = new Scanner(new ByteArrayInputStream("KILOGRAM\n80".getBytes()));
        UserBuilder builder = new UserBuilder();
        weightGoalState.performState(builder, scanner);
        assertEquals(new Weight(80, WeightMeasurementUnit.KILOGRAM), builder.build().weightGoal());
    }

    @Test
    public void testPerformStateInvalidWeight() {
        Scanner scanner = new Scanner(new ByteArrayInputStream("KILOGRAM\n-5\n50".getBytes()));
        UserBuilder builder = new UserBuilder();
        weightGoalState.performState(builder, scanner);
        assertEquals(new Weight(50, WeightMeasurementUnit.KILOGRAM), builder.build().weightGoal());
    }

}
