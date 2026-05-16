package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.Meal;
import com.fmi.myfitnesspal.food.MealPool;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class CreateMealCommandTest {

    private static final FoodId APPLE_ID = new FoodId("Apple", "Red");
    private static final FoodId BANANA_ID = new FoodId("Banana", "Yellow");
    private static final Food APPLE = Food.builder(APPLE_ID, 2, 2).build();
    private static final Food BANANA = Food.builder(BANANA_ID, 2, 2).build();

    @Mock
    private MealPool mealPool;
    @Mock
    private FoodPool foodPool;

    @InjectMocks
    private CreateMealCommand createMealCommand;

    @Test
    public void testExecuteWithValidArgumentsReturnsSuccessMessage() throws InvalidCommandException {
        when(foodPool.getFood(APPLE_ID)).thenReturn(APPLE);
        when(foodPool.getFood(BANANA_ID)).thenReturn(BANANA);
        List<String> arguments = List.of(
                "Healthy Meal", "Veggie",
                "Apple", "Red", "1",
                "Banana", "Yellow", "2"
        );

        String result = createMealCommand.execute(arguments);

        assertEquals(GlobalConstants.SUCCESSFULLY_CREATED_MEAL_MESSAGE, result,
                "execute must return the creation success constant when arguments are valid");
    }

    @Test
    public void testExecuteWithValidArgumentsDelegatesAddMealToPool() throws InvalidCommandException {
        when(foodPool.getFood(APPLE_ID)).thenReturn(APPLE);
        when(foodPool.getFood(BANANA_ID)).thenReturn(BANANA);
        List<String> arguments = List.of(
                "Healthy Meal", "Veggie",
                "Apple", "Red", "1",
                "Banana", "Yellow", "2"
        );

        createMealCommand.execute(arguments);
        verify(mealPool).addMeal(any(Meal.class));
    }

    @Test
    public void testExecuteWithInvalidArgumentCountThrows() {
        List<String> arguments = List.of(
                "Healthy Meal", "Veggie",
                "Apple", "Red", "1",
                "Banana", "Yellow", "2", "Extra"
        );

        assertThrows(InvalidCommandException.class, () -> createMealCommand.execute(arguments),
                "An argument count that does not satisfy the triplet constraint must throw InvalidCommandException");
    }
}
