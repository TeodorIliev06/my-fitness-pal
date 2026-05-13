package com.fmi.myfitnesspal.persistence.user;

import com.fmi.myfitnesspal.persistence.PersistenceStoreFactory;
import com.fmi.myfitnesspal.persistence.file.ObjectPersistenceStore;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;
import com.fmi.myfitnesspal.user.UserId;
import com.fmi.myfitnesspal.user.UserPool;
import com.fmi.myfitnesspal.user.UserProfile;

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
    private final PersistenceStore<String> usernamesStore;
    private final PersistenceStoreFactory storeFactory;
    private final Path usersRootPath;

    public UserFileRepository(UserPool userPool,
                              UserProfileDtoMapper userProfileDtoMapper,
                              PersistenceStore<String> usernamesStore,
                              PersistenceStoreFactory storeFactory,
                              Path usersRootPath) {
        this.userPool = userPool;
        this.userProfileDtoMapper = userProfileDtoMapper;
        this.usernamesStore = usernamesStore;
        this.storeFactory = storeFactory;
        this.usersRootPath = usersRootPath;
    }

    void loadInitialState() {
        usernamesStore.load()
                .stream()
                .map(this::loadProfileFor)
                .map(userProfileDtoMapper::toEntity)
                .forEach(userPool::addUser);
    }

    @Override
    public void addUser(UserProfile userToAdd) {
        userPool.addUser(userToAdd);

        persistProfile(userToAdd);
        persistUsernames();
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

    private void persistUsernames() {
        List<String> usernames = userPool.getAllUserProfiles()
                .stream()
                .map(profile -> profile.userId().username())
                .toList();
        usernamesStore.save(usernames);
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
