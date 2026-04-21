package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.InMemoryFoodDiary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.fmi.myfitnesspal.utility.DateHelper.DATE_FORMATTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class RemoveFoodCommandTest {

    private RemoveFoodCommand removeFoodCommand;
    private InMemoryFoodDiary foodDiary;
    private LocalDate date;

    @BeforeEach
    public void setUp() {
        foodDiary = new InMemoryFoodDiary();
        removeFoodCommand = new RemoveFoodCommand(foodDiary);
        date = LocalDate.parse("12.03.2024", DATE_FORMATTER);

        Food food = Food.builder(new FoodId("Apple", "Red"), 2, 2)
                .build();

        foodDiary.addFood(date, EatingTime.BREAKFAST, food, 1);
    }

    @Test
    public void testExecuteWithValidArguments() throws InvalidCommandException {

        List<String> arguments = new ArrayList<>();
        arguments.add("12.03.2024");
        arguments.add("Breakfast");
        arguments.add("Apple");
        arguments.add("Red");

        assertEquals(GlobalConstants.SUCCESSFULLY_REMOVED_FOOD_MESSAGE, removeFoodCommand.execute(arguments));
        assertTrue(foodDiary.getFoodsByDateAndEatingTime(date, EatingTime.BREAKFAST).isEmpty());
    }
}
