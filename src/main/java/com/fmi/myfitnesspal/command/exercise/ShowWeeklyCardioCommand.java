package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.ExerciseCalculator;
import com.fmi.myfitnesspal.exercise.ExerciseDiary;
import com.fmi.myfitnesspal.exercise.WeeklyCardioSummary;

import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.getWeekNumber;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class ShowWeeklyCardioCommand implements ExecutableCommand {

    private static final String COMMAND_NAME = "show-weekly-cardio";
    private static final int ARGUMENTS_COUNT = 1;

    private final ExerciseDiary diary;

    public ShowWeeklyCardioCommand(ExerciseDiary diary) {
        this.diary = diary;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        int weekNumber = getWeekNumber(arguments.get(0));
        WeeklyCardioSummary summary = ExerciseCalculator.getWeeklyCardio(diary, weekNumber);

        return buildReport(summary);
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <weekNumber | date>";
    }

    private String buildReport(WeeklyCardioSummary summary) {
        return String.format("Week %d: burned %d calories, time spent %s",
                summary.weekNumber(),
                summary.burnedCalories(),
                formatDuration(summary.totalMinutes()));
    }

    private String formatDuration(int totalMinutes) {
        if (totalMinutes >= 60) {
            int hours = totalMinutes / 60;
            int minutes = totalMinutes % 60;
            return String.format("%d h %d min", hours, minutes);
        }
        return String.format("%d min", totalMinutes);
    }
}
