package com.fmi.myfitnesspal.command;

import com.fmi.myfitnesspal.exception.UnknownCommandException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class to handle the registry of executable commands.
 */
public class ExecutableCommandRegistry {
    private final Map<String, ExecutableCommand> commands;

    /**
     * Default constructor for ExecutableCommandRegistry.
     * Initialises the registry with an empty command map.
     */
    public ExecutableCommandRegistry() {
        this(new HashMap<>());
    }

    /**
     * Constructor that initializes the ExecutableCommandRegistry with a map of commands.
     *
     * @param commands a map containing commands where the key is the command name and value is an
     *                instance of ExecutableCommand
     * @throws NullPointerException if the passed map is null
     */
    public ExecutableCommandRegistry(Map<String, ExecutableCommand> commands) {
        this.commands = Objects.requireNonNull(commands, "Commands map cannot be null.");
    }

    /**
     * Adds a new command to the registry, overwrites if it already exists.
     *
     * @param command the ExecutableCommand to be added to the registry
     * @throws NullPointerException if the passed command is null
     */
    public void addCommand(ExecutableCommand command) {
        Objects.requireNonNull(command, "Command cannot be null.");
        commands.put(command.name(), command);
    }

    /**
     * Retrieves an ExecutableCommand from the registry given its name.
     *
     * @param commandName the name of the command to retrieve
     * @return the ExecutableCommand associated with the command name
     * @throws UnknownCommandException if no command with the specified name is found in the registry
     */
    public ExecutableCommand getExecutableCommand(String commandName) {
        if (!commands.containsKey(commandName)) {
            throw new UnknownCommandException("The command " + commandName + " does not exist");
        }
        return commands.get(commandName);
    }

    /**
     * Retrieves all registered commands in this registry.
     *
     * @return a collection containing all registered ExecutableCommand instances
     */
    public Collection<ExecutableCommand> getAllCommands() {
        return commands.values();
    }
}
