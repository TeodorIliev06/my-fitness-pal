package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.height.Height;
import com.fmi.myfitnesspal.user.height.LengthMeasurementUnit;

import java.util.Arrays;
import java.util.Scanner;

public final class HeightState implements State {

    @Override
    public void performState(UserBuilder builder, Scanner scanner) {
        System.out.println("------Let's begin with your,. height------");
        System.out.println("Height measurements:");
        Arrays.stream(LengthMeasurementUnit.values()).forEach(System.out::println);
        LengthMeasurementUnit heightMeasure;
        System.out.println("Your choice: ");
        String measureChoice = scanner.nextLine();
        heightMeasure = LengthMeasurementUnit.valueOf(measureChoice);
        int height;
        do {
            System.out.println("Your height: ");
            height = Integer.parseInt(scanner.nextLine());
        } while (height < 1);

        builder.setHeight(new Height(height, heightMeasure));
    }

    @Override
    public State getNextState() {
        return new WeightState();
    }
}
