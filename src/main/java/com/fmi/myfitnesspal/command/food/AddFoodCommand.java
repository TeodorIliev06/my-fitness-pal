package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodPool;

import com.fmi.myfitnesspal.constants.GlobalConstants;

import java.time.LocalDate;
import java.util.List;

import static com.fmi.myfitnesspal.utility.DateHelper.parseDate;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseEatingTime;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseDouble;

public final class AddFoodCommand implements ExecutableCommand {

    private static final String COMMAND_NAME = "add-food";
    private static final int ARGUMENTS_COUNT = 5;
    private final FoodDiary foodDiary;
    private final FoodPool foodPool;

    public AddFoodCommand(FoodDiary foodDiary, FoodPool foodPool) {
        this.foodDiary = foodDiary;
        this.foodPool = foodPool;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        LocalDate date = parseDate(arguments.get(0));
        EatingTime eatingTime = parseEatingTime(arguments.get(1));
        FoodId foodId = new FoodId(arguments.get(2), arguments.get(3));
        double numberOfServings = parseDouble(arguments.get(4));

        Food targetFood = this.foodPool.getFood(foodId);
        this.foodDiary.addFood(date, eatingTime, targetFood, numberOfServings);

        return GlobalConstants.SUCCESSFULLY_ADDED_FOOD_MESSAGE;

    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <date> <eatingTime> <foodBrand> <foodDescription> <servingCount>";
    }
}
