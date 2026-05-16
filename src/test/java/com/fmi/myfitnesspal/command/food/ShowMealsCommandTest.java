package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.Meal;
import com.fmi.myfitnesspal.food.MealId;
import com.fmi.myfitnesspal.food.MealPool;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ShowMealsCommandTest {

    private Meal breakfastMeal;

    @Mock
    private MealPool mealPool;

    @InjectMocks
    private ShowMealsCommand showMealsCommand;

    @BeforeEach
    public void setUp() {
        Food eggs = Food.builder(new FoodId("Homemade", "Eggs"), 50, 70).build();
        breakfastMeal = new Meal(new MealId("Breakfast", "eggs with bread"));
        breakfastMeal.addFoodPortion(eggs, 2.0);
    }

    @Test
    public void testExecuteShowsAvailableMealTemplates() throws InvalidCommandException {
        when(mealPool.getAllMeals()).thenReturn(List.of(breakfastMeal));

        String result = showMealsCommand.execute(List.of());

        assertEquals(breakfastMeal.toString() + System.lineSeparator(), result,
                "ShowMealsCommand must display each available meal template from the pool");
    }

    @Test
    public void testExecuteWithNoMealsReturnsEmptyString() throws InvalidCommandException {
        when(mealPool.getAllMeals()).thenReturn(List.of());

        String result = showMealsCommand.execute(List.of());

        assertEquals("", result,
                "ShowMealsCommand must return an empty string when no meal templates exist in the pool");
    }
}
