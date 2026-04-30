package com.fmi.myfitnesspal.persistence.user;

import com.fmi.myfitnesspal.user.UserProfile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class UserRegistry {
    private static final Path USERS_ROOT = Path.of("users");

    public boolean isRegistered(String username) {
        return Files.exists(resolveUserDataPath(username));
    }

    public void register(String username) {
        try {
            Files.createDirectories(resolveUserDataPath(username));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to create directory for user: " + username, e);
        }
    }

    public UserProfile load(String username) {
        return new UserProfile(username);
    }

    public Path resolveUserDataPath(String username) {
        return USERS_ROOT.resolve(username);
    }
}
