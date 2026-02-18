package com.fmi.myfitnesspal.command.help;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.command.ExecutableCommandRegistry;
import com.fmi.myfitnesspal.exception.InvalidCommandException;

public class HelpCommand implements ExecutableCommand {
    private static final String COMMAND_NAME = "help";
    private static final int ARGUMENTS_COUNT = 0;

    private final ExecutableCommandRegistry registry;

    public HelpCommand(ExecutableCommandRegistry registry) {
        this.registry = Objects.requireNonNull(registry);
    }

    /**
     * Executes the command with the given arguments.
     *
     * @param arguments a list of string arguments for the command
     * @return the result of executing the command
     * @throws InvalidCommandException if the command execution fails due to invalid arguments or other reasons
     */
    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);
        Collection<ExecutableCommand> commands = registry.getAllCommands();

        StringBuilder output = new StringBuilder();
        for (ExecutableCommand command : commands) {
            output.append(command.getHelp()).append('\n');
        }

        return output.toString();
    }

    /**
     * Returns the name of the command.
     *
     * @return the command's name as a string
     */
    @Override
    public String name() {
        return COMMAND_NAME;
    }

    /**
     * Provides help information for the command.
     *
     * @return a string containing help information about the command
     */
    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME;
    }
}
