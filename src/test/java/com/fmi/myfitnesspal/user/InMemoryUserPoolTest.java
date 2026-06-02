package com.fmi.myfitnesspal.user;

import com.fmi.myfitnesspal.user.country.Country;
import com.fmi.myfitnesspal.user.height.Height;
import com.fmi.myfitnesspal.user.height.LengthMeasurementUnit;
import com.fmi.myfitnesspal.user.sex.Sex;
import com.fmi.myfitnesspal.user.weight.Weight;
import com.fmi.myfitnesspal.user.weight.WeightMeasurementUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class InMemoryUserPoolTest {

    private static final UserId USER_ID = new UserId("Ivan");
    private static final UserProfile USER_PROFILE = createUserProfileFor("Ivan");

    private InMemoryUserPool userPool;

    @BeforeEach
    void setUp() {
        userPool = new InMemoryUserPool();
    }

    @Test
    void testContainsReturnsFalseWhenUserNotAdded() {
        assertFalse(userPool.contains(USER_ID),
                "contains should return false for a user that was never added");
    }

    @Test
    void testContainsReturnsTrueWhenUserAdded() {
        userPool.addUser(USER_PROFILE);

        assertTrue(userPool.contains(USER_ID),
                "contains should return true after the user has been added");
    }

    @Test
    void testAddUserAddsUserToPool() {
        userPool.addUser(USER_PROFILE);

        Optional<UserProfile> found = userPool.findById(USER_ID);
        assertTrue(found.isPresent(),
                "findById should return the profile after it has been added");
        assertEquals(USER_PROFILE, found.get(),
                "findById should return exactly the profile that was added");
    }

    @Test
    void testAddUserThrowsWhenUserAlreadyExists() {
        userPool.addUser(USER_PROFILE);

        assertThrows(IllegalArgumentException.class,
                () -> userPool.addUser(USER_PROFILE),
                "addUser should throw when the same user is added a second time");
    }

    @Test
    void testFindByIdReturnsEmptyWhenUserNotFound() {
        Optional<UserProfile> result = userPool.findById(USER_ID);

        assertFalse(result.isPresent(),
                "findById should return an empty Optional for an unknown user");
    }

    @Test
    void testFindByIdReturnsProfileWhenFound() {
        userPool.addUser(USER_PROFILE);

        Optional<UserProfile> result = userPool.findById(USER_ID);

        assertEquals(Optional.of(USER_PROFILE), result,
                "findById should return the profile that matches the given UserId");
    }

    @Test
    void testGetAllUserProfilesReturnsEmptyListWhenNoUsersAdded() {
        List<UserProfile> profiles = userPool.getAllUserProfiles();

        assertTrue(profiles.isEmpty(),
                "getAllUserProfiles should return an empty list before any user is added");
    }

    @Test
    void testGetAllUserProfilesReturnsAllAddedUsers() {
        UserProfile petarProfile = createUserProfileFor("Petar");
        userPool.addUser(USER_PROFILE);
        userPool.addUser(petarProfile);

        List<UserProfile> profiles = userPool.getAllUserProfiles();

        assertEquals(2, profiles.size(),
                "getAllUserProfiles should return all profiles that have been added");
        assertTrue(profiles.contains(USER_PROFILE),
                "getAllUserProfiles should contain Ivan's profile");
        assertTrue(profiles.contains(petarProfile),
                "getAllUserProfiles should contain Petar's profile");
    }

    private static UserProfile createUserProfileFor(String username) {
        UserId userId = new UserId(username);
        Height height = new Height(170, LengthMeasurementUnit.CENTIMETER);
        Weight weight = new Weight(70, WeightMeasurementUnit.KILOGRAM);
        User user = new User(height, weight, 22, Sex.MALE, Country.BULGARIA);
        return new UserProfile(userId, user, PasswordHash.sentinel());
    }
}
