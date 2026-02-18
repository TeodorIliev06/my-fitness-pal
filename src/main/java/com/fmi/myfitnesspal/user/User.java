package com.fmi.myfitnesspal.user;

import com.fmi.myfitnesspal.user.activitylevel.ActivityLevel;
import com.fmi.myfitnesspal.user.country.Country;
import com.fmi.myfitnesspal.user.sex.Sex;
import com.fmi.myfitnesspal.user.goal.Goal;
import com.fmi.myfitnesspal.user.height.Height;
import com.fmi.myfitnesspal.user.weight.Weight;

import java.util.List;

public record User(
        Height height,
        Weight weight,
        int age,
        Sex sex,
        Country country,
        ActivityLevel activityLevel,
        Weight weightGoal,
        Weight weeklyWeightGoal,
        List<Goal> goals
) {
}
