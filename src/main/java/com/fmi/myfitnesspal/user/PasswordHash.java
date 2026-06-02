package com.fmi.myfitnesspal.user;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordHash {

    private static final String SENTINEL_VALUE = "";

    private final String value;

    private PasswordHash(String value) {
        this.value = value;
    }

    public static PasswordHash of(String rawPassword) {
        return new PasswordHash(BCrypt.hashpw(rawPassword, BCrypt.gensalt()));
    }

    public static PasswordHash fromStored(String storedHash) {
        return new PasswordHash(storedHash);
    }

    public static PasswordHash sentinel() {
        return new PasswordHash(SENTINEL_VALUE);
    }

    public boolean matches(String rawPassword) {
        if (value.isEmpty()) {
            return false;
        }

        return BCrypt.checkpw(rawPassword, value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PasswordHash other)) {
            return false;
        }
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "[PROTECTED]";
    }
}
