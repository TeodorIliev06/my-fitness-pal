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

public final class WeightStateTest {

    private WeightState weightState;

    @BeforeEach
    public void setUp() {
        weightState = new WeightState();
    }

    @Test
    public void testPerformStateValidInputWeight() {
        UserBuilder builder = new UserBuilder();
        Scanner scanner = new Scanner(new ByteArrayInputStream("STONE\n70\n".getBytes()));
        weightState.performState(builder, scanner);
        assertEquals(new Weight(70, WeightMeasurementUnit.STONE), builder.build().weight());
    }

    @Test
    public void testPerformStateInvalidInputWeight() {
        UserBuilder builder = new UserBuilder();
        Scanner scanner = new Scanner(new ByteArrayInputStream("INVALID\n70\n".getBytes()));
        assertThrows(IllegalArgumentException.class, () -> weightState.performState(builder, scanner));
    }
}
