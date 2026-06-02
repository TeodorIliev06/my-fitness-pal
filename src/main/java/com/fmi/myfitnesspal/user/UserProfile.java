package com.fmi.myfitnesspal.user;

public record UserProfile(UserId userId, User userData, PasswordHash passwordHash) {
}
