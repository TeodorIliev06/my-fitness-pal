package com.fmi.myfitnesspal.user;

import com.fmi.myfitnesspal.user.country.Country;
import com.fmi.myfitnesspal.user.height.Height;
import com.fmi.myfitnesspal.user.height.LengthMeasurementUnit;
import com.fmi.myfitnesspal.user.sex.Sex;
import com.fmi.myfitnesspal.user.weight.Weight;
import com.fmi.myfitnesspal.user.weight.WeightMeasurementUnit;

public final class GuestUserProfileFactory {

    private static final String GUEST_USERNAME = "Guest";
    private static final int GUEST_HEIGHT_CM = 170;
    private static final int GUEST_WEIGHT_KG = 70;
    private static final int GUEST_AGE = 30;
    private static final Sex GUEST_SEX = Sex.MALE;
    private static final Country GUEST_COUNTRY = Country.BULGARIA;

    public UserProfile create() {
        UserId guestId = new UserId(GUEST_USERNAME);
        Height height = new Height(GUEST_HEIGHT_CM, LengthMeasurementUnit.CENTIMETER);
        Weight weight = new Weight(GUEST_WEIGHT_KG, WeightMeasurementUnit.KILOGRAM);
        User guestUser = new User(height, weight, GUEST_AGE, GUEST_SEX, GUEST_COUNTRY);
        return new UserProfile(guestId, guestUser, PasswordHash.sentinel());
    }
}
