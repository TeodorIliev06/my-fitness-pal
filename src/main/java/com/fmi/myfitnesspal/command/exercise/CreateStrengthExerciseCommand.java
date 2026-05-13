package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.Exercise;
import com.fmi.myfitnesspal.exercise.ExercisePool;
import com.fmi.myfitnesspal.exercise.StrengthExercise;

import java.util.List;

import static com.fmi.myfitnesspal.utility.NumberParser.parseInteger;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validatePositiveValues;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class CreateStrengthExerciseCommand implements ExecutableCommand {
    private static final int ARGUMENTS_COUNT = 5;
    private static final String NAME = "create-strength-exercise";
    private final ExercisePool exercisePool;

    public CreateStrengthExerciseCommand(ExercisePool exercisePool) {
        this.exercisePool = exercisePool;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        String name = arguments.get(0);
        int sets = parseInteger(arguments.get(1));
        int reps = parseInteger(arguments.get(2));
        int weight = parseInteger(arguments.get(3));
        int burnedCalories = parseInteger(arguments.get(4));

        validatePositiveValues(sets, reps, weight, burnedCalories);

        Exercise newStrengthExercise = new StrengthExercise(name, sets, reps, weight, burnedCalories);
        exercisePool.createExercise(newStrengthExercise);
        return String.format("%s was created successfully!", name);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + NAME + " <name> <sets> <reps> <weight> <burnedCalories>";
    }
}
