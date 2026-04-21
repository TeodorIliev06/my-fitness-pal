package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.InMemoryFoodDiary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Meal;
import com.fmi.myfitnesspal.food.MealId;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.fmi.myfitnesspal.utility.DateHelper.DATE_FORMATTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class RemoveMealCommandTest {
    private RemoveMealCommand removeMealCommand;
    private InMemoryFoodDiary foodDiary;
    private LocalDate date;

    @BeforeEach
    public void setUp() {
        foodDiary = new InMemoryFoodDiary();
        removeMealCommand = new RemoveMealCommand(foodDiary);
        date = LocalDate.parse("12.03.2024", DATE_FORMATTER);

        Meal meal = new Meal(new MealId("Healthy Meal", "Veggie"));
        foodDiary.addMeal(date, EatingTime.BREAKFAST, meal);
    }

    @Test
    public void testExecuteWithValidArguments() throws InvalidCommandException {

        List<String> arguments = new ArrayList<>();
        arguments.add("12.03.2024");
        arguments.add("Breakfast");
        arguments.add("Healthy Meal");
        arguments.add("Veggie");

        assertEquals(GlobalConstants.SUCCESSFULLY_REMOVED_MEAL_MESSAGE, removeMealCommand.execute(arguments));
        assertTrue(foodDiary.getFoodsByDateAndEatingTime(date, EatingTime.BREAKFAST).isEmpty());
    }
}
