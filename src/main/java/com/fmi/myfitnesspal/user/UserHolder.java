package com.fmi.myfitnesspal.user;

public final class UserHolder {

    private User user;

    public UserHolder() {
        this.user = null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
