package com.fmi.myfitnesspal.user;

import com.fmi.myfitnesspal.user.country.Country;
import com.fmi.myfitnesspal.user.height.Height;
import com.fmi.myfitnesspal.user.sex.Sex;
import com.fmi.myfitnesspal.user.weight.Weight;

public final class UserBuilder {
    private Height height;
    private Weight weight;
    private int age;
    private Sex sex;
    private Country country;

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

    public User build() {
        return new User(height, weight, age, sex, country);
    }
}
