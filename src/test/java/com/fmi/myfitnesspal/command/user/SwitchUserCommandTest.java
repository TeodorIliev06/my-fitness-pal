package com.fmi.myfitnesspal.command.user;

import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.user.PasswordHash;
import com.fmi.myfitnesspal.user.User;
import com.fmi.myfitnesspal.user.UserId;
import com.fmi.myfitnesspal.user.UserPool;
import com.fmi.myfitnesspal.user.UserProfile;
import com.fmi.myfitnesspal.user.UserSession;
import com.fmi.myfitnesspal.user.country.Country;
import com.fmi.myfitnesspal.user.height.Height;
import com.fmi.myfitnesspal.user.height.LengthMeasurementUnit;
import com.fmi.myfitnesspal.user.sex.Sex;
import com.fmi.myfitnesspal.user.weight.Weight;
import com.fmi.myfitnesspal.user.weight.WeightMeasurementUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class SwitchUserCommandTest {

    private static final String EXISTING_USERNAME = "Ivan";
    private static final String CORRECT_PASSWORD = "correctPassword";
    private static final String WRONG_PASSWORD = "wrongPassword";

    private static final PasswordHash IVAN_PASSWORD_HASH = PasswordHash.of(CORRECT_PASSWORD);
    private static final UserProfile IVAN_PROFILE = buildProfile(EXISTING_USERNAME);

    @Mock
    private UserPool userPool;

    @Mock
    private UserSession userSession;

    private SwitchUserCommand command;

    @BeforeEach
    void setUp() {
        command = new SwitchUserCommand(userPool, userSession);
    }

    @Test
    void testExecuteSwitchesSessionWhenUserExistsAndPasswordIsCorrect() throws InvalidCommandException {
        when(userPool.findById(new UserId(EXISTING_USERNAME))).thenReturn(Optional.of(IVAN_PROFILE));

        command.execute(List.of(EXISTING_USERNAME, CORRECT_PASSWORD));

        verify(userSession).switchTo(IVAN_PROFILE);
    }

    @Test
    void testExecuteReturnsYouAreNowMessageOnSuccess() throws InvalidCommandException {
        when(userPool.findById(new UserId(EXISTING_USERNAME))).thenReturn(Optional.of(IVAN_PROFILE));

        String result = command.execute(List.of(EXISTING_USERNAME, CORRECT_PASSWORD));

        assertEquals("You are now " + EXISTING_USERNAME, result,
                "Success message should follow the 'You are now <name>' format");
    }

    @Test
    void testExecuteThrowsWhenPasswordIsInvalid() {
        when(userPool.findById(new UserId(EXISTING_USERNAME))).thenReturn(Optional.of(IVAN_PROFILE));

        InvalidCommandException exception = assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(EXISTING_USERNAME, WRONG_PASSWORD)),
                "Should throw when the supplied correctPassword does not match the stored hash");

        assertEquals(GlobalConstants.INVALID_PASSWORD_MESSAGE, exception.getMessage(),
                "Exception message must equal the INVALID_PASSWORD_MESSAGE constant");
    }

    @Test
    void testExecuteDoesNotSwitchSessionWhenPasswordIsInvalid() {
        when(userPool.findById(new UserId(EXISTING_USERNAME))).thenReturn(Optional.of(IVAN_PROFILE));

        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of(EXISTING_USERNAME, WRONG_PASSWORD)));

        verifyNoInteractions(userSession);
    }

    @Test
    void testExecuteThrowsWhenUserDoesNotExist() {
        when(userPool.findById(any(UserId.class))).thenReturn(Optional.empty());

        InvalidCommandException exception = assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of("NonExistent", "anyPassword")),
                "Should throw when the requested username has not been registered");

        assertEquals("User NonExistent does not exist", exception.getMessage(),
                "Exception message should identify the missing username");
    }

    @Test
    void testExecuteDoesNotSwitchSessionWhenUserNotFound() {
        when(userPool.findById(any(UserId.class))).thenReturn(Optional.empty());

        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of("NonExistent", "anyPassword")));

        verifyNoInteractions(userSession);
    }

    @Test
    void testExecuteThrowsWhenNoArgumentsProvided() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(Collections.emptyList()),
                "Should throw when both username and correctPassword arguments are missing");
    }

    @Test
    void testExecuteThrowsWhenOnlyUsernameIsProvided() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of("Ivan", "extra")),
                "Should throw when more than one argument is provided");
    }

    @Test
    void testName() {
        assertEquals("switch-user", command.name(),
    void testExecuteThrowsWhenTooManyArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of("Ivan", CORRECT_PASSWORD, "extra")),
                "Should throw when more than two arguments are provided");
    }

    private static UserProfile buildProfile(String username) {
        UserId userId = new UserId(username);
        Height height = new Height(170, LengthMeasurementUnit.CENTIMETER);
        Weight weight = new Weight(70, WeightMeasurementUnit.KILOGRAM);
        User user = new User(height, weight, 22, Sex.MALE, Country.BULGARIA);
        return new UserProfile(userId, user, IVAN_PASSWORD_HASH);
    }
}
