package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.MealId;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.EatingTime;

import java.time.LocalDate;
import java.util.List;

import static com.fmi.myfitnesspal.utility.DateHelper.parseDate;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseEatingTime;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class RemoveMealCommand implements ExecutableCommand {
    private static final String COMMAND_NAME = "remove-meal";

    private static final int ARGUMENTS_COUNT = 4;
    private final FoodDiary foodDiary;

    public RemoveMealCommand(FoodDiary foodDiary) {
        this.foodDiary = foodDiary;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        LocalDate date = parseDate(arguments.get(0));
        EatingTime eatingTime = parseEatingTime(arguments.get(1));
        MealId mealId = new MealId(arguments.get(2), arguments.get(3));
        this.foodDiary.removeMeal(date, eatingTime, mealId);

        return GlobalConstants.SUCCESSFULLY_REMOVED_MEAL_MESSAGE;

    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <date> <eatingTime> <mealName> <mealDescription>";
    }
}
