package com.fmi.myfitnesspal.persistence.user;

import com.fmi.myfitnesspal.persistence.PersistenceStoreFactory;
import com.fmi.myfitnesspal.persistence.file.ObjectPersistenceStore;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;
import com.fmi.myfitnesspal.user.UserId;
import com.fmi.myfitnesspal.user.UserPool;
import com.fmi.myfitnesspal.user.UserProfile;
import com.fmi.myfitnesspal.user.PasswordHash;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public final class UserFileRepository implements UserPool {

    private static final String PROFILE_FILE_NAME = "profile.json";

    private final UserPool userPool;
    private final UserProfileDtoMapper userProfileDtoMapper;
    private final PersistenceStore<UserCredentialsDto> credentialsStore;
    private final PersistenceStoreFactory storeFactory;
    private final Path usersRootPath;

    public UserFileRepository(UserPool userPool,
                              UserProfileDtoMapper userProfileDtoMapper,
                              PersistenceStore<UserCredentialsDto> credentialsStore,
                              PersistenceStoreFactory storeFactory,
                              Path usersRootPath) {
        this.userPool = userPool;
        this.userProfileDtoMapper = userProfileDtoMapper;
        this.credentialsStore = credentialsStore;
        this.storeFactory = storeFactory;
        this.usersRootPath = usersRootPath;
    }

    void loadInitialState() {
        credentialsStore.load()
                .stream()
                .map(this::assembleProfileFrom)
                .forEach(userPool::addUser);
    }

    @Override
    public void addUser(UserProfile userToAdd) {
        userPool.addUser(userToAdd);

        persistProfile(userToAdd);
        persistCredentials();
    }

    @Override
    public boolean contains(UserId userId) {
        return userPool.contains(userId);
    }

    @Override
    public Optional<UserProfile> findById(UserId userId) {
        return userPool.findById(userId);
    }

    @Override
    public List<UserProfile> getAllUserProfiles() {
        return userPool.getAllUserProfiles();
    }

    // Combines the credentials entry (username + hash) with the per-user bio file.
    private UserProfile assembleProfileFrom(UserCredentialsDto credentials) {
        UserProfileDto bioDto = loadProfileFor(credentials.username());
        PasswordHash passwordHash = PasswordHash.fromStored(credentials.passwordHash());
        return userProfileDtoMapper.toEntity(bioDto, passwordHash);
    }

    private UserProfileDto loadProfileFor(String username) {
        return profileStoreFor(username)
                .load()
                .orElseThrow(() -> new IllegalStateException(
                        "Profile file missing for registered user: " + username));
    }

    private void persistProfile(UserProfile userProfile) {
        String username = userProfile.userId().username();
        createUserDirectory(usersRootPath.resolve(username));
        UserProfileDto dto = userProfileDtoMapper.toDto(userProfile);
        profileStoreFor(username).save(dto);
    }

    private void persistCredentials() {
        List<UserCredentialsDto> credentials = userPool.getAllUserProfiles()
                .stream()
                .map(profile -> new UserCredentialsDto(
                        profile.userId().username(),
                        profile.passwordHash().value()))
                .toList();

        credentialsStore.save(credentials);
    }

    // Creating a JsonObjectPersistence here is intentional: this repository
    // is the factory for per-user profile stores
    private ObjectPersistenceStore<UserProfileDto> profileStoreFor(String username) {
        Path profilePath = usersRootPath.resolve(username).resolve(PROFILE_FILE_NAME);
        return storeFactory.createObjectStore(profilePath, UserProfileDto.class);
    }

    private void createUserDirectory(Path userDirectory) {
        try {
            Files.createDirectories(userDirectory);
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Failed to create directory for user: " + userDirectory, e);
        }
    }
}
