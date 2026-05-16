package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.food.Recipe;
import com.fmi.myfitnesspal.food.RecipeId;
import com.fmi.myfitnesspal.food.FoodPortion;
import com.fmi.myfitnesspal.food.RecipePool;

import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseFoodPortions;
import static com.fmi.myfitnesspal.utility.NumberParser.parseInteger;

public final class CreateRecipeCommand implements ExecutableCommand {

    private static final String COMMAND_NAME = "create-recipe";
    private static final int HEADER_ARGS = 3;
    private static final int FOOD_PORTION_ARGS_COUNT = 3;
    private final RecipePool recipePool;
    private final FoodPool foodPool;

    public CreateRecipeCommand(RecipePool recipePool, FoodPool foodPool) {
        this.recipePool = recipePool;
        this.foodPool = foodPool;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, args -> args.size() >= HEADER_ARGS
                && (args.size() - HEADER_ARGS) % FOOD_PORTION_ARGS_COUNT == 0);

        RecipeId recipeId = new RecipeId(arguments.get(0), arguments.get(1));
        int numberOfServings = parseInteger(arguments.get(2));
        List<FoodPortion> foodPortions = parseFoodPortions(arguments, HEADER_ARGS, this.foodPool);
        this.recipePool.addRecipe(new Recipe(recipeId, foodPortions, numberOfServings));

        return GlobalConstants.SUCCESSFULLY_CREATED_RECIPE_MESSAGE;
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <name> <description> <numberOfServings> <brand1> <desc1> <servings1> ...";
    }
}
