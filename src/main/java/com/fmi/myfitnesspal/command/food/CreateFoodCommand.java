package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.FoodPool;

import java.util.List;
import java.util.Optional;

import static com.fmi.myfitnesspal.utility.NumberParser.parseDouble;
import static com.fmi.myfitnesspal.utility.NumberParser.parseDoubleOptional;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class CreateFoodCommand implements ExecutableCommand {
    private static final String COMMAND_NAME = "create-food";
    private static final int ARGUMENTS_COUNT = 7;

    private final FoodPool foodPool;

    public CreateFoodCommand(FoodPool foodPool) {
        this.foodPool = foodPool;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        Food newFood = parseFood(arguments);
        this.foodPool.addFood(newFood);

        return GlobalConstants.SUCCESSFULLY_CREATED_FOOD_MESSAGE;
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <foodBrand> <foodDescription> <servingSize> "
                + "<calories> <fats> <protein> <carbs>";
    }

    private Food parseFood(List<String> arguments) throws InvalidCommandException {
        FoodId id = new FoodId(arguments.get(0), arguments.get(1));
        double servingSize = parseDouble(arguments.get(2));
        double calories = parseDouble(arguments.get(3));
        Optional<Double> fats = parseDoubleOptional(arguments.get(4));
        Optional<Double> protein = parseDoubleOptional(arguments.get(5));
        Optional<Double> carbs = parseDoubleOptional(arguments.get(6));

        return Food.builder(id, servingSize, calories)
                .setFats(fats)
                .setProtein(protein)
                .setCarbs(carbs)
                .build();
    }
}
