package com.fmi.myfitnesspal.command.utility;

import com.fmi.myfitnesspal.calorie.CalorieGoalType;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.user.sex.Sex;
import com.fmi.myfitnesspal.water.Portion;
import com.fmi.myfitnesspal.exception.InvalidCommandException;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class CommandUtilities {

    private CommandUtilities() {
    }

    public static void validateArgumentsCount(List<String> arguments, int expectedCount)
            throws InvalidCommandException {
        if (arguments.size() != expectedCount) {
            throw new InvalidCommandException("Command expected to be with " + expectedCount + " arguments");
        }
    }

    public static void validateArgumentsCount(List<String> arguments, Predicate<List<String>> validator)
            throws InvalidCommandException {
        if (!validator.test(arguments)) {
            throw new InvalidCommandException(GlobalConstants.NOT_VALID_ARGUMENTS_COUNT_MESSAGE);
        }
    }

    public static void validatePositiveValues(int... arguments)
        throws InvalidCommandException {
        for (int number : arguments) {
            if (number < 0) {
                throw new InvalidCommandException(GlobalConstants.NOT_VALID_ARGUMENTS_NEGATIVE_VALUE_MESSAGE);
            }
        }
    }

    public static int parseInt(String number) throws InvalidCommandException {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("The number provided is not valid integer", e);
        }
    }

    public static double parseDouble(String number) throws InvalidCommandException {
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException(GlobalConstants.NOT_VALID_DOUBLE_MESSAGE, e);
        }
    }

    public static Optional<Double> parseDoubleOptional(String number) throws InvalidCommandException {
        if (number.equals(GlobalConstants.EMPTY_OPTIONAL_VALUE)) {
            return Optional.empty();
        }

        double value = parseDouble(number);
        return Optional.of(value);
    }

    public static Portion parsePortion(String portion) throws InvalidCommandException {
        try {
            return Portion.valueOf(portion);
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("The water portion does not exist", e);
        }
    }

    public static EatingTime parseEatingTime(String eatingTime) throws InvalidCommandException {
        try {
            return EatingTime.valueOf(eatingTime.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException(GlobalConstants.NOT_EXISTING_EATING_TIME_MESSAGE, e);
        }
    }

    public static Sex parseSex(String argument) throws InvalidCommandException {
        try {
            return Sex.valueOf(argument.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("The sex provided does not exist", e);
        }
    }

    public static CalorieGoalType parseCalorieGoalType(String argument) throws InvalidCommandException {
        try {
            return CalorieGoalType.valueOf(argument.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("The calorie goal type provided does not exist", e);
        }
    }
}
