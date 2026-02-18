package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.activitylevel.ActivityLevel;

import java.util.Arrays;
import java.util.Scanner;

public final class ActivityLevelState implements State {
    @Override
    public void performState(UserBuilder builder, Scanner scanner) {
        System.out.println("--------- Are you an active person? -------");
        System.out.println("Activity options:");
        Arrays.stream(ActivityLevel.values()).forEach(System.out::println);
        System.out.println("Your choice: ");
        ActivityLevel activityLevel = ActivityLevel.valueOf(scanner.nextLine());

        builder.setActivityLevel(activityLevel);
    }

    @Override
    public State getNextState() {
        return new WeightGoalState();
    }
}
