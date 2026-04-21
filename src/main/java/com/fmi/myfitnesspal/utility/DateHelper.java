package com.fmi.myfitnesspal.utility;

import com.fmi.myfitnesspal.exception.InvalidCommandException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.IsoFields;
import java.util.List;

import static com.fmi.myfitnesspal.utility.NumberParser.parseInteger;

public final class DateHelper {

    public static final int MIN_SUPPORTED_WEEK_NUMBER = 1;
    public static final int MAX_SUPPORTED_WEEK_NUMBER = 53;
    public static final int MIN_SUPPORTED_YEAR = 1970;
    public static final String DATE_TODAY = "today";
    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String TIME_NOW = "now";
    public static final String TIME_FORMAT = "HH:mm";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    private DateHelper() {
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

    public static int getWeekNumberFrom(LocalDate date) {
        return date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    public static int getYearFrom(LocalDate date) {
        return date.get(IsoFields.WEEK_BASED_YEAR);
    }

    public static int parseWeekNumber(String weekNumber) throws InvalidCommandException {
        int week = parseInteger(weekNumber);

        if (week < MIN_SUPPORTED_WEEK_NUMBER || week > MAX_SUPPORTED_WEEK_NUMBER) {
            throw new InvalidCommandException("Week number must be between " + MIN_SUPPORTED_WEEK_NUMBER
                    + " and " + MAX_SUPPORTED_WEEK_NUMBER);
        }

        return week;
    }

    public static int parseYear(String year) throws InvalidCommandException {
        int parsedYear = parseInteger(year);
        int maximumSupportedYear = getYearFrom(LocalDate.now());

        if (parsedYear < MIN_SUPPORTED_YEAR || parsedYear > maximumSupportedYear) {
            throw new InvalidCommandException(
                    "Year must be between " + MIN_SUPPORTED_YEAR + " and " + maximumSupportedYear);
        }

        return parsedYear;
    }

    public static WeekYear parseWeekYear(List<String> arguments) throws InvalidCommandException {
        if (arguments.size() == 2) {
            int weekNumber = parseWeekNumber(arguments.get(0));
            int year = parseYear(arguments.get(1));

            return new WeekYear(weekNumber, year);
        }

        LocalDate date = parseDate(arguments.get(0));
        return new WeekYear(getWeekNumberFrom(date), getYearFrom(date));
    }
}
