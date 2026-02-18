package com.fmi.myfitnesspal.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exception.UnknownCommandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public final class CommandExecutorTest {
    @Mock
    private ExecutableCommand commandMock;
    @Mock
    private ExecutableCommandRegistry factoryMock;
    private CommandExecutor executor;

    @BeforeEach
    void initialize() {
        executor = new CommandExecutor(factoryMock);
        when(factoryMock.getExecutableCommand(anyString())).thenReturn(commandMock);
    }

    @Test
    void testExecuteCommandUnknownCommand() {
        String commandName = "command";
        List<String> args = List.of("arg");

        when(factoryMock.getExecutableCommand(commandName)).thenThrow(UnknownCommandException.class);

        String message = executor.execute(new Command(commandName, args));
        assertEquals("Unknown command", message,
                "The method should return message that command is unknown");
    }

    @Test
    void testExecuteCommandInvalidCommand() throws InvalidCommandException {
        String commandName = "command";
        List<String> args = List.of("arg");

        String expected = "Error message";
        when(commandMock.execute(args)).thenThrow(new InvalidCommandException(expected));

        String actual = executor.execute(new Command(commandName, args));
        assertEquals(expected, actual, "The error message is not returned");
    }

    @Test
    void testExecuteCommandValidCommand() throws InvalidCommandException {
        String commandName = "command";
        List<String> args = List.of("arg");

        String expected = "Success message";
        when(commandMock.execute(args)).thenReturn(expected);

        String actual = executor.execute(new Command(commandName, args));
        assertEquals(expected, actual, "The success message is not returned");
    }
}
