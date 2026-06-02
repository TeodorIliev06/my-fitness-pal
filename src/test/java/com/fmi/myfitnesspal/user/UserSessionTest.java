package com.fmi.myfitnesspal.user;

import com.fmi.myfitnesspal.user.country.Country;
import com.fmi.myfitnesspal.user.height.Height;
import com.fmi.myfitnesspal.user.height.LengthMeasurementUnit;
import com.fmi.myfitnesspal.user.sex.Sex;
import com.fmi.myfitnesspal.user.weight.Weight;
import com.fmi.myfitnesspal.user.weight.WeightMeasurementUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public final class UserSessionTest {

    private static final UserProfile IVAN_PROFILE = createUserProfileFor("Ivan");
    private static final UserProfile PETAR_PROFILE = createUserProfileFor("Petar");

    @Mock
    private UserAware firstComponent;

    @Mock
    private UserAware secondComponent;

    @Test
    void testGetActiveProfileReturnsEmptyBeforeSwitching() {
        UserSession userSession = new UserSession(List.of());

        Optional<UserProfile> activeProfile = userSession.getActiveProfile();

        assertFalse(activeProfile.isPresent(),
                "getActiveProfile should return empty when no user has been switched to yet");
    }

    @Test
    void testSwitchToSetsActiveProfile() {
        UserSession userSession = new UserSession(List.of());

        userSession.switchTo(IVAN_PROFILE);

        assertEquals(Optional.of(IVAN_PROFILE), userSession.getActiveProfile(),
                "getActiveProfile should return the profile passed to switchTo");
    }

    @Test
    void testSwitchToOverridesPreviousActiveProfile() {
        UserSession userSession = new UserSession(List.of());
        userSession.switchTo(IVAN_PROFILE);

        userSession.switchTo(PETAR_PROFILE);

        assertEquals(Optional.of(PETAR_PROFILE), userSession.getActiveProfile(),
                "getActiveProfile should return the most recently switched-to profile");
    }

    @Test
    void testSwitchToNotifiesSingleUserAwareComponent() {
        UserSession userSession = new UserSession(List.of(firstComponent));

        userSession.switchTo(IVAN_PROFILE);

        verify(firstComponent).onUserSwitched(IVAN_PROFILE);
    }

    @Test
    void testSwitchToNotifiesAllUserAwareComponents() {
        UserSession userSession = new UserSession(List.of(firstComponent, secondComponent));

        userSession.switchTo(IVAN_PROFILE);

        verify(firstComponent).onUserSwitched(IVAN_PROFILE);
        verify(secondComponent).onUserSwitched(IVAN_PROFILE);
    }

    @Test
    void testGetActiveProfileReturnsPresentAfterSwitch() {
        UserSession userSession = new UserSession(List.of());
        userSession.switchTo(IVAN_PROFILE);

        Optional<UserProfile> activeProfile = userSession.getActiveProfile();

        assertTrue(activeProfile.isPresent(),
                "getActiveProfile should return a non-empty Optional after switchTo has been called");
    }

    private static UserProfile createUserProfileFor(String username) {
        UserId userId = new UserId(username);
        Height height = new Height(170, LengthMeasurementUnit.CENTIMETER);
        Weight weight = new Weight(70, WeightMeasurementUnit.KILOGRAM);
        User user = new User(height, weight, 22, Sex.MALE, Country.BULGARIA);
        return new UserProfile(userId, user, PasswordHash.sentinel());
    }
}
