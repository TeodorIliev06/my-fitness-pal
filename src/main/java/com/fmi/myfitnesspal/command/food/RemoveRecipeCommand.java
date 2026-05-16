package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.RecipeId;
import com.fmi.myfitnesspal.food.RecipePool;

import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class RemoveRecipeCommand implements ExecutableCommand {

    private static final String COMMAND_NAME = "remove-recipe";
    private static final int ARGUMENTS_COUNT = 2;

    private final RecipePool recipePool;

    public RemoveRecipeCommand(RecipePool recipePool) {
        this.recipePool = recipePool;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        RecipeId targetId = new RecipeId(arguments.get(0), arguments.get(1));
        this.recipePool.removeRecipe(targetId);

        return GlobalConstants.SUCCESSFULLY_REMOVED_RECIPE_MESSAGE;
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <recipeName> <recipeDescription>";
    }
}
