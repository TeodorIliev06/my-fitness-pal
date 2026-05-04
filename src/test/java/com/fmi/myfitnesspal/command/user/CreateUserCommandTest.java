package com.fmi.myfitnesspal.command.user;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.user.UserId;
import com.fmi.myfitnesspal.user.UserPool;
import com.fmi.myfitnesspal.user.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class CreateUserCommandTest {

    private static final List<String> VALID_ARGUMENTS =
            List.of("Ivan", "170", "CENTIMETER", "70", "KILOGRAM", "22", "MALE", "BULGARIA");

    @Mock
    private UserPool userPool;

    private CreateUserCommand command;

    @BeforeEach
    void setUp() {
        command = new CreateUserCommand(userPool);
    }

    @Test
    void testExecuteCreatesUserInPool() throws InvalidCommandException {
        when(userPool.contains(any(UserId.class))).thenReturn(false);

        command.execute(VALID_ARGUMENTS);

        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userPool).addUser(captor.capture());
        assertEquals("Ivan", captor.getValue().userId().username(),
                "addUser should be called with the correct username");
    }

    @Test
    void testExecuteReturnsSuccessMessage() throws InvalidCommandException {
        when(userPool.contains(any(UserId.class))).thenReturn(false);

        String result = command.execute(VALID_ARGUMENTS);

        assertEquals("User Ivan created", result,
                "Success message should follow the 'User <name> created' format");
    }

    @Test
    void testExecuteThrowsWhenUserAlreadyExists() {
        when(userPool.contains(any(UserId.class))).thenReturn(true);

        InvalidCommandException exception = assertThrows(InvalidCommandException.class,
                () -> command.execute(VALID_ARGUMENTS),
                "Should throw when user already exists in the pool");

        assertEquals("User Ivan already exists", exception.getMessage(),
                "Exception message should identify the duplicate username");
    }

    @Test
    void testExecuteThrowsWhenArgumentCountIsWrong() {
        List<String> tooFewArguments = List.of("Ivan", "22");

        assertThrows(InvalidCommandException.class,
                () -> command.execute(tooFewArguments),
                "Should throw when fewer than 8 arguments are provided");
    }

    @Test
    void testExecuteThrowsWhenArgumentListIsEmpty() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(Collections.emptyList()),
                "Should throw when no arguments are provided");
    }

    @Test
    void testName() {
        assertEquals("create-user", command.name(),
                "Command name must match the CLI-facing string");
    }

    @Test
    void testGetHelp() {
        String help = command.getHelp();
        assertTrue(help.contains("create-user"),
                "Help text should include the command name");
    }
}
