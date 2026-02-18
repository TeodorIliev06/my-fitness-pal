package com.fmi.myfitnesspal.water;

public enum Portion {
    P_250(250),
    P_500(500),
    P_1000(1000);

    private final int quantity;

    Portion(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }
}
