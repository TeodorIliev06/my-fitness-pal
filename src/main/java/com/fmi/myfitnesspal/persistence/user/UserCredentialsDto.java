package com.fmi.myfitnesspal.persistence.user;

public record UserCredentialsDto(String username, String passwordHash) {
}
