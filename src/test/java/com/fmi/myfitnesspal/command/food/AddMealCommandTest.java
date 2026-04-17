package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.utility.CommandUtilities;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.InMemoryFoodDiary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.MealPool;
import com.fmi.myfitnesspal.food.MealId;
import com.fmi.myfitnesspal.food.Meal;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class AddMealCommandTest {

    private AddMealCommand addMealCommand;
    private InMemoryFoodDiary foodDiary;
    private MealPool mealPool;
    private LocalDate date;

    @BeforeEach
    public void setUp() {
        foodDiary = new InMemoryFoodDiary();
        mealPool = new MealPool();
        addMealCommand = new AddMealCommand(foodDiary, mealPool);
        date = LocalDate.parse("12.03.2024", CommandUtilities.DATE_FORMATTER);

        Meal meal = new Meal(new MealId("Healthy Meal", "Veggie"));
        mealPool.addMeal(meal);
    }

    @Test
    public void testExecuteWithValidArguments() throws InvalidCommandException {
        List<String> arguments = new ArrayList<>();
        arguments.add("12.03.2024");
        arguments.add("Lunch");
        arguments.add("Healthy Meal");
        arguments.add("Veggie");

        assertEquals(GlobalConstants.SUCCESSFULLY_ADDED_MEAL_MESSAGE, addMealCommand.execute(arguments));
        assertEquals(1, foodDiary.getMealsByDateAndEatingTime(date, EatingTime.LUNCH).size());
    }

    @Test
    public void testExecuteWithInvalidArgumentsCount() {
        List<String> arguments = new ArrayList<>();
        arguments.add("12.03.2024");
        arguments.add("Breakfast");
        arguments.add("Healthy Meal");

        assertThrows(InvalidCommandException.class, () -> addMealCommand.execute(arguments));
    }

    @Test
    public void testExecuteWithInvalidEatingTime() {
        List<String> arguments = new ArrayList<>();
        arguments.add("12.03.2024");
        arguments.add("InvalidTime");
        arguments.add("Healthy Meal");
        arguments.add("Veggie");

        assertThrows(InvalidCommandException.class, () -> addMealCommand.execute(arguments));
    }
}
