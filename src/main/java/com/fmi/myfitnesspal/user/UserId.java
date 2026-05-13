package com.fmi.myfitnesspal.user;

public record UserId(String username) {

    public UserId {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be empty");
        }
    }
}
