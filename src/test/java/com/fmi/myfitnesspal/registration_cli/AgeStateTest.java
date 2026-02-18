package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class AgeStateTest {

    @Test
    public void testPerformStateValidInputAge() {
        UserBuilder builder = new UserBuilder();
        Scanner scanner = new Scanner(new ByteArrayInputStream("70\n".getBytes()));
        AgeState ageState = new AgeState();
        ageState.performState(builder, scanner);
        assertEquals(70, builder.build().age());
    }

    @Test
    public void testPerformStateInvalidInputAge() {
        UserBuilder builder = new UserBuilder();
        Scanner scanner = new Scanner(new ByteArrayInputStream("-5\n70\n".getBytes()));
        AgeState ageState = new AgeState();
        ageState.performState(builder, scanner);
        assertEquals(70, builder.build().age());
    }

}
