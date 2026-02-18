package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.User;
import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.exception.SecondaryGoalNotApplicableException;

import java.util.Scanner;

public final class UserRegistration {
    private Scanner scanner;
    private UserBuilder builder;

    public UserRegistration(Scanner scanner) {
        this.scanner = scanner;
        this.builder = new UserBuilder();
    }

    public User registerNewUser() {

        State state = new HeightState();
        while (state != null) {
            try {
                state.performState(this.builder, this.scanner);
                state = state.getNextState();
            } catch (SecondaryGoalNotApplicableException e) {
                System.out.println("Please try again and be aware of spaces");
            } catch (IllegalArgumentException e) {
                System.out.println("incorrect input. Try again!");
            }
        }

        return builder.build();
    }
}
