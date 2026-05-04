package com.fmi.myfitnesspal.command.user;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class SwitchUserCommandTest {

    private static final String EXISTING_USERNAME = "Ivan";
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
    void testExecuteSwitchesSessionWhenUserExists() throws InvalidCommandException {
        when(userPool.findById(new UserId(EXISTING_USERNAME))).thenReturn(Optional.of(IVAN_PROFILE));

        command.execute(List.of(EXISTING_USERNAME));

        verify(userSession).switchTo(IVAN_PROFILE);
    }

    @Test
    void testExecuteReturnsYouAreNowMessage() throws InvalidCommandException {
        when(userPool.findById(new UserId(EXISTING_USERNAME))).thenReturn(Optional.of(IVAN_PROFILE));

        String result = command.execute(List.of(EXISTING_USERNAME));

        assertEquals("You are now " + EXISTING_USERNAME, result,
                "Success message should follow the 'You are now <name>' format");
    }

    @Test
    void testExecuteThrowsWhenUserDoesNotExist() {
        when(userPool.findById(any(UserId.class))).thenReturn(Optional.empty());

        InvalidCommandException exception = assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of("NonExistent")),
                "Should throw when user has not been created via create-user");

        assertEquals("User NonExistent does not exist", exception.getMessage(),
                "Exception message should identify the missing username");
    }

    @Test
    void testExecuteDoesNotSwitchSessionWhenUserNotFound() {
        when(userPool.findById(any(UserId.class))).thenReturn(Optional.empty());

        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of("NonExistent")));

        verifyNoInteractions(userSession);
    }

    @Test
    void testExecuteThrowsWhenNoArgumentsProvided() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(Collections.emptyList()),
                "Should throw when username argument is missing");
    }

    @Test
    void testExecuteThrowsWhenTooManyArguments() {
        assertThrows(InvalidCommandException.class,
                () -> command.execute(List.of("Ivan", "extra")),
                "Should throw when more than one argument is provided");
    }

    @Test
    void testName() {
        assertEquals("switch-user", command.name(),
                "Command name must match the CLI-facing string");
    }

    @Test
    void testGetHelp() {
        String help = command.getHelp();
        assertTrue(help.contains("switch-user"),
                "Help text should include the command name");
    }

    private static UserProfile buildProfile(String username) {
        UserId userId = new UserId(username);
        Height height = new Height(170, LengthMeasurementUnit.CENTIMETER);
        Weight weight = new Weight(70, WeightMeasurementUnit.KILOGRAM);
        User user = new User(height, weight, 22, Sex.MALE, Country.BULGARIA);
        return new UserProfile(userId, user);
    }
}
