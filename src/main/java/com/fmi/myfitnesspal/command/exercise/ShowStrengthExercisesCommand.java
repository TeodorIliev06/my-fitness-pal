package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.ExercisePool;

import java.util.List;
import java.util.stream.Collectors;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class ShowStrengthExercisesCommand implements ExecutableCommand {
    private static final String NAME = "show-strength-exercises";
    private static final int ARGUMENTS_COUNT = 0;
    private final ExercisePool exercisePool;

    public ShowStrengthExercisesCommand(ExercisePool exercisePool) {
        this.exercisePool = exercisePool;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);
        return exercisePool.getStrengthExercises().values().stream()
            .map(Object::toString)
            .collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + NAME;
    }
}
