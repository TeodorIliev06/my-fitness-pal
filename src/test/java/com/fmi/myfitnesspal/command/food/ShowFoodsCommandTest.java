package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.FoodId;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ShowFoodsCommandTest {

    private static final LocalDate CONSUMPTION_DATE = LocalDate.parse("12.03.2024", DATE_FORMATTER);

    private static final Food APPLE = Food.builder(new FoodId("Apple", "Red"), 2, 2).build();

    @Mock
    private FoodDiary foodDiary;

    @InjectMocks
    private ShowFoodsCommand showFoodsCommand;

    @Test
    public void testExecuteWithValidArgumentsReturnsFormattedFoodList() throws InvalidCommandException {
        when(foodDiary.getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.BREAKFAST))
                .thenReturn(List.of(APPLE));
        List<String> arguments = List.of("12.03.2024", "Breakfast");

        String result = showFoodsCommand.execute(arguments);

        assertEquals(APPLE.toString() + System.lineSeparator(), result,
                "execute must format each food returned by the diary with a trailing line separator");
    }

    @Test
    public void testExecuteWithNoFoodsForDateReturnsEmptyString() throws InvalidCommandException {
        when(foodDiary.getFoodsByDateAndEatingTime(CONSUMPTION_DATE, EatingTime.BREAKFAST))
                .thenReturn(List.of());
        List<String> arguments = List.of("12.03.2024", "Breakfast");

        String result = showFoodsCommand.execute(arguments);

        assertEquals("", result,
                "execute must return an empty string when no foods are logged for the given date and eating time");
    }

    @Test
    public void testExecuteWithInvalidArgumentCountThrows() {
        assertThrows(InvalidCommandException.class,
                () -> showFoodsCommand.execute(List.of("12.03.2024")),
                "execute must throw InvalidCommandException when argument count is not exactly 2");
    }
}
