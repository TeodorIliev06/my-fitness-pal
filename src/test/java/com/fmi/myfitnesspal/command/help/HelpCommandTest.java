package com.fmi.myfitnesspal.command.help;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.command.ExecutableCommandRegistry;
import com.fmi.myfitnesspal.exception.InvalidCommandException;

@ExtendWith(MockitoExtension.class)
public final class HelpCommandTest {
    @Mock
    private ExecutableCommandRegistry registryMock;
    @Mock
    private ExecutableCommand commandMock1;
    @Mock
    private ExecutableCommand commandMock2;
    @Mock
    private ExecutableCommand commandMock3;

    @InjectMocks
    private HelpCommand helpCommand;

    @Test
    void testExecute() throws InvalidCommandException {
        when(commandMock1.getHelp()).thenReturn("help1");
        when(commandMock2.getHelp()).thenReturn("help2");
        when(commandMock3.getHelp()).thenReturn("help3");

        when(registryMock.getAllCommands()).thenReturn(List.of(commandMock1, commandMock2, commandMock3));

        String helpMessage = helpCommand.execute(new ArrayList<>());
        assertEquals("help1\nhelp2\nhelp3\n", helpMessage);
    }

    @Test
    void testExecuteWrongArgumentsCount() {
        assertThrows(InvalidCommandException.class, () -> helpCommand.execute(List.of("arg1")),
                "Method should throw exception when the command is called with arguments");
    }
}
