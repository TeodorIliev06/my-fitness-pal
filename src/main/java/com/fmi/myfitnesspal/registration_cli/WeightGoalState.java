package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.weight.Weight;
import com.fmi.myfitnesspal.user.weight.WeightMeasurementUnit;

import java.util.Arrays;
import java.util.Scanner;

public final class WeightGoalState implements State {
    @Override
    public void performState(UserBuilder builder, Scanner scanner) {
        System.out.println("-------- How much do you want to weight? -----------");
        System.out.println("Weight measurements:");
        Arrays.stream(WeightMeasurementUnit.values()).forEach(System.out::println);
        System.out.println("Your choice: ");
        WeightMeasurementUnit weightMeasure = WeightMeasurementUnit.valueOf(scanner.nextLine());
        int weightGoal;
        do {
            System.out.println("Your weight goal: ");
            weightGoal = Integer.parseInt(scanner.nextLine());
        } while (weightGoal < 1);
        builder.setWeightGoal(new Weight(weightGoal, weightMeasure));
    }

    @Override
    public State getNextState() {
        return new WeeklyWeightGoalState();
    }
}
