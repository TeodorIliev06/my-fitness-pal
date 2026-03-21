package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodCalculator;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.WeeklyNutritionSummary;

import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.getWeekNumber;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class ShowWeeklyCaloriesCommand implements ExecutableCommand {

    private static final String COMMAND_NAME = "show-weekly-calories";
    private static final int ARGUMENTS_COUNT = 1;

    private final FoodDiary diary;

    public ShowWeeklyCaloriesCommand(FoodDiary diary) {
        this.diary = diary;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        int weekNumber = getWeekNumber(arguments.get(0));
        WeeklyNutritionSummary summary = FoodCalculator.getWeeklyNutritionSummary(diary, weekNumber);

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

    private String buildReport(WeeklyNutritionSummary summary) {
        boolean macrosAvailable = summary.protein().isPresent()
                || summary.carbs().isPresent()
                || summary.fats().isPresent();

        if (macrosAvailable) {
            return String.format("Week %d: %.0f calories, {%.1f protein, %.1f carbs, %.1f fats}",
                    summary.weekNumber(),
                    summary.calories(),
                    summary.protein().orElse(0.0),
                    summary.carbs().orElse(0.0),
                    summary.fats().orElse(0.0));
        }

        return String.format("Week %d: %.0f calories", summary.weekNumber(), summary.calories());
    }
}
