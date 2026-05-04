package com.fmi.myfitnesspal.persistence.user;

import com.fmi.myfitnesspal.persistence.file.JsonPersistence;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;
import com.fmi.myfitnesspal.user.InMemoryUserPool;
import com.fmi.myfitnesspal.user.UserPool;
import org.external.json.JsonConverter;

import java.nio.file.Path;

public final class UserPoolFactory {

    private static final String USERS_FILE_NAME = "users.json";

    private final boolean persistToFile;
    private final JsonConverter jsonConverter;
    private final UserProfileDtoMapper userProfileDtoMapper;
    private final Path usersRootPath;

    public UserPoolFactory(boolean persistToFile,
                           JsonConverter jsonConverter,
                           UserProfileDtoMapper userProfileDtoMapper,
                           Path usersRootPath) {
        this.persistToFile = persistToFile;
        this.jsonConverter = jsonConverter;
        this.userProfileDtoMapper = userProfileDtoMapper;
        this.usersRootPath = usersRootPath;
    }

    public UserPool create() {
        InMemoryUserPool inMemoryUserPool = new InMemoryUserPool();
        if (!persistToFile) {
            return inMemoryUserPool;
        }

        Path usersFilePath = usersRootPath.resolve(USERS_FILE_NAME);
        PersistenceStore<String> userNamesStore =
                new JsonPersistence<>(jsonConverter, usersFilePath, String.class);

        UserFileRepository repository = new UserFileRepository(
                inMemoryUserPool, userProfileDtoMapper, userNamesStore,
                jsonConverter, usersRootPath);
        repository.loadInitialState();
        return repository;
    }
}
