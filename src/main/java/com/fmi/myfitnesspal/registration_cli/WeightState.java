package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.weight.Weight;
import com.fmi.myfitnesspal.user.weight.WeightMeasurementUnit;

import java.util.Arrays;
import java.util.Scanner;

public final class WeightState implements State {


    @Override
    public void performState(UserBuilder builder, Scanner scanner) {
        System.out.println("---- Now input your weight details -----");
        System.out.println("Weight measurements:");
        Arrays.stream(WeightMeasurementUnit.values()).forEach(System.out::println);
        System.out.println("Your choice: ");
        String measureChoice = scanner.nextLine();
        WeightMeasurementUnit weightMeasure = WeightMeasurementUnit.valueOf(measureChoice);
        int weight;
        do {
            System.out.println("Your weight: ");
            weight = Integer.parseInt(scanner.nextLine());
        } while (weight < 1);
        builder.setWeight(new Weight(weight, weightMeasure));
    }

    @Override
    public State getNextState() {
        return new AgeState();
    }
}
