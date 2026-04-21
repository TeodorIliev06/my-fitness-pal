package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.BoundedCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exercise.ExerciseDiary;
import com.fmi.myfitnesspal.exercise.WeeklyCardioSummary;
import com.fmi.myfitnesspal.utility.WeekYear;

import java.util.List;

import static com.fmi.myfitnesspal.utility.DateHelper.parseWeekYear;

public final class ShowWeeklyCardioCommand extends BoundedCommand {

    @Override protected int minArgCount() {
        return 1;
    }
    @Override protected int maxArgCount() {
        return 2;
    }

    private static final String COMMAND_NAME = "show-weekly-cardio";

    private final ExerciseDiary exerciseDiary;

    public ShowWeeklyCardioCommand(ExerciseDiary exerciseDiary) {
        this.exerciseDiary = exerciseDiary;
    }

    @Override
    public String doExecute(List<String> arguments) throws InvalidCommandException {
        WeekYear weekYear = parseWeekYear(arguments);
        WeeklyCardioSummary summary = exerciseDiary.getWeeklyCardioSummary(weekYear.weekNumber(), weekYear.year());

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
