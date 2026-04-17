package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exception.UnknownExerciseException;
import com.fmi.myfitnesspal.exercise.ExerciseDiary;

import java.time.LocalDate;
import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseDate;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class RemoveExerciseCommand implements ExecutableCommand {
    private static final int ARGUMENTS_COUNT = 2;
    private static final String NAME = "remove-exercise";
    private final ExerciseDiary exerciseDiary;

    public RemoveExerciseCommand(ExerciseDiary exerciseDiary) {
        this.exerciseDiary = exerciseDiary;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);
        LocalDate date = parseDate(arguments.get(0));
        String exerciseName = arguments.get(1);

        try {
            exerciseDiary.removeExercise(date, exerciseName);
        } catch (UnknownExerciseException e) {
            throw new InvalidCommandException(GlobalConstants.NOT_EXISTING_EXERCISE_MESSAGE);
        }

        return String.format("%s was removed successfully!", exerciseName);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + NAME + " <date> <exercise_name>";
    }
}
