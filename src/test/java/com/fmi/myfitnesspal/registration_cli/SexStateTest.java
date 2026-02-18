package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.sex.Sex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public final class SexStateTest {

    private SexState sexState;

    @BeforeEach
    public void setUp() {
        sexState = new SexState();
    }

    @Test
    public void testPerformStateValidInputSex() {
        Scanner scanner = new Scanner(new ByteArrayInputStream("MALE".getBytes()));
        UserBuilder builder = new UserBuilder();
        sexState.performState(builder, scanner);
        assertEquals(Sex.MALE, builder.build().sex());
    }

    @Test
    public void testPerformStateInvalidInputSex() {
        Scanner scanner = new Scanner(new ByteArrayInputStream("INVALID".getBytes()));
        UserBuilder builder = new UserBuilder();
        assertThrows(IllegalArgumentException.class, () -> sexState.performState(builder, scanner));
    }
}
