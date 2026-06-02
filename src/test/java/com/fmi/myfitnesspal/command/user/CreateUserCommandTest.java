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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class CreateUserCommandTest {

    private static final List<String> VALID_ARGUMENTS =
            List.of("Ivan", "password", "170", "CENTIMETER", "70", "KILOGRAM", "22", "MALE", "BULGARIA");

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
    void testExecuteHashesPasswordBeforeStoringUser() throws InvalidCommandException {
        when(userPool.contains(any(UserId.class))).thenReturn(false);

        command.execute(VALID_ARGUMENTS);

        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userPool).addUser(captor.capture());
        String storedHash = captor.getValue().passwordHash().value();
        assertNotEquals("password", storedHash,
                "The stored password hash must not equal the raw password");
        assertTrue(storedHash.startsWith("$2a$"),
                "The stored hash should be a BCrypt hash (starts with $2a$)");
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
                "Should throw when fewer than 9 arguments are provided");
    }

    @Test
    void testExecuteThrowsWhenArgumentListIsEmpty() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(Collections.emptyList()),
                "Should throw when no arguments are provided");
    }

    @Test
    void testExecuteThrowsWhenUsernameIsBlank() {
        List<String> blankUsernameArguments =
                List.of("   ", "password", "170", "CENTIMETER", "70", "KILOGRAM", "22", "MALE", "BULGARIA");

        assertThrows(InvalidCommandException.class,
                () -> command.execute(blankUsernameArguments),
                "Should throw when username argument is blank");
    }

    @Test
    void testExecuteThrowsWhenHeightUnitIsInvalid() {
        List<String> badHeightUnit =
                List.of("Ivan", "password", "170", "FURLONGS", "70", "KILOGRAM", "22", "MALE", "BULGARIA");

        assertThrows(InvalidCommandException.class,
                () -> command.execute(badHeightUnit),
                "Should throw InvalidCommandException for an unrecognised height unit");
    }

    @Test
    void testExecuteThrowsWhenWeightUnitIsInvalid() {
        List<String> badWeightUnit =
                List.of("Ivan", "password", "170", "CENTIMETER", "70", "TONNES", "22", "MALE", "BULGARIA");

        assertThrows(InvalidCommandException.class,
                () -> command.execute(badWeightUnit),
                "Should throw InvalidCommandException for an unrecognised weight unit");
    }

    @Test
    void testExecuteThrowsWhenCountryIsInvalid() {
        List<String> badCountry =
                List.of("Ivan", "password", "170", "CENTIMETER", "70", "KILOGRAM", "22", "MALE", "MARS");

        assertThrows(InvalidCommandException.class,
                () -> command.execute(badCountry),
                "Should throw InvalidCommandException for an unrecognised country");
    }
}
