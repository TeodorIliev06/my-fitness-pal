package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.InMemoryFoodDiary;
import com.fmi.myfitnesspal.food.Recipe;
import com.fmi.myfitnesspal.food.RecipeId;
import com.fmi.myfitnesspal.food.FoodPortion;
import com.fmi.myfitnesspal.food.RecipePool;
import com.fmi.myfitnesspal.food.InMemoryRecipePool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.fmi.myfitnesspal.utility.DateHelper.DATE_FORMATTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AddRecipeCommandTest {

    private static final String CONSUMPTION_DATE_STRING = "12.03.2024";
    private AddRecipeCommand addRecipeCommand;
    private InMemoryFoodDiary foodDiary;
    private RecipePool recipePool;
    private LocalDate consumptionDate;

    @BeforeEach
    public void setUp() {
        foodDiary = new InMemoryFoodDiary();
        recipePool = new InMemoryRecipePool();
        addRecipeCommand = new AddRecipeCommand(foodDiary, recipePool);
        consumptionDate = LocalDate.parse(CONSUMPTION_DATE_STRING, DATE_FORMATTER);

        Food chicken = Food.builder(new FoodId("Brand", "Chicken"), 100, 200).build();
        FoodPortion foodPortion = new FoodPortion(chicken, 1.0);
        Recipe recipe = new Recipe(new RecipeId("ChickenRice", "Classic"), List.of(foodPortion), 2);
        recipePool.addRecipe(recipe);
    }

    @Test
    public void testExecuteWithValidArgumentsReturnsSuccessMessage() throws InvalidCommandException {
        List<String> arguments = new ArrayList<>(List.of(
                CONSUMPTION_DATE_STRING, "Lunch", "ChickenRice", "Classic", "1.0"
        ));

        String result = addRecipeCommand.execute(arguments);

        assertEquals(GlobalConstants.SUCCESSFULLY_ADDED_RECIPE_MESSAGE, result,
                "execute must return the success constant when arguments are valid");
    }

    @Test
    public void testExecuteLogsOneFoodEntryIntoDiary() throws InvalidCommandException {
        List<String> arguments = new ArrayList<>(List.of(
                CONSUMPTION_DATE_STRING, "Lunch", "ChickenRice", "Classic", "1.0"
        ));

        addRecipeCommand.execute(arguments);

        List<Food> logged = foodDiary.getAllFoodsByDateAndEatingTime(consumptionDate, EatingTime.LUNCH);
        assertEquals(1, logged.size(),
                "One Food entry (the resolved recipe) must be logged into the diary");
    }

    @Test
    public void testExecuteScalesCaloriesByServingsToLog() throws InvalidCommandException {
        List<String> arguments = new ArrayList<>(List.of(
                CONSUMPTION_DATE_STRING, "Lunch", "ChickenRice", "Classic", "3.0"
        ));

        addRecipeCommand.execute(arguments);

        List<Food> logged = foodDiary.getAllFoodsByDateAndEatingTime(consumptionDate, EatingTime.LUNCH);
        double expectedCalories = (200.0 / 2) * 3.0;
        assertEquals(expectedCalories, logged.get(0).getCalories(), 0.001,
                "Logged food calories must equal perServing calories scaled by servingsToLog");
    }

    @Test
    public void testExecuteWithInvalidArgumentsCountThrows() {
        List<String> arguments = new ArrayList<>(List.of(
                CONSUMPTION_DATE_STRING, "Lunch", "ChickenRice", "Classic"
        ));

        assertThrows(InvalidCommandException.class, () -> addRecipeCommand.execute(arguments),
                "Fewer than 5 arguments must throw InvalidCommandException");
    }

    @Test
    public void testExecuteWithInvalidEatingTimeThrows() {
        List<String> arguments = new ArrayList<>(List.of(
                CONSUMPTION_DATE_STRING, "Brunch", "ChickenRice", "Classic", "1.0"
        ));

        assertThrows(InvalidCommandException.class, () -> addRecipeCommand.execute(arguments),
                "An unrecognised eating time must throw InvalidCommandException");
    }

    @Test
    public void testExecuteWithNonExistentRecipeThrows() {
        List<String> arguments = new ArrayList<>(List.of(
                CONSUMPTION_DATE_STRING, "Lunch", "Ghost", "Recipe", "1.0"
        ));

        assertThrows(RuntimeException.class, () -> addRecipeCommand.execute(arguments),
                "A recipe absent from the pool must propagate an exception");
    }
}
