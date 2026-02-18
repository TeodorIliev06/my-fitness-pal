package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.country.Country;

import java.util.Arrays;
import java.util.Scanner;

public final class CountryState implements State {
    @Override
    public void performState(UserBuilder builder, Scanner scanner) {
        System.out.println("------- Where do you live? -------");
        System.out.println("Countries:");
        Arrays.stream(Country.values()).forEach(System.out::println);
        System.out.println("Your choice: ");
        Country country = Country.valueOf(scanner.nextLine());
        builder.setCountry(country);
    }

    @Override
    public State getNextState() {
        return new ActivityLevelState();
    }
}
