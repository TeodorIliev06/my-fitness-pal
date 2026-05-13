package com.fmi.myfitnesspal.utility;

import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;

import java.util.Optional;

public final class NumberParser {

    private NumberParser() {
    }

    public static int parseInteger(String number) throws InvalidCommandException {
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
}
