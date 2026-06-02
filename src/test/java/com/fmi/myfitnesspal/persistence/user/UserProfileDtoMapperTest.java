package com.fmi.myfitnesspal.persistence.user;

import com.fmi.myfitnesspal.user.PasswordHash;
import com.fmi.myfitnesspal.user.User;
import com.fmi.myfitnesspal.user.UserId;
import com.fmi.myfitnesspal.user.UserProfile;
import com.fmi.myfitnesspal.user.country.Country;
import com.fmi.myfitnesspal.user.height.Height;
import com.fmi.myfitnesspal.user.height.LengthMeasurementUnit;
import com.fmi.myfitnesspal.user.sex.Sex;
import com.fmi.myfitnesspal.user.weight.Weight;
import com.fmi.myfitnesspal.user.weight.WeightMeasurementUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class UserProfileDtoMapperTest {

    private static final UserId USER_ID = new UserId("Ivan");
    private static final Height HEIGHT = new Height(170, LengthMeasurementUnit.CENTIMETER);
    private static final Weight WEIGHT = new Weight(70, WeightMeasurementUnit.KILOGRAM);
    private static final int AGE = 22;
    private static final Sex SEX = Sex.MALE;
    private static final Country COUNTRY = Country.BULGARIA;

    private final UserProfileDtoMapper mapper = new UserProfileDtoMapper();

    @Test
    void testToDtoMapsUserIdCorrectly() {
        UserProfile userProfile = createUserProfile();

        UserProfileDto dto = mapper.toDto(userProfile);

        assertEquals(USER_ID.username(), dto.userId().username(),
                "toDto should map the UserId username field correctly");
    }

    @Test
    void testToDtoMapsUserDataCorrectly() {
        UserProfile userProfile = createUserProfile();

        UserProfileDto dto = mapper.toDto(userProfile);

        assertEquals(HEIGHT, dto.userData().height(),
                "toDto should map the user's height correctly");
        assertEquals(WEIGHT, dto.userData().weight(),
                "toDto should map the user's weight correctly");
        assertEquals(AGE, dto.userData().age(),
                "toDto should map the user's age correctly");
        assertEquals(SEX, dto.userData().sex(),
                "toDto should map the user's sex correctly");
        assertEquals(COUNTRY, dto.userData().country(),
                "toDto should map the user's country correctly");
    }

    @Test
    void testToDtoDoesNotPersistPasswordHashInDto() {
        UserProfile userProfile = createUserProfile();

        UserProfileDto dto = mapper.toDto(userProfile);

        assertEquals(2, UserProfileDto.class.getRecordComponents().length,
                "UserProfileDto must have exactly 2 components (userId, userData) — no password hash");
    }

    @Test
    void testToEntitySingleArgUsesSentinel() {
        UserProfileDto dto = createUserProfileDto();

        UserProfile entity = mapper.toEntity(dto);

        assertEquals(entity.passwordHash(), PasswordHash.sentinel(),
                "Single-arg toEntity should assign a sentinel hash (never matches any real password)");
    }

    @Test
    void testToEntityWithHashMapsUserIdCorrectly() {
        UserProfileDto dto = createUserProfileDto();
        PasswordHash hash = PasswordHash.sentinel();

        UserProfile entity = mapper.toEntity(dto, hash);

        assertEquals(USER_ID.username(), entity.userId().username(),
                "toEntity should map the UserId username field correctly");
    }

    @Test
    void testToEntityWithHashMapsUserDataCorrectly() {
        UserProfileDto dto = createUserProfileDto();
        PasswordHash hash = PasswordHash.sentinel();

        UserProfile entity = mapper.toEntity(dto, hash);

        assertEquals(HEIGHT, entity.userData().height(),
                "toEntity should map the user's height correctly");
        assertEquals(WEIGHT, entity.userData().weight(),
                "toEntity should map the user's weight correctly");
        assertEquals(AGE, entity.userData().age(),
                "toEntity should map the user's age correctly");
        assertEquals(SEX, entity.userData().sex(),
                "toEntity should map the user's sex correctly");
        assertEquals(COUNTRY, entity.userData().country(),
                "toEntity should map the user's country correctly");
    }

    @Test
    void testToEntityWithHashPreservesProvidedHash() {
        UserProfileDto dto = createUserProfileDto();
        PasswordHash expectedHash = PasswordHash.fromStored("$2a$10$storedTestHash");

        UserProfile entity = mapper.toEntity(dto, expectedHash);

        assertEquals(expectedHash, entity.passwordHash(),
                "toEntity(dto, hash) must set exactly the provided PasswordHash on the returned profile");
    }

    @Test
    void testToDtoAndToEntityAreSymmetric() {
        UserProfile original = createUserProfile();

        UserProfile roundTripped = mapper.toEntity(mapper.toDto(original));

        assertEquals(original.userId().username(), roundTripped.userId().username(),
                "A profile round-tripped through toDto then toEntity should preserve the username");
        assertEquals(original.userData(), roundTripped.userData(),
                "A profile round-tripped through toDto then toEntity should preserve all user data");
    }

    private static UserProfile createUserProfile() {
        User user = new User(HEIGHT, WEIGHT, AGE, SEX, COUNTRY);
        return new UserProfile(USER_ID, user, PasswordHash.sentinel());
    }

    private static UserProfileDto createUserProfileDto() {
        User user = new User(HEIGHT, WEIGHT, AGE, SEX, COUNTRY);
        return new UserProfileDto(USER_ID, user);
    }
}
