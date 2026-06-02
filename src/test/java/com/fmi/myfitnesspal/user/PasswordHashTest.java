package com.fmi.myfitnesspal.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PasswordHash.of() is called once as a static constant to keep per-test cost
 * limited to the single BCrypt.checkpw() call in matches().
 */
public final class PasswordHashTest {

    private static final String RAW_PASSWORD = "correctHorseBatteryStaple";
    private static final PasswordHash HASH = PasswordHash.of(RAW_PASSWORD);

    @Test
    void testOfProducesBcryptHash() {
        String value = HASH.value();

        assertTrue(value.startsWith("$2a$"),
                "PasswordHash.of() must produce a BCrypt hash starting with $2a$");
    }

    @Test
    void testOfDoesNotStoreRawPassword() {
        assertNotEquals(RAW_PASSWORD, HASH.value(),
                "The stored value must not equal the original raw password");
    }

    @Test
    void testMatchesReturnsTrueForCorrectPassword() {
        assertTrue(HASH.matches(RAW_PASSWORD),
                "matches() must return true when the correct raw password is supplied");
    }

    @Test
    void testMatchesReturnsFalseForWrongPassword() {
        assertFalse(HASH.matches("wrongPassword"),
                "matches() must return false when the wrong password is supplied");
    }

    @Test
    void testTwoHashesOfSamePasswordAreNotEqual() {
        PasswordHash second = PasswordHash.of(RAW_PASSWORD);

        assertNotEquals(HASH, second,
                "Two PasswordHash.of() calls for the same password must produce different stored values");
    }

    @Test
    void testFromStoredPreservesValue() {
        String storedValue = HASH.value();

        PasswordHash rehydrated = PasswordHash.fromStored(storedValue);

        assertEquals(storedValue, rehydrated.value(),
                "fromStored must preserve the exact hash string without re-hashing");
    }

    @Test
    void testFromStoredMatchesCorrectPassword() {
        PasswordHash rehydrated = PasswordHash.fromStored(HASH.value());

        assertTrue(rehydrated.matches(RAW_PASSWORD),
                "A rehydrated hash must still match the original raw password");
    }

    @Test
    void testSentinelNeverMatchesAnyPassword() {
        PasswordHash sentinel = PasswordHash.sentinel();

        assertFalse(sentinel.matches(RAW_PASSWORD),
                "Sentinel must not match a real password");
        assertFalse(sentinel.matches(""),
                "Sentinel must not match an empty string");
    }

    @Test
    void testSentinelValueIsEmpty() {
        assertEquals("", PasswordHash.sentinel().value(),
                "Sentinel value must be an empty string (used as the placeholder in users.json)");
    }

    @Test
    void testToStringDoesNotLeakHash() {
        String str = HASH.toString();

        assertFalse(str.contains(HASH.value()),
                "toString() must not expose the stored BCrypt hash");
        assertEquals("[PROTECTED]", str,
                "toString() should return a safe placeholder to prevent accidental log leakage");
    }

    @Test
    void testEqualsIsBasedOnStoredValue() {
        PasswordHash firstHash = PasswordHash.fromStored("$2a$10$same");
        PasswordHash secondHash = PasswordHash.fromStored("$2a$10$same");

        assertEquals(firstHash, secondHash,
                "Two PasswordHash objects with the same stored value must be equal");
    }

    @Test
    void testHashCodeIsConsistentWithEquals() {
        PasswordHash firstHash = PasswordHash.fromStored("$2a$10$same");
        PasswordHash secondHash = PasswordHash.fromStored("$2a$10$same");

        assertEquals(firstHash.hashCode(), secondHash.hashCode(),
                "Equal PasswordHash objects must have the same hashCode");
    }
}
