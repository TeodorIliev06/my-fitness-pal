package com.fmi.myfitnesspal.user.sex;

public enum Sex {
    MALE(2400),
    FEMALE(2000);

    private final int baseBmr;

    Sex(int baseBmr) {
        this.baseBmr = baseBmr;
    }

    public int getBaseBmr() {
        return baseBmr;
    }
}
