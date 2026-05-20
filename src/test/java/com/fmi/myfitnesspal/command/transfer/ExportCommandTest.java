package com.fmi.myfitnesspal.command.transfer;

import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.persistence.DataTransferable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public final class ExportCommandTest {

    @Mock
    private DataTransferable foodDiaryTransferMock;
    @Mock
    private DataTransferable waterDiaryTransferMock;

    private ExportCommand exportCommand;

    @BeforeEach
    void setUp() {
        exportCommand = new ExportCommand(List.of(foodDiaryTransferMock, waterDiaryTransferMock));
    }

    @Test
    void testExecuteWithNoArgumentsCallsExportOnAllTargets() throws InvalidCommandException {
        exportCommand.execute(List.of());

        verify(foodDiaryTransferMock).exportToUserFiles();
        verify(waterDiaryTransferMock).exportToUserFiles();
    }

    @Test
    void testExecuteWithNoArgumentsReturnsSuccessMessage() throws InvalidCommandException {
        String result = exportCommand.execute(List.of());

        assertEquals(GlobalConstants.SUCCESSFULLY_EXPORTED_MESSAGE, result,
                "execute must return the export success constant when no arguments are given");
    }

    @Test
    void testExecuteWithAnyArgumentsThrowsInvalidCommandException() {
        assertThrows(InvalidCommandException.class,
                () -> exportCommand.execute(List.of("unexpected-arg")),
                "export takes no arguments; any argument must throw InvalidCommandException");
    }

    @Test
    void testExecuteWithAnyArgumentsDoesNotDelegateToTargets() {
        try {
            exportCommand.execute(List.of("unexpected-arg"));
        } catch (InvalidCommandException ignored) {
        }

        verifyNoInteractions(foodDiaryTransferMock, waterDiaryTransferMock);
    }

    @Test
    void testExecuteWhenTargetThrowsPropagatesInvalidCommandException() throws InvalidCommandException {
        doThrow(new InvalidCommandException(GlobalConstants.IN_MEMORY_MODE_REQUIRED_MESSAGE))
                .when(foodDiaryTransferMock).exportToUserFiles();

        assertThrows(InvalidCommandException.class,
                () -> exportCommand.execute(List.of()),
                "If any target throws InvalidCommandException the command must propagate it");
    }
}
