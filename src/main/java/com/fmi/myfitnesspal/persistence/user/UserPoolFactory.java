package com.fmi.myfitnesspal.persistence.user;

import com.fmi.myfitnesspal.persistence.AbstractRepositoryFactory;
import com.fmi.myfitnesspal.persistence.PersistenceStoreFactory;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;
import com.fmi.myfitnesspal.user.InMemoryUserPool;
import com.fmi.myfitnesspal.user.UserPool;

import java.nio.file.Path;

public final class UserPoolFactory extends AbstractRepositoryFactory {

    private static final String USERS_FILE_NAME = "users.json";

    private final UserProfileDtoMapper userProfileDtoMapper;
    private final Path usersRootPath;

    public UserPoolFactory(boolean persistToFile,
                           PersistenceStoreFactory storeFactory,
                           UserProfileDtoMapper userProfileDtoMapper,
                           Path usersRootPath) {
        super(persistToFile, storeFactory);
        this.userProfileDtoMapper = userProfileDtoMapper;
        this.usersRootPath = usersRootPath;
    }

    public UserPool create() {
        return resolveRepository(
                InMemoryUserPool::new,
                this::buildPersistentUserPool
        );
    }

    private UserFileRepository buildPersistentUserPool() {
        Path usersFilePath = usersRootPath.resolve(USERS_FILE_NAME);
        PersistenceStore<String> usernamesStore =
                storeFactory.createListStore(usersFilePath, String.class);

        UserFileRepository repository = new UserFileRepository(
                new InMemoryUserPool(), userProfileDtoMapper, usernamesStore,
                storeFactory, usersRootPath);
        repository.loadInitialState();
        return repository;
    }
}
