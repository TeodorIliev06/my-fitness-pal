package com.fmi.myfitnesspal.persistence.user;

import com.fmi.myfitnesspal.persistence.PersistenceStoreFactory;
import com.fmi.myfitnesspal.persistence.file.ObjectPersistenceStore;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;
import com.fmi.myfitnesspal.user.InMemoryUserPool;
import com.fmi.myfitnesspal.user.User;
import com.fmi.myfitnesspal.user.UserId;
import com.fmi.myfitnesspal.user.UserPool;
import com.fmi.myfitnesspal.user.UserProfile;
import com.fmi.myfitnesspal.user.PasswordHash;
import com.fmi.myfitnesspal.user.country.Country;
import com.fmi.myfitnesspal.user.height.Height;
import com.fmi.myfitnesspal.user.height.LengthMeasurementUnit;
import com.fmi.myfitnesspal.user.sex.Sex;
import com.fmi.myfitnesspal.user.weight.Weight;
import com.fmi.myfitnesspal.user.weight.WeightMeasurementUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class UserPoolFactoryTest {

    @TempDir
    Path tempDirectory;

    @Mock
    private PersistenceStoreFactory storeFactoryMock;
    @Mock
    private PersistenceStore<UserCredentialsDto> credentialsStoreMock;
    @Mock
    private ObjectPersistenceStore<UserProfileDto> profileStoreMock;

    @Test
    void testCreateWithoutPersistenceReturnsInMemoryUserPool() {
        UserPoolFactory factory = buildFactory(false);

        UserPool createdPool = factory.create();

        assertInstanceOf(InMemoryUserPool.class, createdPool,
                "create should return a plain InMemoryUserPool when file persistence is disabled");
    }

    @Test
    void testCreateWithoutPersistenceDoesNotCallPersistenceStoreFactory() {
        UserPoolFactory factory = buildFactory(false);

        factory.create();

        verifyNoInteractions(storeFactoryMock);
    }

    @Test
    void testCreateWithPersistenceReturnsUserFileRepository() {
        when(storeFactoryMock.createListStore(any(), eq(UserCredentialsDto.class))).thenReturn(credentialsStoreMock);
        when(credentialsStoreMock.load()).thenReturn(List.of());

        UserPoolFactory factory = buildFactory(true);

        UserPool createdPool = factory.create();

        assertInstanceOf(UserFileRepository.class, createdPool,
                "create should return a UserFileRepository when file persistence is enabled");
    }

    @Test
    void testCreateWithPersistenceAndNonExistingFileStartsWithEmptyPool() {
        when(storeFactoryMock.createListStore(any(), eq(UserCredentialsDto.class))).thenReturn(credentialsStoreMock);
        when(credentialsStoreMock.load()).thenReturn(List.of());

        UserPoolFactory factory = buildFactory(true);

        UserPool createdPool = factory.create();

        assertTrue(createdPool.getAllUserProfiles().isEmpty(),
                "The pool should be empty when no persisted file exists yet");
    }

    @Test
    void testCreateWithPersistenceReturnsCorrectPool() {
        // username list store is created on initialization
        when(storeFactoryMock.createListStore(any(), eq(UserCredentialsDto.class))).thenReturn(credentialsStoreMock);
        when(credentialsStoreMock.load()).thenReturn(List.of());

        when(storeFactoryMock.createObjectStore(any(), eq(UserProfileDto.class))).thenReturn(profileStoreMock);

        UserPoolFactory factory = buildFactory(true);
        UserPool createdPool = factory.create();
        UserProfile ivanProfile = buildUserProfile();

        createdPool.addUser(ivanProfile);

        Optional<UserProfile> found = createdPool.findById(new UserId("Ivan"));
        assertTrue(found.isPresent(),
                "The UserFileRepository returned by the factory should delegate findById correctly");
        assertEquals(ivanProfile, found.get(),
                "findById should return the profile that was added via addUser");
    }

    private UserPoolFactory buildFactory(boolean persistToFile) {
        return new UserPoolFactory(
                persistToFile,
                storeFactoryMock,
                new UserProfileDtoMapper(),
                tempDirectory
        );
    }

    private static UserProfile buildUserProfile() {
        UserId userId = new UserId("Ivan");
        User user = new User(
                new Height(170, LengthMeasurementUnit.CENTIMETER),
                new Weight(70, WeightMeasurementUnit.KILOGRAM),
                22, Sex.MALE, Country.BULGARIA);
        return new UserProfile(userId, user, PasswordHash.sentinel());
    }
}
