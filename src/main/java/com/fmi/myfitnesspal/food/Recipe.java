package com.fmi.myfitnesspal.food;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class Recipe {

    public static final String RECIPE_BRAND = "Recipe";

    private final RecipeId id;
    private final List<FoodPortion> foodPortions;
    private final int numberOfServings;

    public Recipe(RecipeId id, List<FoodPortion> foodPortions, int numberOfServings) {
        this.id = id;
        this.foodPortions = List.copyOf(foodPortions);
        this.numberOfServings = numberOfServings;
    }

    public RecipeId getId() {
        return this.id;
    }

    public List<FoodPortion> getIngredients() {
        return this.foodPortions;
    }

    public int getNumberOfServings() {
        return this.numberOfServings;
    }

    public Food toFoodPerServing() {
        List<Food> scaledIngredients = scaleIngredients();

        double totalCalories = sumCalories(scaledIngredients);
        double totalServingSize = sumServingSize(scaledIngredients);
        FoodId recipeFood = new FoodId(RECIPE_BRAND, this.id.name());

        return Food.builder(recipeFood, totalServingSize / numberOfServings, totalCalories / numberOfServings)
                .setFats(sumMacro(scaledIngredients, Food::getFats).map(f -> f / numberOfServings))
                .setProtein(sumMacro(scaledIngredients, Food::getProtein).map(p -> p / numberOfServings))
                .setCarbs(sumMacro(scaledIngredients, Food::getCarbs).map(c -> c / numberOfServings))
                .build();
    }

    private List<Food> scaleIngredients() {
        return this.foodPortions.stream()
                .map(ingredient -> ingredient.food().scaledBy(ingredient.servingsUsed()))
                .toList();
    }

    private double sumCalories(List<Food> scaledIngredients) {
        return scaledIngredients.stream().mapToDouble(Food::getCalories).sum();
    }

    private double sumServingSize(List<Food> scaledIngredients) {
        return scaledIngredients.stream().mapToDouble(Food::getServingSize).sum();
    }

    private Optional<Double> sumMacro(List<Food> scaledIngredients, Function<Food, Optional<Double>> extractor) {
        return scaledIngredients.stream()
                .map(extractor)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(Double::sum);
    }

    @Override
    public String toString() {
        Food perServing = toFoodPerServing();
        return "Recipe [ Name: " + id.name()
                + ", Description: " + id.description()
                + ", Servings: " + numberOfServings
                + ", Per Serving: " + perServing
                + " ]";
    }
}
