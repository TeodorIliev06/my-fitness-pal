package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodPool;
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

@ExtendWith(MockitoExtension.class)
public final class CreateFoodCommandTest {

    @Mock
    private FoodPool foodPool;

    @InjectMocks
    private CreateFoodCommand createFoodCommand;

    @Test
    public void testExecuteWithValidArguments() throws InvalidCommandException {
        List<String> arguments = List.of("Apple", "Red", "100", "52", "0.2", "0.3", "13.8");

        String result = createFoodCommand.execute(arguments);

        assertEquals(GlobalConstants.SUCCESSFULLY_CREATED_FOOD_MESSAGE, result,
                "execute must return the creation success constant when all 7 arguments are valid");
    }

    @Test
    public void testExecuteWithValidArgumentsDelegatesAddFoodToPool() throws InvalidCommandException {
        List<String> arguments = List.of("Apple", "Red", "100", "52", "0.2", "0.3", "13.8");

        createFoodCommand.execute(arguments);

        verify(foodPool).addFood(any(Food.class));
    }

    @Test
    public void testExecuteWithInvalidArgumentsCountThrows() {
        List<String> arguments = List.of("Apple", "Red", "100");

        assertThrows(InvalidCommandException.class,
                () -> createFoodCommand.execute(arguments),
                "Fewer than 7 arguments must throw InvalidCommandException");
    }

    @Test
    public void testExecuteWithInvalidNumericValueThrows() {
        List<String> arguments = List.of("Banana", "Yellow", "100g", "52", "0.2", "0.3", "13.8");

        assertThrows(InvalidCommandException.class,
                () -> createFoodCommand.execute(arguments),
                "A non-numeric serving size must throw InvalidCommandException");
    }
}
