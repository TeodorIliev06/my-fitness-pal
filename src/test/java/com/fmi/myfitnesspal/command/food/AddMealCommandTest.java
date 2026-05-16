package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.InMemoryFoodDiary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.MealPool;
import com.fmi.myfitnesspal.food.InMemoryMealPool;
import com.fmi.myfitnesspal.food.MealId;
import com.fmi.myfitnesspal.food.Meal;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.fmi.myfitnesspal.utility.DateHelper.DATE_FORMATTER;
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
        mealPool = new InMemoryMealPool();
        addMealCommand = new AddMealCommand(foodDiary, mealPool);
        date = LocalDate.parse("12.03.2024", DATE_FORMATTER);

        Food apple = Food.builder(new FoodId("Apple", "Green"), 100, 52).build();
        Food banana = Food.builder(new FoodId("Banana", "Yellow"), 100, 89).build();

        Meal meal = new Meal(new MealId("Healthy Meal", "Veggie"));
        meal.addFoodPortion(apple, 1.0);
        meal.addFoodPortion(banana, 2.0);
        mealPool.addMeal(meal);
    }

    @Test
    public void testExecuteWithValidArguments() throws InvalidCommandException {
        List<String> arguments = new ArrayList<>();
        arguments.add("12.03.2024");
        arguments.add("Lunch");
        arguments.add("Healthy Meal");
        arguments.add("Veggie");

        assertEquals(GlobalConstants.SUCCESSFULLY_ADDED_MEAL_MESSAGE, addMealCommand.execute(arguments),
                "Execute must return success message");
        assertEquals(2, foodDiary.getFoodsByDateAndEatingTime(date, EatingTime.LUNCH).size(),
                "Each food portion must be logged as an individual food entry in the diary");
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
