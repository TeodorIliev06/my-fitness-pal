package com.fmi.myfitnesspal.command;

import com.fmi.myfitnesspal.exception.InvalidCommandException;

import java.util.List;

/**
 * An interface representing an executable command.
 */
public interface ExecutableCommand {

    /**
     * Executes the command with the given arguments.
     *
     * @param arguments a list of string arguments for the command
     * @return the result of executing the command
     * @throws InvalidCommandException if the command execution fails due to invalid arguments or other reasons
     */
    String execute(List<String> arguments) throws InvalidCommandException;

    /**
     * Returns the name of the command.
     *
     * @return the command's name as a string
     */
    String name();

    /**
     * Provides help information for the command.
     *
     * @return a string containing help information about the command
     */
    String getHelp();
}
