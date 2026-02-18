package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.activitylevel.ActivityLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ActivityLevelStateTest {

    private ActivityLevelState activityLevelState;

    @BeforeEach
    public void setUp() {
        activityLevelState = new ActivityLevelState();
    }

    @Test
    public void testPerformStateValidInput() {
        Scanner scanner = new Scanner(new ByteArrayInputStream("ACTIVE".getBytes()));
        UserBuilder builder = new UserBuilder();
        activityLevelState.performState(builder, scanner);
        assertEquals(ActivityLevel.ACTIVE, builder.build().activityLevel());
    }
}
