package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.food.MealPool;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.MealId;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.ArrayList;
import java.util.List;

public final class CreateMealCommandTest {

    private CreateMealCommand createMealCommand;
    private MealPool mealPool;
    private FoodPool foodPool;

    @BeforeEach
    public void setUp() {
        mealPool = new MealPool();
        foodPool = new FoodPool();
        createMealCommand = new CreateMealCommand(mealPool, foodPool);

        Food firstFood = Food.builder(new FoodId("Apple", "Red"), 2, 2)
                .build();

        Food secondFood = Food.builder(new FoodId("Banana", "Yellow"), 2, 2)
                .build();

        foodPool.addFood(firstFood);
        foodPool.addFood(secondFood);
    }

    @Test
    public void testExecuteWithValidArguments() throws InvalidCommandException {
        List<String> arguments = new ArrayList<>();
        arguments.add("Healthy Meal");
        arguments.add("Veggie");
        arguments.add("Apple");
        arguments.add("Red");
        arguments.add("1");
        arguments.add("Banana");
        arguments.add("Yellow");
        arguments.add("2");

        MealId id = new MealId("Healthy Meal", "Veggie");
        assertEquals(GlobalConstants.SUCCESSFULLY_CREATED_MEAL_MESSAGE, createMealCommand.execute(arguments));
        assertNotNull(mealPool.getMeal(id));
        assertEquals(2, mealPool.getMeal(id).getFoods().size());
    }

    @Test
    public void testExecuteWithInvalidNumberOfArguments() {
        List<String> arguments = new ArrayList<>();
        arguments.add("Healthy Meal");
        arguments.add("Veggie");
        arguments.add("Apple");
        arguments.add("Red");
        arguments.add("1");
        arguments.add("Banana");
        arguments.add("Yellow");
        arguments.add("2");
        arguments.add("Extra");

        assertThrows(InvalidCommandException.class, () -> createMealCommand.execute(arguments));
    }
}

