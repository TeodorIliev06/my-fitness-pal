package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.MealPool;
import com.fmi.myfitnesspal.food.MealId;
import com.fmi.myfitnesspal.food.Meal;

import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseDouble;

public final class CreateMealCommand implements ExecutableCommand {
    private static final String COMMAND_NAME = "create-meal";
    private final MealPool meals;
    private final FoodPool foodPool;

    public CreateMealCommand(MealPool meals, FoodPool foodPool) {
        this.meals = meals;
        this.foodPool = foodPool;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, (args) -> args.size() % 3 == 2);

        MealId mealId = new MealId(arguments.get(0), arguments.get(1));
        Meal resultMeal = new Meal(mealId);

        for (int i = 2; i < arguments.size() - 2; i += 3) {
            FoodId foodId = new FoodId(arguments.get(i), arguments.get(i + 1));
            double numberOfServings = parseDouble(arguments.get(i + 2));
            Food food = this.foodPool.getFood(foodId);
            resultMeal.addFood(food, numberOfServings);
        }

        this.meals.addMeal(resultMeal);

        return GlobalConstants.SUCCESSFULLY_CREATED_MEAL_MESSAGE;
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
