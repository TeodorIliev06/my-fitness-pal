package com.fmi.myfitnesspal.food;

import java.util.Optional;

public final class Food {

    private final FoodId id;
    private final double servingSize;
    private final double calories;

    private final Optional<Double> fats;
    private final Optional<Double> protein;
    private final Optional<Double> carbs;

    public FoodId getId() {
        return this.id;
    }

    public double getServingSize() {
        return this.servingSize;
    }

    public double getCalories() {
        return this.calories;
    }

    public Optional<Double> getFats() {
        return this.fats;
    }

    public Optional<Double> getProtein() {
        return this.protein;
    }

    public Optional<Double> getCarbs() {
        return this.carbs;
    }

    public Food scaledBy(double servingsCount) {
        return Food.builder(this.id, this.servingSize * servingsCount, this.calories * servingsCount)
                .setFats(this.fats.map(fats -> fats * servingsCount))
                .setProtein(this.protein.map(protein -> protein * servingsCount))
                .setCarbs(this.carbs.map(carbs -> carbs * servingsCount))
                .build();
    }

    public static FoodBuilder builder(FoodId id, double servingSize, double calories) {
        return new FoodBuilder(id, servingSize, calories);
    }

    private Food(FoodBuilder builder) {
        this.id = builder.id;
        this.servingSize = builder.servingSize;
        this.calories = builder.calories;
        this.fats = builder.fats;
        this.protein = builder.protein;
        this.carbs = builder.carbs;
    }

    public static final class FoodBuilder {
        private final FoodId id;
        private final double servingSize;
        private final double calories;

        private Optional<Double> fats = Optional.empty();
        private Optional<Double> protein = Optional.empty();
        private Optional<Double> carbs = Optional.empty();

        private FoodBuilder(FoodId id, double servingSize, double calories) {
            this.id = id;
            this.servingSize = servingSize;
            this.calories = calories;
        }

        public FoodBuilder setFats(Optional<Double> fats) {
            this.fats = fats;
            return this;
        }

        public FoodBuilder setProtein(Optional<Double> protein) {
            this.protein = protein;
            return this;
        }

        public FoodBuilder setCarbs(Optional<Double> carbs) {
            this.carbs = carbs;
            return this;
        }

        public Food build() {
            return new Food(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Food [ Brand: ").append(id.brand())
                .append(", Description: ").append(id.description())
                .append(", Serving Size: ").append(String.format("%.2f", servingSize))
                .append(", Calories: ").append(String.format("%.2f", calories))
                .append(", Fats: ").append(fats.orElse(null))
                .append(", Protein: ").append(protein.orElse(null))
                .append(", Carbs: ").append(carbs.orElse(null))
                .append(" ]");

        return sb.toString();
    }
}
