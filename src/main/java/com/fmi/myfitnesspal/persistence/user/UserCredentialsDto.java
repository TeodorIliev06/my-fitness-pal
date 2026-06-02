package com.fmi.myfitnesspal.persistence.user;

/**
 * DTO for a single entry in users.json.
 *
 * Intentionally separate from UserProfileDto (bio data in profile.json):
 * credentials and biographical data have different reasons to change (SRP).
 */
public record UserCredentialsDto(String username, String passwordHash) {
}
