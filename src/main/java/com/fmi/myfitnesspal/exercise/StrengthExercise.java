package com.fmi.myfitnesspal.exercise;

public final class StrengthExercise extends Exercise {
    private final int sets;
    private final int reps;
    private final int weight;

    public StrengthExercise(String name, int sets, int reps, int weight, int burnedCalories) {
        super(name, burnedCalories);
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    public int sets() {
        return sets;
    }

    public int reps() {
        return reps;
    }

    public int weight() {
        return weight;
    }

    @Override
    public String toString() {
        return "StrengthExercise{"
            + "name=" + super.name()
            + ", sets=" + sets
            + ", reps=" + reps
            + ", weight=" + weight
            + ", burnedCalories=" + super.burnedCalories()
            + '}';
    }
}
