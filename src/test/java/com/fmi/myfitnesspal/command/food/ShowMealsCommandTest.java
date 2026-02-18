package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.utility.CommandUtilities;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.Meal;
import com.fmi.myfitnesspal.food.MealId;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.EatingTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public final class ShowMealsCommandTest {

    private ShowMealsCommand showMealsCommand;
    private FoodDiary diary;
    private LocalDate date;

    @BeforeEach
    public void setUp() {
        diary = new FoodDiary();
        showMealsCommand = new ShowMealsCommand(diary);
        date = LocalDate.parse("12.03.2024", CommandUtilities.DATE_FORMATTER);
    }

    @Test
    public void testExecuteWithValidArguments() throws InvalidCommandException {
        Meal meal = new Meal(new MealId("Breakfast Meal", "eggs with bread"));

        Food firstFood = Food.builder(new FoodId("Homemade", "Eggs"), 1, 1)
                        .build();

        Food secondFood = Food.builder(new FoodId("Homemade", "Bread"), 2, 2)
                        .build();

        meal.addFood(firstFood, 2.0);
        meal.addFood(secondFood, 2.0);

        diary.addMeal(date, EatingTime.BREAKFAST, meal);

        List<String> arguments = new ArrayList<>();
        arguments.add("12.03.2024");
        arguments.add("Breakfast");

        String expectedResult = meal.toString();

        assertEquals(expectedResult, showMealsCommand.execute(arguments));
    }

}
