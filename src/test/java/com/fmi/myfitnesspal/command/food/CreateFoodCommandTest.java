package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.ArrayList;
import java.util.List;

public final class CreateFoodCommandTest {

    private CreateFoodCommand createFoodCommand;
    private FoodPool foodPool;

    @BeforeEach
    public void setUp() {
        foodPool = new FoodPool();
        createFoodCommand = new CreateFoodCommand(foodPool);
    }

    @Test
    public void testExecuteWithValidArguments() throws InvalidCommandException {
        List<String> arguments = new ArrayList<>();
        arguments.add("Apple");
        arguments.add("Red");
        arguments.add("100");
        arguments.add("52");
        arguments.add("0.2");
        arguments.add("0.3");
        arguments.add("13.8");

        assertEquals(GlobalConstants.SUCCESSFULLY_CREATED_FOOD_MESSAGE, createFoodCommand.execute(arguments));

        assertNotNull(foodPool.getFood(new FoodId("Apple", "Red")));
    }

    @Test
    public void testExecuteWithInvalidArgumentsCount() {
        List<String> arguments = new ArrayList<>();
        arguments.add("Apple");
        arguments.add("Red");
        arguments.add("100");

        assertThrows(InvalidCommandException.class, () -> createFoodCommand.execute(arguments));
    }

    @Test
    public void testExecuteWithInvalidNumericValue() {
        List<String> arguments = new ArrayList<>();
        arguments.add("Banana");
        arguments.add("Yellow");
        arguments.add("100g");
        arguments.add("52");
        arguments.add("0.2");
        arguments.add("0.3");
        arguments.add("13.8");

        assertThrows(InvalidCommandException.class, () -> createFoodCommand.execute(arguments));
    }
}
