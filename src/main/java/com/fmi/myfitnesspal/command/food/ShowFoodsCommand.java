package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;


import java.time.LocalDate;
import java.util.List;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseEatingTime;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseDate;

public final class ShowFoodsCommand implements ExecutableCommand {
    private static final String COMMAND_NAME = "show-foods";
    private static final int ARGUMENTS_COUNT = 2;
    private final FoodDiary foodDiary;

    public ShowFoodsCommand(FoodDiary foodDiary) {
        this.foodDiary = foodDiary;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        LocalDate date = parseDate(arguments.get(0));
        EatingTime eatingTime = parseEatingTime(arguments.get(1));
        StringBuilder result = new StringBuilder();
        List<Food> foods = this.foodDiary.getFoodsByDateAndEatingTime(date, eatingTime);
        for (Food food : foods) {
            result.append(food.toString());
            result.append(System.lineSeparator());
        }

        return result.toString();
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
