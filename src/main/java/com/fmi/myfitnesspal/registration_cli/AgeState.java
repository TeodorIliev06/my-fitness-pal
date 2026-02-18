package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;

import java.util.Scanner;

public final class AgeState implements State {
    @Override
    public void performState(UserBuilder builder, Scanner scanner) {
        System.out.println("---------- Your age is also important ------------");
        int age;
        do {
            System.out.println("Your age:");
            age = Integer.parseInt(scanner.nextLine());
        } while (age < 1);
        builder.setAge(age);
    }

    @Override
    public State getNextState() {
        return new SexState();
    }
}
