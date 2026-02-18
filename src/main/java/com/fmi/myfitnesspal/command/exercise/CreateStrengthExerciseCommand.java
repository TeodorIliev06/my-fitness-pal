package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.Exercise;
import com.fmi.myfitnesspal.exercise.ExercisePool;
import com.fmi.myfitnesspal.exercise.StrengthExercise;

import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseInt;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validatePositiveValues;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public class CreateStrengthExerciseCommand implements ExecutableCommand {
    private static final int ARGUMENTS_COUNT = 5;
    private static final String NAME = "create-strength-exercise";
    private ExercisePool exercisePool;

    public CreateStrengthExerciseCommand(ExercisePool exercisePool) {
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

        String name = arguments.get(0);
        int sets = parseInt(arguments.get(1));
        int reps = parseInt(arguments.get(2));
        int weight = parseInt(arguments.get(3));
        int burnedCalories = parseInt(arguments.get(4));

        validatePositiveValues(sets, reps, weight, burnedCalories);

        Exercise newStrengthExercise = new StrengthExercise(name, sets, reps, weight, burnedCalories);
        exercisePool.createExercise(newStrengthExercise);
        return String.format("%s was created successfully!", name);
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
        return "Usage: " + NAME + " <name> <sets> <reps> <weight> <burnedCalories>";
    }
}
