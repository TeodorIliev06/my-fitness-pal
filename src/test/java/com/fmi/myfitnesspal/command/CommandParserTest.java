package com.fmi.myfitnesspal.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public final class CommandParserTest {
    private final CommandParser parser = new CommandParser();

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "  " })
    void testParseCommandNullAndBlankInput(String input) {
        assertThrows(IllegalArgumentException.class, () -> parser.parseCommand(input),
                "The method should throw IllegalArgumentException when it is called with null, empty or blank string");
    }

    @Test
    void testParseCommandWithoutArguments() {
        String input = "  mycommand  ";
        Command expected = new Command("mycommand", new ArrayList<>());
        assertEquals(expected, parser.parseCommand(input), "The command without arguments is not parsed properly");
    }

    @Test
    void testParseCommandWithArguments() {
        String input = "  mycommand  myarg1 myarg2";
        Command expected = new Command("mycommand", List.of("myarg1", "myarg2"));
        assertEquals(expected, parser.parseCommand(input), "The command with arguments is not parsed properly");
    }
}
