package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.RecipePool;

import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.formatItemList;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class ShowRecipesCommand implements ExecutableCommand {

    private static final String COMMAND_NAME = "show-recipes";
    private static final int ARGUMENTS_COUNT = 0;
    private final RecipePool recipePool;

    public ShowRecipesCommand(RecipePool recipePool) {
        this.recipePool = recipePool;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        return formatItemList(this.recipePool.getAllRecipes());
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME;
    }
}
