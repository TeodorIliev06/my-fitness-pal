package com.fmi.myfitnesspal.user;

import java.util.List;
import java.util.Optional;

public interface UserPool {

    void addUser(UserProfile userToAdd);

    boolean contains(UserId userId);

    Optional<UserProfile> findById(UserId userId);

    List<UserProfile> getAllUserProfiles();
}
