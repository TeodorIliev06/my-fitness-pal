package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.CardioExercise;
import com.fmi.myfitnesspal.exercise.Exercise;
import com.fmi.myfitnesspal.exercise.ExercisePool;

import java.time.LocalTime;
import java.util.List;

import static com.fmi.myfitnesspal.utility.DateHelper.parseTime;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseInt;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validatePositiveValues;

public final class CreateCardioExerciseCommand implements ExecutableCommand {
    private static final int ARGUMENTS_COUNT = 4;
    private static final String NAME = "create-cardio-exercise";
    private final ExercisePool exercisePool;

    public CreateCardioExerciseCommand(ExercisePool exercisePool) {
        this.exercisePool = exercisePool;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        String name = arguments.get(0);
        int durationInMinutes = parseInt(arguments.get(1));
        int burnedCalories = parseInt(arguments.get(2));
        LocalTime startTime = parseTime(arguments.get(3));

        validatePositiveValues(durationInMinutes, burnedCalories);

        Exercise newCardioExercise = new CardioExercise(name, durationInMinutes, burnedCalories, startTime);
        exercisePool.createExercise(newCardioExercise);
        return String.format("%s was created successfully!", name);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + NAME + " <name> <duration> <burnedCalories> <startTime>";
    }
}
