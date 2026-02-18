package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.Exercise;
import com.fmi.myfitnesspal.exercise.ExercisePool;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public class ShowWorkoutsCommand implements ExecutableCommand {
    private static final String NAME = "show-workouts";
    private static final int ARGUMENTS_COUNT = 0;
    private ExercisePool exercisePool;

    public ShowWorkoutsCommand(ExercisePool exercisePool) {
        this.exercisePool = exercisePool;
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
        List<Exercise> exercises = new ArrayList<>(exercisePool.getWorkouts().values());
        return exercises.stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * Returns the name of the command.
     *
     * @return the command's name as a string
     */
    @Override
    public String name() {
        return NAME;
    }

    /**
     * Provides help information for the command.
     *
     * @return a string containing help information about the command
     */
    @Override
    public String getHelp() {
        return "Usage: " + NAME;
    }
}
