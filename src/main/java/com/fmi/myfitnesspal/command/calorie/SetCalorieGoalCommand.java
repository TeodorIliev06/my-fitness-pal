package com.fmi.myfitnesspal.command.calorie;

import com.fmi.myfitnesspal.calorie.CalorieGoal;
import com.fmi.myfitnesspal.calorie.CalorieGoalCalculator;
import com.fmi.myfitnesspal.calorie.CalorieGoalHolder;
import com.fmi.myfitnesspal.calorie.CalorieGoalType;
import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.user.sex.Sex;

import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseSex;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseCalorieGoalType;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class SetCalorieGoalCommand implements ExecutableCommand {

    private static final String COMMAND_NAME = "set-calorie-goal";
    private static final int ARGUMENTS_COUNT = 2;

    private final CalorieGoalHolder calorieGoalHolder;

    public SetCalorieGoalCommand(CalorieGoalHolder calorieGoalHolder) {
        this.calorieGoalHolder = calorieGoalHolder;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        Sex sex = parseSex(arguments.get(0));
        CalorieGoalType goalType = parseCalorieGoalType(arguments.get(1));

        CalorieGoal calculatedGoal = CalorieGoalCalculator.calculateCalories(sex, goalType);
        calorieGoalHolder.setActiveCalorieGoal(calculatedGoal);

        return String.format("Calorie goal set to %d.",
                calculatedGoal.dailyCalorieTarget());
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <sex> <goalType>";
    }
}
