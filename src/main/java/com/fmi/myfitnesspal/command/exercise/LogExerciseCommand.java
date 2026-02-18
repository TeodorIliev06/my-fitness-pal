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

public class LogExerciseCommand implements ExecutableCommand {
    private static final int ARGUMENTS_COUNT = 2;
    private static final String NAME = "log-exercise";
    private ExerciseDiary diary;

    public LogExerciseCommand(ExerciseDiary diary) {
        this.diary = diary;
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
        LocalDate date = parseDate(arguments.get(0));
        String exerciseName = arguments.get(1);

        try {
            diary.logExercise(date, exerciseName);
        } catch (UnknownExerciseException e) {
            return GlobalConstants.NOT_EXISTING_EXERCISE_MESSAGE;
        }

        return String.format("%s was logged successfully!", exerciseName);
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
        return "Usage: " + NAME + " <date> <exercise_name>";
    }
}
