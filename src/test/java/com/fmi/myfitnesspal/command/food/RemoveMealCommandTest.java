package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.Meal;
import com.fmi.myfitnesspal.food.MealId;
import com.fmi.myfitnesspal.food.MealPool;
import com.fmi.myfitnesspal.food.InMemoryMealPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class RemoveMealCommandTest {

    private RemoveMealCommand removeMealCommand;
    private MealPool mealPool;

    @BeforeEach
    public void setUp() {
        mealPool = new InMemoryMealPool();
        removeMealCommand = new RemoveMealCommand(mealPool);

        Food apple = Food.builder(new FoodId("Apple", "Green"), 100, 52).build();
        Meal healthyMeal = new Meal(new MealId("Healthy Meal", "Veggie"));
        healthyMeal.addFoodPortion(apple, 1.0);
        mealPool.addMeal(healthyMeal);
    }

    @Test
    public void testExecuteRemovesMealTemplateFromPool() throws InvalidCommandException {
        List<String> arguments = List.of("Healthy Meal", "Veggie");

        String result = removeMealCommand.execute(arguments);

        assertEquals(GlobalConstants.SUCCESSFULLY_REMOVED_MEAL_MESSAGE, result,
                "Execute must return the deletion success message");
        assertTrue(mealPool.getAllMeals().isEmpty(),
                "MealPool must be empty after the only template is deleted");
    }

    @Test
    public void testExecuteWithNonExistingMealThrows() {
        List<String> arguments = List.of("Ghost Meal", "Does Not Exist");

        assertThrows(IllegalArgumentException.class,
                () -> removeMealCommand.execute(arguments),
                "Deleting a meal that does not exist in the pool must throw IllegalArgumentException");
    }

    @Test
    public void testExecuteWithWrongArgumentCountThrows() {
        List<String> arguments = List.of("Healthy Meal");

        assertThrows(InvalidCommandException.class,
                () -> removeMealCommand.execute(arguments),
                "Execute must throw InvalidCommandException when the argument count is not exactly 2");
    }
}
