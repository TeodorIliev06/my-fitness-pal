package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.BoundedCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.WeeklyNutritionSummary;
import com.fmi.myfitnesspal.utility.WeekYear;

import java.util.List;

import static com.fmi.myfitnesspal.utility.DateHelper.parseWeekYear;

public final class ShowWeeklyCaloriesCommand extends BoundedCommand {

    @Override protected int minArgCount() {
        return 1;
    }
    @Override protected int maxArgCount() {
        return 2;
    }

    private static final String COMMAND_NAME = "show-weekly-calories";

    private final FoodDiary foodDiary;

    public ShowWeeklyCaloriesCommand(FoodDiary foodDiary) {
        this.foodDiary = foodDiary;
    }

    @Override
    public String doExecute(List<String> arguments) throws InvalidCommandException {
        WeekYear weekYear = parseWeekYear(arguments);
        WeeklyNutritionSummary summary = foodDiary.getWeeklyNutritionSummary(weekYear.weekNumber(), weekYear.year());

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
