package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.MealPool;
import com.fmi.myfitnesspal.food.MealId;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Meal;


import java.time.LocalDate;
import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseEatingTime;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseDate;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class AddMealCommand implements ExecutableCommand {
    private static final String COMMAND_NAME = "add-meal";
    private static final int ARGUMENTS_COUNT = 4;
    private final FoodDiary diary;
    private final MealPool availableMeals;

    public AddMealCommand(FoodDiary diary, MealPool availableFood) {
        this.diary = diary;
        this.availableMeals = availableFood;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        LocalDate date = parseDate(arguments.get(0));
        EatingTime eatingTime = parseEatingTime(arguments.get(1));
        MealId mealId = new MealId(arguments.get(2), arguments.get(3));
        Meal targetMeal = this.availableMeals.getMeal(mealId);
        this.diary.addMeal(date, eatingTime, targetMeal);

        return GlobalConstants.SUCCESSFULLY_ADDED_MEAL_MESSAGE;

    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <date> <eating time> <mealName> <mealDescription>";
    }
}
