package com.fmi.myfitnesspal.command.utility;

import com.fmi.myfitnesspal.calorie.CalorieGoalType;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.food.FoodPortion;
import com.fmi.myfitnesspal.user.UserId;
import com.fmi.myfitnesspal.user.country.Country;
import com.fmi.myfitnesspal.user.height.LengthMeasurementUnit;
import com.fmi.myfitnesspal.user.sex.Sex;
import com.fmi.myfitnesspal.user.weight.WeightMeasurementUnit;
import com.fmi.myfitnesspal.water.Portion;
import com.fmi.myfitnesspal.exception.InvalidCommandException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.fmi.myfitnesspal.utility.NumberParser.parseDouble;

public final class CommandUtilities {

    private CommandUtilities() {
    }

    public static void validateArgumentsCount(List<String> arguments, int expectedCount)
            throws InvalidCommandException {
        if (arguments.size() != expectedCount) {
            throw new InvalidCommandException("Command expected to be with " + expectedCount + " arguments");
        }
    }

    public static void validateArgumentsCount(List<String> arguments, Predicate<List<String>> validator)
            throws InvalidCommandException {
        if (!validator.test(arguments)) {
            throw new InvalidCommandException(GlobalConstants.NOT_VALID_ARGUMENTS_COUNT_MESSAGE);
        }
    }

    public static void validatePositiveValues(int... arguments)
        throws InvalidCommandException {
        for (int number : arguments) {
            if (number < 0) {
                throw new InvalidCommandException(GlobalConstants.NOT_VALID_ARGUMENTS_NEGATIVE_VALUE_MESSAGE);
            }
        }
    }

    public static String formatItemList(List<?> items) {
        StringBuilder result = new StringBuilder();

        for (Object item : items) {
            result.append(item.toString());
            result.append(System.lineSeparator());
        }

        return result.toString();
    }

    public static Portion parsePortion(String portion) throws InvalidCommandException {
        try {
            return Portion.valueOf(portion);
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("The water portion does not exist", e);
        }
    }

    public static EatingTime parseEatingTime(String eatingTime) throws InvalidCommandException {
        try {
            return EatingTime.valueOf(eatingTime.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException(GlobalConstants.NOT_EXISTING_EATING_TIME_MESSAGE, e);
        }
    }

    public static Sex parseSex(String argument) throws InvalidCommandException {
        try {
            return Sex.valueOf(argument.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("The sex provided does not exist", e);
        }
    }

    public static CalorieGoalType parseCalorieGoalType(String argument) throws InvalidCommandException {
        try {
            return CalorieGoalType.valueOf(argument.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("The calorie goal type provided does not exist", e);
        }
    }

    public static UserId parseUserId(String username) throws InvalidCommandException {
        try {
            return new UserId(username);
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException(GlobalConstants.BLANK_USERNAME_MESSAGE, e);
        }
    }

    public static LengthMeasurementUnit parseLengthMeasurementUnit(String argument)
            throws InvalidCommandException {
        try {
            return LengthMeasurementUnit.valueOf(argument.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException(GlobalConstants.NOT_EXISTING_LENGTH_UNIT_MESSAGE, e);
        }
    }

    public static WeightMeasurementUnit parseWeightMeasurementUnit(String argument)
            throws InvalidCommandException {
        try {
            return WeightMeasurementUnit.valueOf(argument.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException(GlobalConstants.NOT_EXISTING_WEIGHT_UNIT_MESSAGE, e);
        }
    }

    public static Country parseCountry(String argument) throws InvalidCommandException {
        try {
            return Country.valueOf(argument.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException(GlobalConstants.NOT_EXISTING_COUNTRY_MESSAGE, e);
        }
    }

    public static List<FoodPortion> parseFoodPortions(
            List<String> arguments, int startIndex, FoodPool foodPool) throws InvalidCommandException {
        List<FoodPortion> portions = new ArrayList<>();

        for (int i = startIndex; i < arguments.size(); i += 3) {
            FoodId foodId = new FoodId(arguments.get(i), arguments.get(i + 1));
            double servingsUsed = parseDouble(arguments.get(i + 2));
            Food targetFood = foodPool.getFood(foodId);
            portions.add(new FoodPortion(targetFood, servingsUsed));
        }

        return portions;
    }
}
