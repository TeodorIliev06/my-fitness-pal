package com.fmi.myfitnesspal.command.water;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.water.WaterDiary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
@ExtendWith(MockitoExtension.class)
public final class AddWaterTest {
    @Mock
    private WaterDiary diaryMock;
    private ExecutableCommand command;

    @BeforeEach
    void initialize() {
        command = new AddWaterCommand(diaryMock);
    }

    @Test
    void testExecuteValidCommand() throws InvalidCommandException {
        String message = command.execute(List.of("12.12.2012", "123"));
        assertEquals("Water added successfully!", message);
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
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("invalid-date", "123")),
                "The method should throw InvalidCommandException when the date is not in valid format");
    }

    @Test
    void testExecuteInvalidNumber() {
        assertThrows(InvalidCommandException.class, () -> command.execute(List.of("12.12.2012", "invalid-number")),
                "The method should throw InvalidCommandException when the number is not valid integer");
    }
}
