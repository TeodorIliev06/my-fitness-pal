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
public final class ImportCommandTest {

    @Mock
    private DataTransferable foodDiaryTransferMock;
    @Mock
    private DataTransferable waterDiaryTransferMock;

    private ImportCommand importCommand;

    @BeforeEach
    void setUp() {
        importCommand = new ImportCommand(List.of(foodDiaryTransferMock, waterDiaryTransferMock));
    }

    @Test
    void testExecuteWithNoArgumentsCallsImportOnAllTargets() throws InvalidCommandException {
        importCommand.execute(List.of());

        verify(foodDiaryTransferMock).importFromUserFiles();
        verify(waterDiaryTransferMock).importFromUserFiles();
    }

    @Test
    void testExecuteWithNoArgumentsReturnsSuccessMessage() throws InvalidCommandException {
        String result = importCommand.execute(List.of());

        assertEquals(GlobalConstants.SUCCESSFULLY_IMPORTED_MESSAGE, result,
                "execute must return the import success constant when no arguments are given");
    }

    @Test
    void testExecuteWithAnyArgumentsThrowsInvalidCommandException() {
        assertThrows(InvalidCommandException.class,
                () -> importCommand.execute(List.of("unexpected-arg")),
                "import takes no arguments; any argument must throw InvalidCommandException");
    }

    @Test
    void testExecuteWithAnyArgumentsDoesNotDelegateToTargets() {
        try {
            importCommand.execute(List.of("unexpected-arg"));
        } catch (InvalidCommandException ignored) {
        }

        verifyNoInteractions(foodDiaryTransferMock, waterDiaryTransferMock);
    }

    @Test
    void testExecuteWhenTargetThrowsPropagatesInvalidCommandException() throws InvalidCommandException {
        doThrow(new InvalidCommandException(GlobalConstants.IN_MEMORY_MODE_REQUIRED_MESSAGE))
                .when(foodDiaryTransferMock).importFromUserFiles();

        assertThrows(InvalidCommandException.class,
                () -> importCommand.execute(List.of()),
                "If any target throws InvalidCommandException the command must propagate it");
    }
}
