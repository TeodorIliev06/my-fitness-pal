package com.fmi.myfitnesspal.command.water;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.water.WaterDiary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
public final class AddWaterTest {
    @Mock
    private WaterDiary diaryMock;

    @InjectMocks
    private AddWaterCommand command;

    @Test
    void testExecuteValidCommand() throws InvalidCommandException {
        String message = command.execute(List.of("12.12.2012", "123"));
        assertEquals("Water added successfully!", message);
        verify(diaryMock).addWater(LocalDate.of(2012, 12, 12), 123);
    }

    @Test
    void testExecuteLessArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("arg1")),
                "The method should throw InvalidCommandException when the count of the arguments is not 2");
        verify(diaryMock, never()).addWater(any(), anyInt());
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("arg1", "arg2", "arg3")),
                "The method should throw InvalidCommandException when the count of the arguments is not 2");
        verify(diaryMock, never()).addWater(any(), anyInt());
    }

    @Test
    void testExecuteInvalidDate() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("invalid-date", "123")),
                "The method should throw InvalidCommandException when the date is not in valid format");
        verify(diaryMock, never()).addWater(any(), anyInt());
    }

    @Test
    void testExecuteInvalidNumber() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("12.12.2012", "invalid-number")),
                "The method should throw InvalidCommandException when the number is not valid integer");
        verify(diaryMock, never()).addWater(any(), anyInt());
    }
}
