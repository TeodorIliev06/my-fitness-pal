package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.food.FoodPortion;
import com.fmi.myfitnesspal.food.MealPool;
import com.fmi.myfitnesspal.food.MealId;
import com.fmi.myfitnesspal.food.Meal;

import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseFoodPortions;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class CreateMealCommand implements ExecutableCommand {
    private static final String COMMAND_NAME = "create-meal";
    private static final int HEADER_ARGS = 2;
    private static final int FOOD_PORTION_ARGS_COUNT = 3;
    private final MealPool mealPool;
    private final FoodPool foodPool;

    public CreateMealCommand(MealPool mealPool, FoodPool foodPool) {
        this.mealPool = mealPool;
        this.foodPool = foodPool;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, args -> args.size() >= HEADER_ARGS
                && (args.size() - HEADER_ARGS) % FOOD_PORTION_ARGS_COUNT == 0);

        MealId mealId = new MealId(arguments.get(0), arguments.get(1));
        List<FoodPortion> portions = parseFoodPortions(arguments, HEADER_ARGS, this.foodPool);
        this.mealPool.addMeal(createMeal(mealId, portions));

        return GlobalConstants.SUCCESSFULLY_CREATED_MEAL_MESSAGE;
    }

    private Meal createMeal(MealId mealId, List<FoodPortion> portions) {
        Meal meal = new Meal(mealId);
        for (FoodPortion portion : portions) {
            meal.addFoodPortion(portion.food(), portion.servingsUsed());
        }
        return meal;
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <mealName> <MealDescription> <brand1> <description1> <servings1> ...";
    }
}
