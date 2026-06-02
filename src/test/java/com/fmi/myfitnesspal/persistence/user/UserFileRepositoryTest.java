package com.fmi.myfitnesspal.persistence.user;

import com.fmi.myfitnesspal.persistence.PersistenceStoreFactory;
import com.fmi.myfitnesspal.persistence.file.ObjectPersistenceStore;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;
import com.fmi.myfitnesspal.user.PasswordHash;
import com.fmi.myfitnesspal.user.User;
import com.fmi.myfitnesspal.user.UserId;
import com.fmi.myfitnesspal.user.UserPool;
import com.fmi.myfitnesspal.user.UserProfile;
import com.fmi.myfitnesspal.user.country.Country;
import com.fmi.myfitnesspal.user.height.Height;
import com.fmi.myfitnesspal.user.height.LengthMeasurementUnit;
import com.fmi.myfitnesspal.user.sex.Sex;
import com.fmi.myfitnesspal.user.weight.Weight;
import com.fmi.myfitnesspal.user.weight.WeightMeasurementUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class UserFileRepositoryTest {

    private static final UserId USER_ID = new UserId("Ivan");
    private static final UserProfile USER_PROFILE = buildProfileFor(USER_ID);
    private static final UserProfileDto USER_PROFILE_DTO = buildDtoFor(USER_ID);
    private static final UserCredentialsDto IVAN_CREDENTIALS =
            new UserCredentialsDto("Ivan", PasswordHash.sentinel().value());

    @TempDir
    Path usersRootPath;

    @Mock
    private UserPool userPool;
    @Mock
    private UserProfileDtoMapper userProfileDtoMapper;
    @Mock
    private PersistenceStore<UserCredentialsDto> credentialsStore;
    @Mock
    private PersistenceStoreFactory storeFactory;
    @Mock
    private ObjectPersistenceStore<UserProfileDto> profileStore;

    private UserFileRepository userFileRepository;

    @BeforeEach
    void setUp() {
        userFileRepository = new UserFileRepository(
                userPool, userProfileDtoMapper, credentialsStore, storeFactory, usersRootPath);
    }

    @Test
    void testLoadInitialStateWithEmptyCredentialListAddsNothingToPool() {
        when(credentialsStore.load()).thenReturn(List.of());

        userFileRepository.loadInitialState();

        verifyNoInteractions(storeFactory);
        verifyNoInteractions(userProfileDtoMapper);
        verifyNoInteractions(userPool);
    }

    @Test
    void testLoadInitialStateWithSingleEntryLoadsAndAddsProfileToPool() {
        when(credentialsStore.load()).thenReturn(List.of(IVAN_CREDENTIALS));
        when(storeFactory.createObjectStore(any(), eq(UserProfileDto.class))).thenReturn(profileStore);
        when(profileStore.load()).thenReturn(Optional.of(USER_PROFILE_DTO));
        when(userProfileDtoMapper.toEntity(eq(USER_PROFILE_DTO), any(PasswordHash.class)))
                .thenReturn(USER_PROFILE);

        userFileRepository.loadInitialState();

        verify(userProfileDtoMapper).toEntity(eq(USER_PROFILE_DTO), any(PasswordHash.class));
        verify(userPool).addUser(USER_PROFILE);
    }

    @Test
    void testLoadInitialStateWithMultipleEntriesLoadsAndAddsEachProfileToPool() {
        UserId petarId = new UserId("Petar");
        UserCredentialsDto petarCredentials =
                new UserCredentialsDto("Petar", PasswordHash.sentinel().value());
        UserProfileDto petarDto = buildDtoFor(petarId);
        UserProfile petarProfile = buildProfileFor(petarId);

        when(credentialsStore.load()).thenReturn(List.of(IVAN_CREDENTIALS, petarCredentials));
        when(storeFactory.createObjectStore(any(), eq(UserProfileDto.class))).thenReturn(profileStore);
        when(profileStore.load())
                .thenReturn(Optional.of(USER_PROFILE_DTO))
                .thenReturn(Optional.of(petarDto));
        when(userProfileDtoMapper.toEntity(eq(USER_PROFILE_DTO), any(PasswordHash.class)))
                .thenReturn(USER_PROFILE);
        when(userProfileDtoMapper.toEntity(eq(petarDto), any(PasswordHash.class)))
                .thenReturn(petarProfile);

        userFileRepository.loadInitialState();

        verify(userPool).addUser(USER_PROFILE);
        verify(userPool).addUser(petarProfile);
    }

    @Test
    void testAddUserDelegatesToInnerPool() {
        when(storeFactory.createObjectStore(any(), eq(UserProfileDto.class))).thenReturn(profileStore);
        when(userProfileDtoMapper.toDto(USER_PROFILE)).thenReturn(USER_PROFILE_DTO);
        when(userPool.getAllUserProfiles()).thenReturn(List.of(USER_PROFILE));

        userFileRepository.addUser(USER_PROFILE);

        verify(userPool).addUser(USER_PROFILE);
    }

    @Test
    void testAddUserMapsAndPersistsProfileDtoToProfileStore() {
        when(storeFactory.createObjectStore(any(), eq(UserProfileDto.class))).thenReturn(profileStore);
        when(userProfileDtoMapper.toDto(USER_PROFILE)).thenReturn(USER_PROFILE_DTO);
        when(userPool.getAllUserProfiles()).thenReturn(List.of(USER_PROFILE));

        userFileRepository.addUser(USER_PROFILE);

        verify(userProfileDtoMapper).toDto(USER_PROFILE);
        verify(profileStore).save(USER_PROFILE_DTO);
    }

    @Test
    void testAddUserPersistsCurrentCredentialListToCredentialsStore() {
        when(storeFactory.createObjectStore(any(), eq(UserProfileDto.class))).thenReturn(profileStore);
        when(userProfileDtoMapper.toDto(USER_PROFILE)).thenReturn(USER_PROFILE_DTO);
        when(userPool.getAllUserProfiles()).thenReturn(List.of(USER_PROFILE));

        userFileRepository.addUser(USER_PROFILE);

        verify(credentialsStore).save(List.of(IVAN_CREDENTIALS));
    }

    @Test
    void testContainsDelegatesToInnerPool() {
        when(userPool.contains(USER_ID)).thenReturn(true);

        boolean result = userFileRepository.contains(USER_ID);

        assertEquals(true, result,
                "contains should delegate to the inner UserPool and return its result");
        verify(userPool).contains(USER_ID);
    }

    @Test
    void testFindByIdDelegatesToInnerPoolAndReturnsResult() {
        when(userPool.findById(USER_ID)).thenReturn(Optional.of(USER_PROFILE));

        Optional<UserProfile> result = userFileRepository.findById(USER_ID);

        assertEquals(Optional.of(USER_PROFILE), result,
                "findById should return the profile delegated from the inner UserPool");
        verify(userPool).findById(USER_ID);
    }

    @Test
    void testGetAllUserProfilesDelegatesToInnerPool() {
        List<UserProfile> expectedProfiles = List.of(USER_PROFILE);
        when(userPool.getAllUserProfiles()).thenReturn(expectedProfiles);

        List<UserProfile> resultProfiles = userFileRepository.getAllUserProfiles();

        assertEquals(expectedProfiles, resultProfiles,
                "getAllUserProfiles should return the list delegated from the inner UserPool");
        verify(userPool).getAllUserProfiles();
    }

    private static UserProfile buildProfileFor(UserId userId) {
        User user = new User(
                new Height(170, LengthMeasurementUnit.CENTIMETER),
                new Weight(70, WeightMeasurementUnit.KILOGRAM),
                22, Sex.MALE, Country.BULGARIA);
        return new UserProfile(userId, user, PasswordHash.sentinel());
    }

    private static UserProfileDto buildDtoFor(UserId userId) {
        User user = new User(
                new Height(170, LengthMeasurementUnit.CENTIMETER),
                new Weight(70, WeightMeasurementUnit.KILOGRAM),
                22, Sex.MALE, Country.BULGARIA);
        return new UserProfileDto(userId, user);
    }
}
