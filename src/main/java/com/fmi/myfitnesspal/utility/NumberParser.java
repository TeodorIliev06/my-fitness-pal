package com.fmi.myfitnesspal.utility;

import com.fmi.myfitnesspal.exception.InvalidCommandException;

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
}
