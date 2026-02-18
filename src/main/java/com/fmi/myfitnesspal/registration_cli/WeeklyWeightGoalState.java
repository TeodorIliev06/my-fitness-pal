package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.weight.Weight;
import com.fmi.myfitnesspal.user.weight.WeightMeasurementUnit;

import java.util.Arrays;
import java.util.Scanner;

public final class WeeklyWeightGoalState implements State {
    @Override
    public void performState(UserBuilder builder, Scanner scanner) {
        System.out.println("---------- How much weight you want to lose per week? ---------");
        System.out.println("Weight measurements:");
        Arrays.stream(WeightMeasurementUnit.values()).forEach(System.out::println);
        System.out.println("Your choice: ");
        WeightMeasurementUnit weightMeasure = WeightMeasurementUnit.valueOf(scanner.nextLine());
        int weeklyWeightGoal;
        do {
            System.out.println("Your weekly weight goal: ");
            weeklyWeightGoal = Integer.parseInt(scanner.nextLine());
        } while (weeklyWeightGoal < 1);
        builder.setWeeklyWeightGoal(new Weight(weeklyWeightGoal, weightMeasure));
    }

    @Override
    public State getNextState() {
        return new UserGoalsState();
    }
}
