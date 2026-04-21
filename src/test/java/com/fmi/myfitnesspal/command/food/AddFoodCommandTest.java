package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.InMemoryFoodDiary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.food.InMemoryFoodPool;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.fmi.myfitnesspal.utility.DateHelper.DATE_FORMATTER;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class AddFoodCommandTest {

    private AddFoodCommand addFoodCommand;
    private FoodDiary foodDiary;
    private FoodPool foodPool;
    private LocalDate date;

    @BeforeEach
    public void setUp() {
        foodDiary = new InMemoryFoodDiary();
        foodPool = new InMemoryFoodPool();
        addFoodCommand = new AddFoodCommand(foodDiary, foodPool);
        date = LocalDate.parse("12.03.2024", DATE_FORMATTER);

        Food food =  Food.builder(new FoodId("Apple", "Red"), 1, 10)
                .setCarbs(Optional.empty())
                .setFats(Optional.empty())
                .setProtein(Optional.empty())
                .build();

        foodPool.addFood(food);
    }

    @Test
    public void testExecuteWithValidArgumentsPassed() throws InvalidCommandException {
        List<String> arguments = new ArrayList<>();
        arguments.add("12.03.2024");
        arguments.add("Lunch");
        arguments.add("Apple");
        arguments.add("Red");
        arguments.add("1");

        assertEquals(GlobalConstants.SUCCESSFULLY_ADDED_FOOD_MESSAGE, addFoodCommand.execute(arguments));
        assertEquals(1, foodDiary.getFoodsByDateAndEatingTime(date, EatingTime.LUNCH).size());
    }

    @Test
    public void testExecuteWithInvalidArgumentsCount() {
        List<String> arguments = new ArrayList<>();
        arguments.add("12.03.2024");
        arguments.add("Lunch");
        arguments.add("Apple");
        arguments.add("Red");

        assertThrows(InvalidCommandException.class, () -> addFoodCommand.execute(arguments));
    }

    @Test
    public void testExecuteWithInvalidEatingTime() {
        List<String> arguments = new ArrayList<>();
        arguments.add("12.03.2024");
        arguments.add("InvalidTime");
        arguments.add("Apple");
        arguments.add("Red");
        arguments.add("1");

        assertThrows(InvalidCommandException.class, () -> addFoodCommand.execute(arguments));
    }

    @Test
    public void testExecuteWithInvalidNumberOfServings() {
        List<String> arguments = new ArrayList<>();
        arguments.add("12.03.2024");
        arguments.add("Dinner");
        arguments.add("Chicken");
        arguments.add("Grilled");
        arguments.add("Invalid");

        assertThrows(InvalidCommandException.class, () -> addFoodCommand.execute(arguments));
    }

    @Test
    public void testExecuteWithNegativeNumberOfServings() {
        List<String> arguments = new ArrayList<>();
        arguments.add("12.03.2024");
        arguments.add("Snack");
        arguments.add("Banana");
        arguments.add("Yellow");
        arguments.add("-1");

        assertThrows(InvalidCommandException.class, () -> addFoodCommand.execute(arguments));
    }
}
