package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.fmi.myfitnesspal.utility.DateHelper.DATE_FORMATTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public final class RemoveFoodCommandTest {

    private static final LocalDate CONSUMPTION_DATE = LocalDate.parse("12.03.2024", DATE_FORMATTER);
    private static final FoodId APPLE_ID = new FoodId("Apple", "Red");

    @Mock
    private FoodDiary foodDiary;

    @InjectMocks
    private RemoveFoodCommand removeFoodCommand;

    @Test
    public void testExecuteWithValidArgumentsReturnsSuccessMessage() throws InvalidCommandException {
        List<String> arguments = List.of("12.03.2024", "Breakfast", "Apple", "Red");

        String result = removeFoodCommand.execute(arguments);

        assertEquals(GlobalConstants.SUCCESSFULLY_REMOVED_FOOD_MESSAGE, result,
                "execute must return the removal success constant when arguments are valid");
    }

    @Test
    public void testExecuteWithValidArgumentsDelegatesRemoveFoodToDiary() throws InvalidCommandException {
        List<String> arguments = List.of("12.03.2024", "Breakfast", "Apple", "Red");

        removeFoodCommand.execute(arguments);

        verify(foodDiary).removeFood(CONSUMPTION_DATE, EatingTime.BREAKFAST, APPLE_ID);
    }

    @Test
    public void testExecuteWithInvalidArgumentCountThrows() {
        List<String> arguments = List.of("12.03.2024", "Breakfast", "Apple");

        assertThrows(InvalidCommandException.class,
                () -> removeFoodCommand.execute(arguments),
                "Fewer than 4 arguments must throw InvalidCommandException");
    }
}
