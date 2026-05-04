package com.fmi.myfitnesspal.persistence.user;

import com.fmi.myfitnesspal.user.User;
import com.fmi.myfitnesspal.user.UserId;

public record UserProfileDto(UserId userId, User userData) {
}
