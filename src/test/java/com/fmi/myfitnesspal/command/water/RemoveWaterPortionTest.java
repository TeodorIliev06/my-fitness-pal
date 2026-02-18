package com.fmi.myfitnesspal.command.water;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exception.WaterNotLoggedException;
import com.fmi.myfitnesspal.water.Portion;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public final class RemoveWaterPortionTest {
    @Mock
    private WaterDiary diaryMock;
    @InjectMocks
    private RemoveWaterPortionCommand command;

    @Test
    void testExecuteValidCommand() throws InvalidCommandException, WaterNotLoggedException {
        String message = command.execute(List.of("12.12.2012", "P_250"));
        assertEquals("Water removed successfully!", message);
        verify(diaryMock).removeWater(LocalDate.of(2012, 12, 12), Portion.P_250);
    }

    @Test
    void testExecuteLessArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("arg1")),
                "The method should throw InvalidCommandException when the count of the arguments is not 2");
    }

    @Test
    void testExecuteMoreArguments() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("arg1", "arg2", "arg3")),
                "The method should throw InvalidCommandException when the count of the arguments is not 2");
    }

    @Test
    void testExecuteInvalidDate() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("invalid-date", "P_250")),
                "The method should throw InvalidCommandException when the date is not in valid format");
    }

    @Test
    void testExecuteInvalidPortion() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("12.12.2012", "invalid-portion")),
                "The method should throw InvalidCommandException when the portion is not valid");
    }

    @Test
    void testExecuteWhenWaterNotLogged() throws WaterNotLoggedException, InvalidCommandException {
        doThrow(WaterNotLoggedException.class)
                .when(diaryMock).removeWater(LocalDate.parse("2012-12-12"), Portion.P_250);

        String message = command.execute(List.of("12.12.2012", "P_250"));
        assertEquals("No water intake is recorded for the given date", message);
    }
}
