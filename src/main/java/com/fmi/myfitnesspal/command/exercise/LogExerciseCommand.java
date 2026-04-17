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

public final class LogExerciseCommand implements ExecutableCommand {
    private static final int ARGUMENTS_COUNT = 2;
    private static final String NAME = "log-exercise";
    private final ExerciseDiary exerciseDiary;

    public LogExerciseCommand(ExerciseDiary exerciseDiary) {
        this.exerciseDiary = exerciseDiary;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);
        LocalDate date = parseDate(arguments.get(0));
        String exerciseName = arguments.get(1);

        try {
            exerciseDiary.logExercise(date, exerciseName);
        } catch (UnknownExerciseException e) {
            return GlobalConstants.NOT_EXISTING_EXERCISE_MESSAGE;
        }

        return String.format("%s was logged successfully!", exerciseName);
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
