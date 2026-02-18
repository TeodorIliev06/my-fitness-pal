package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.command.utility.CommandUtilities;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class ShowFoodsCommandTest {

    private ShowFoodsCommand showFoodsCommand;
    private FoodDiary diary;
    private LocalDate date;

    @BeforeEach
    public void setUp() {
        diary = new FoodDiary();
        showFoodsCommand = new ShowFoodsCommand(diary);
        date = LocalDate.parse("12.03.2024", CommandUtilities.DATE_FORMATTER);
    }

    @Test
    public void testExecuteWithValidArguments() throws InvalidCommandException {

        Food food = Food.builder(new FoodId("Apple", "Red"), 2, 2)
                .build();

        diary.addFood(date, EatingTime.BREAKFAST, food, 2);

        List<String> arguments = new ArrayList<>();
        arguments.add("12.03.2024");
        arguments.add("Breakfast");

        String expectedResult = "Food [ Brand: Apple, "
                + "Description: Red, "
                + "Serving Size: " + String.format("%.2f", 4d)
                + ", Calories: " + String.format("%.2f", 4d)
                + ", Fats: null, Protein: null, Carbs: null ]";

        assertEquals(expectedResult, showFoodsCommand.execute(arguments).trim());
    }
}
