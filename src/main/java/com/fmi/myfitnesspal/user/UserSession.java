package com.fmi.myfitnesspal.user;

import java.util.List;
import java.util.Optional;

public final class UserSession {

    private UserProfile activeProfile;
    private final List<UserAware> userAwareComponents;

    public UserSession(List<UserAware> userAwareComponents) {
        this.userAwareComponents = List.copyOf(userAwareComponents);
    }

    public void switchTo(UserProfile newProfile) {
        this.activeProfile = newProfile;
        userAwareComponents.forEach(component -> component.onUserSwitched(newProfile));
    }

    public Optional<UserProfile> getActiveProfile() {
        return Optional.ofNullable(activeProfile);
    }
}
