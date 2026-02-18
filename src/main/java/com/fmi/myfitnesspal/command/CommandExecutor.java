package com.fmi.myfitnesspal.command;

import java.util.Objects;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exception.UnknownCommandException;

public class CommandExecutor {
    private final ExecutableCommandRegistry registry;

    public CommandExecutor(ExecutableCommandRegistry registry) {
        this.registry = Objects.requireNonNull(registry);
    }

    /**
     * Executes a command by delegating to specific handler methods based on the command type.
     *
     * @param command the command to execute
     * @return the result of executing the command, typically a success message or error message
     */
    public String execute(Command command) {
        try {
            ExecutableCommand executableCommand = registry.getExecutableCommand(command.command());
            return executableCommand.execute(command.arguments());
        } catch (UnknownCommandException e) {
            return "Unknown command";
        } catch (InvalidCommandException e) {
            return e.getMessage();
        }
    }
}
