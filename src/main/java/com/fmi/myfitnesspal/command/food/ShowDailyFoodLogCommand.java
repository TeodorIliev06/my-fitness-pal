package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodDiary;

import java.time.LocalDate;
import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;
import static com.fmi.myfitnesspal.utility.DateHelper.parseDate;

public final class ShowDailyFoodLogCommand implements ExecutableCommand {

    private static final String COMMAND_NAME = "show-daily-log";
    private static final int ARGUMENTS_COUNT = 1;
    private final FoodDiary foodDiary;

    public ShowDailyFoodLogCommand(FoodDiary foodDiary) {
        this.foodDiary = foodDiary;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        LocalDate consumptionDate = parseDate(arguments.get(0));
        StringBuilder result = new StringBuilder();

        for (EatingTime eatingTime : EatingTime.values()) {
            appendEatingTimeSection(result, consumptionDate, eatingTime);
        }

        return result.toString();
    }

    private void appendEatingTimeSection(StringBuilder result, LocalDate consumptionDate, EatingTime eatingTime) {
        result.append(eatingTime.getLabel()).append(":").append(System.lineSeparator());

        List<Food> foods = this.foodDiary.getAllFoodsByDateAndEatingTime(consumptionDate, eatingTime);
        for (Food food : foods) {
            result.append("   ").append(food.toString()).append(System.lineSeparator());
        }
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <date>";
    }
}
