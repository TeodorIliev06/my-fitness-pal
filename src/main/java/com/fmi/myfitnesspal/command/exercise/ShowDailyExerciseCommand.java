package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.Exercise;
import com.fmi.myfitnesspal.exercise.ExerciseDiary;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.fmi.myfitnesspal.utility.DateHelper.parseDate;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class ShowDailyExerciseCommand implements ExecutableCommand {
    private static final String NAME = "show-daily-exercise";
    private static final int ARGUMENTS_COUNT = 1;
    private final ExerciseDiary exerciseDiary;

    public ShowDailyExerciseCommand(ExerciseDiary exerciseDiary) {
        this.exerciseDiary = exerciseDiary;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);
        LocalDate date = parseDate(arguments.get(0));

        List<Exercise> exercises = new ArrayList<>(exerciseDiary.getDailyExercise(date));
        return exercises.stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + NAME + " <date>";
    }
}
