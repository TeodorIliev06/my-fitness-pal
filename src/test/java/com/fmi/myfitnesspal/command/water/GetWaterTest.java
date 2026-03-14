package com.fmi.myfitnesspal.command.water;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.water.WaterDiary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class GetWaterTest {
    @Mock
    private WaterDiary diaryMock;

    @InjectMocks
    private GetWaterCommand command;

    @Test
    void testExecuteValidCommand() throws InvalidCommandException {
        int quantity = 200;
        String date = "12.12.2012";
        when(diaryMock.getDailyWater(LocalDate.of(2012, 12, 12))).thenReturn(quantity);
        String message = command.execute(List.of(date));
        assertEquals("Water found for " + date + ": 200ml", message);
        verify(diaryMock).getDailyWater(LocalDate.of(2012, 12, 12));
    }

    @Test
    void testExecuteLessArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(new ArrayList<>()),
                "The method should throw InvalidCommandException when the count of the arguments is not 1");
        verify(diaryMock, never()).getDailyWater(any());
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("arg1", "arg2")),
                "The method should throw InvalidCommandException when the count of the arguments is not 1");
        verify(diaryMock, never()).getDailyWater(any());
    }

    @Test
    void testExecuteInvalidDate() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("invalid-date")),
                "The method should throw InvalidCommandException when the date is not in valid format");
        verify(diaryMock, never()).getDailyWater(any());
    }
}
