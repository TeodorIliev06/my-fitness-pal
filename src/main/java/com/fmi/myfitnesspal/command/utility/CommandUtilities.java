package com.fmi.myfitnesspal.command.utility;

import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.water.Portion;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.IsoFields;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class CommandUtilities {
    private CommandUtilities() {
    }

    public static final String DATE_TODAY = "today";
    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String TIME_NOW = "now";
    public static final String TIME_FORMAT = "HH:mm";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);


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

    public static LocalDate parseDate(String date) throws InvalidCommandException {
        if (date.equalsIgnoreCase(DATE_TODAY)) {
            return LocalDate.now();
        }

        try {
            return LocalDate.parse(date, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidCommandException("Date must be passed in " + DATE_FORMAT + " format", e);
        }
    }

    public static LocalTime parseTime(String time) throws InvalidCommandException {
        if (time.equalsIgnoreCase(TIME_NOW)) {
            return LocalTime.now();
        }

        try {
            return LocalTime.parse(time, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidCommandException("Time must be passed in " + TIME_FORMAT + " format", e);
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

    public static int toWeekNumber(LocalDate date) {
        return date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    public static int parseWeekNumber(String weekNumber) throws InvalidCommandException {
        int week = parseInt(weekNumber);
        if (week < 1 || week > 53) {
            throw new InvalidCommandException("Week number must be between 1 and 53");
        }
        return week;
    }

    public static int getWeekNumber(String argument) throws InvalidCommandException {
        boolean isWeekNumber = argument.chars().allMatch(Character::isDigit);

        if (isWeekNumber) {
            return parseWeekNumber(argument);
        }

        LocalDate date = parseDate(argument);
        return toWeekNumber(date);
    }
}
