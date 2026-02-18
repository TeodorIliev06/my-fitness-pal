package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.height.Height;
import com.fmi.myfitnesspal.user.height.LengthMeasurementUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public final class HeightStateTest {

    private HeightState heightState;

    @BeforeEach
    public void setUp() {
        heightState = new HeightState();
    }

    @Test
    public void testPerformStateValidInputHeight() {
        UserBuilder builder = new UserBuilder();
        Scanner scanner = new Scanner(new ByteArrayInputStream("INCH\n70\n".getBytes()));
        heightState.performState(builder, scanner);
        assertEquals(new Height(70, LengthMeasurementUnit.INCH), builder.build().height());
    }

    @Test
    public void testPerformStateInvalidInputHeight() {
        UserBuilder builder = new UserBuilder();
        Scanner scanner = new Scanner(new ByteArrayInputStream("INVALID\n70\n".getBytes()));
        assertThrows(IllegalArgumentException.class, () -> heightState.performState(builder, scanner));
    }

}
