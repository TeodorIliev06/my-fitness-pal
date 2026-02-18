package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.sex.Sex;

import java.util.Arrays;
import java.util.Scanner;

public final class SexState implements State {
    @Override
    public void performState(UserBuilder builder, Scanner scanner) {
        System.out.println("---------- What is your sex? ---------");
        System.out.println("Sex types:");
        Arrays.stream(Sex.values()).forEach(System.out::println);
        System.out.println("Your choice: ");
        Sex sex = Sex.valueOf(scanner.nextLine());
        builder.setSex(sex);
    }

    @Override
    public State getNextState() {
        return new CountryState();
    }
}
