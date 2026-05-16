package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.InMemoryFoodDiary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.fmi.myfitnesspal.utility.DateHelper.DATE_FORMATTER;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ShowDailyFoodLogCommandTest {

    private static final String CONSUMPTION_DATE_STRING = "12.03.2024";
    private ShowDailyFoodLogCommand showDailyFoodLogCommand;
    private InMemoryFoodDiary foodDiary;

    @BeforeEach
    public void setUp() {
        foodDiary = new InMemoryFoodDiary();
        showDailyFoodLogCommand = new ShowDailyFoodLogCommand(foodDiary);
    }

    @Test
    public void testExecuteContainsAllEatingTimeLabels() throws InvalidCommandException {
        List<String> arguments = new ArrayList<>(List.of(CONSUMPTION_DATE_STRING));

        String result = showDailyFoodLogCommand.execute(arguments);

        for (EatingTime eatingTime : EatingTime.values()) {
            assertTrue(result.contains(eatingTime.getLabel()),
                    "Output must contain the label for eating time: " + eatingTime.getLabel());
        }
    }

    @Test
    public void testExecuteWithLoggedFoodShowsFoodInCorrectSection() throws InvalidCommandException {
        Food apple = Food.builder(new FoodId("Brand", "Apple"), 100, 52).build();
        foodDiary.addFood(
                java.time.LocalDate.parse(CONSUMPTION_DATE_STRING, DATE_FORMATTER),
                EatingTime.BREAKFAST,
                apple,
                1.0
        );

        String result = showDailyFoodLogCommand.execute(new ArrayList<>(List.of(CONSUMPTION_DATE_STRING)));

        assertTrue(result.contains("Apple"),
                "Output must include the food name that was logged for the given date");
        int breakfastIndex = result.indexOf(EatingTime.BREAKFAST.getLabel());
        int lunchIndex = result.indexOf(EatingTime.LUNCH.getLabel());
        int appleIndex = result.indexOf("Apple");
        assertTrue(appleIndex > breakfastIndex && appleIndex < lunchIndex,
                "The logged food must appear under the Breakfast section, not another eating time");
    }

    @Test
    public void testExecuteWithNoArgumentsThrows() {
        assertThrows(InvalidCommandException.class,
                () -> showDailyFoodLogCommand.execute(new ArrayList<>()),
                "Missing the date argument must throw InvalidCommandException");
    }

    @Test
    public void testExecuteWithTooManyArgumentsThrows() {
        List<String> arguments = new ArrayList<>(List.of(CONSUMPTION_DATE_STRING, "extra"));

        assertThrows(InvalidCommandException.class, () -> showDailyFoodLogCommand.execute(arguments),
                "More than one argument must throw InvalidCommandException");
    }

    @Test
    public void testExecuteWithEmptyDiaryStillShowsAllSectionHeaders() throws InvalidCommandException {
        String result = showDailyFoodLogCommand.execute(new ArrayList<>(List.of(CONSUMPTION_DATE_STRING)));

        assertTrue(result.contains(EatingTime.BREAKFAST.getLabel()),
                "Breakfast section header must appear even when no food is logged");
        assertTrue(result.contains(EatingTime.DINNER.getLabel()),
                "Dinner section header must appear even when no food is logged");
    }
}
