package com.fmi.myfitnesspal.user;

import com.fmi.myfitnesspal.user.country.Country;
import com.fmi.myfitnesspal.user.sex.Sex;
import com.fmi.myfitnesspal.user.height.Height;
import com.fmi.myfitnesspal.user.weight.Weight;

public record User(
        Height height,
        Weight weight,
        int age,
        Sex sex,
        Country country
) {
}
