package com.fmi.myfitnesspal.command;

import java.util.Arrays;
import java.util.List;

public class CommandParser {

    /**
     * Parses the input string into a {@link Command} object.
     *
     * <p>
     * This method takes a non-null and non-blank input string, trims leading and trailing whitespace,
     * and replaces consecutive whitespace sequences with a single space. It then splits the input string
     * into tokens based on space delimiter and constructs a {@link Command} object.
     *
     * @param input the input string to parse into a command
     * @return a {@link Command} object representing the parsed command
     * @throws IllegalArgumentException if the input is null, empty, or contains only whitespace characters
     * @see Command
     */
    public Command parseCommand(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("The input can not be null, empty or blank");
        }

        input = input.trim().replaceAll("\\s+", " ");
        List<String> tokens = Arrays.asList(input.split(" "));

        return new Command(tokens.get(0), tokens.subList(1, tokens.size()));
    }
}
