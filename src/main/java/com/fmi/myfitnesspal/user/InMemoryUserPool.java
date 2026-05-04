package com.fmi.myfitnesspal.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryUserPool implements UserPool {

    private final Map<UserId, UserProfile> users;

    public InMemoryUserPool() {
        this.users = new HashMap<>();
    }

    @Override
    public void addUser(UserProfile userToAdd) {
        if (contains(userToAdd.userId())) {
            throw new IllegalArgumentException(
                    "User " + userToAdd.userId().username() + " already exists");
        }
        users.put(userToAdd.userId(), userToAdd);
    }

    @Override
    public boolean contains(UserId userId) {
        return users.containsKey(userId);
    }

    @Override
    public Optional<UserProfile> findById(UserId userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<UserProfile> getAllUserProfiles() {
        return List.copyOf(users.values());
    }
}
