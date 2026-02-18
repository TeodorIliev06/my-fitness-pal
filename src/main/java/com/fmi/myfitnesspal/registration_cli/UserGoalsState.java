package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.goal.DefaultPossibleGoals;
import com.fmi.myfitnesspal.user.goal.Goal;
import com.fmi.myfitnesspal.user.goal.GoalType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;


public final class UserGoalsState implements State {

    @Override
    public void performState(UserBuilder builder, Scanner scanner) {
        Set<GoalType> primaryGoals = insertPrimaryGoals(scanner);
        List<Goal> userGoals = new ArrayList<>();
        for (GoalType primaryGoal : primaryGoals) {
            DefaultPossibleGoals defaultPossibleGoals = new DefaultPossibleGoals();
            Goal goal = new Goal(primaryGoal);
            System.out.println(String.format("---------- What are your secondary goals for %s?", primaryGoal));
            System.out.println("Secondary goals:");
            for (String secondaryGoal : defaultPossibleGoals.getSecondaryGoals(primaryGoal)) {
                System.out.println(secondaryGoal);
            }
            System.out.println("Your choice(you can choose more than one/separate with ',' and without spaces): ");
            Arrays.stream(scanner.nextLine().split(",")).forEach(goal::addSecondaryGoal);
            userGoals.add(goal);
        }
        builder.setGoals(userGoals);
    }

    @Override
    public State getNextState() {
        return null;
    }

    private Set<GoalType> insertPrimaryGoals(Scanner scanner) {
        Set<GoalType> primaryGoals;
        System.out.println("---------- What is your primary goals? ---------");
        System.out.println("Primary goals:");
        Arrays.stream(GoalType.values()).forEach(System.out::println);
        do {
            primaryGoals = new HashSet<>();
            System.out.println("Your choice(you can choose more than one/separate with ',' and without spaces): ");
            for (String arg : Arrays.asList(scanner.nextLine().split(","))) {
                primaryGoals.add(GoalType.valueOf(arg));
                System.out.println(arg);
            }
            if (!GoalType.checkGoalsCompatibility(primaryGoals)) {
                System.out.println("incorrect choice. Please try again");
            }
        } while (!GoalType.checkGoalsCompatibility(primaryGoals));
        return primaryGoals;
    }
}
