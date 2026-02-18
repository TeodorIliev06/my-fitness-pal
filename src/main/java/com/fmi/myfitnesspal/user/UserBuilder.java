package com.fmi.myfitnesspal.user;

import com.fmi.myfitnesspal.user.activitylevel.ActivityLevel;
import com.fmi.myfitnesspal.user.country.Country;
import com.fmi.myfitnesspal.user.goal.Goal;
import com.fmi.myfitnesspal.user.height.Height;
import com.fmi.myfitnesspal.user.sex.Sex;
import com.fmi.myfitnesspal.user.weight.Weight;

import java.util.List;


public final class UserBuilder {
    private Height height;
    private Weight weight;
    private int age;
    private Sex sex;
    private Country country;
    private ActivityLevel activityLevel;
    private Weight weightGoal;
    private Weight weeklyWeightGoal;
    private List<Goal> goals;

    public UserBuilder() {
    }

    public UserBuilder setHeight(Height height) {
        this.height = height;
        return this;
    }

    public UserBuilder setWeight(Weight weight) {
        this.weight = weight;
        return this;
    }

    public UserBuilder setAge(int age) {
        this.age = age;
        return this;
    }

    public UserBuilder setSex(Sex sex) {
        this.sex = sex;
        return this;
    }

    public UserBuilder setCountry(Country country) {
        this.country = country;
        return this;
    }

    public UserBuilder setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
        return this;
    }

    public UserBuilder setWeightGoal(Weight weightGoal) {
        this.weightGoal = weightGoal;
        return this;
    }

    public UserBuilder setWeeklyWeightGoal(Weight weeklyWeightGoal) {
        this.weeklyWeightGoal = weeklyWeightGoal;
        return this;
    }

    public UserBuilder setGoals(List<Goal> goals) {
        this.goals = goals;
        return this;
    }

    public User build() {
        return new User(height, weight, age, sex, country, activityLevel, weightGoal, weeklyWeightGoal, goals);
    }
}
