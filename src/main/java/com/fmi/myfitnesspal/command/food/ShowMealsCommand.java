package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Meal;


import java.time.LocalDate;
import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseEatingTime;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseDate;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class ShowMealsCommand implements ExecutableCommand {
    private static final String COMMAND_NAME = "show-meals";
    private static final int ARGUMENTS_COUNT = 2;
    private final FoodDiary foodDiary;

    public ShowMealsCommand(FoodDiary foodDiary) {
        this.foodDiary = foodDiary;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        LocalDate date = parseDate(arguments.get(0));
        EatingTime eatingTime = parseEatingTime(arguments.get(1));
        StringBuilder sb = new StringBuilder();
        List<Meal> meals = this.foodDiary.getMealsByDateAndEatingTime(date, eatingTime);
        for (Meal meal : meals) {
            sb.append(meal.toString());
        }

        return sb.toString();
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <date> <eatingTime>";
    }
}
