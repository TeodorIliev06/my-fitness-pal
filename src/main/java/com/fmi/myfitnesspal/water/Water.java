package com.fmi.myfitnesspal.water;

public final class Water {
    private int quantity;

    public Water() {
        this(0);
    }

    public Water(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("The water quantity can not be negative");
        }
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void add(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("The water quantity can not be negative");
        }
        this.quantity += quantity;
    }

    public void add(Portion portion) {
        add(portion.getQuantity());
    }

    public void remove(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("The water quantity can not be negative");
        }
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("There is not enough water to remove");
        }
        this.quantity -= quantity;
    }

    public void remove(Portion portion) {
        remove(portion.getQuantity());
    }
}
