package com.fmi.myfitnesspal.food;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class RecipeTest {

    private static final double DELTA = 0.001;
    private Food chickenBreast;
    private Food rice;

    @BeforeEach
    public void setUp() {
        chickenBreast = Food.builder(new FoodId("Brand", "Chicken Breast"), 100, 165)
                .setProtein(Optional.of(31.0))
                .setFats(Optional.of(3.6))
                .setCarbs(Optional.of(0.0))
                .build();

        rice = Food.builder(new FoodId("Brand", "White Rice"), 100, 130)
                .setProtein(Optional.of(2.7))
                .setFats(Optional.of(0.3))
                .setCarbs(Optional.of(28.0))
                .build();
    }

    @Test
    public void testToFoodPerServingCaloriesAreDividedByServings() {
        FoodPortion chickenPortion = new FoodPortion(chickenBreast, 2.0);
        FoodPortion ricePortion = new FoodPortion(rice, 1.5);
        Recipe recipe = new Recipe(new RecipeId("Meal Prep", "Weekly"), List.of(chickenPortion, ricePortion), 2);

        Food result = recipe.toFoodPerServing();

        double expectedCalories = (165 * 2.0 + 130 * 1.5) / 2;
        assertEquals(expectedCalories, result.getCalories(), DELTA,
                "Calories per serving must be (sum of scaled ingredients) / numberOfServings");
    }

    @Test
    public void testToFoodPerServingProteinAreDividedByServings() {
        FoodPortion chickenPortion = new FoodPortion(chickenBreast, 2.0);
        FoodPortion ricePortion = new FoodPortion(rice, 1.5);
        Recipe recipe = new Recipe(new RecipeId("Meal Prep", "Weekly"), List.of(chickenPortion, ricePortion), 2);

        Food result = recipe.toFoodPerServing();

        double expectedProtein = (31.0 * 2.0 + 2.7 * 1.5) / 2;
        assertTrue(result.getProtein().isPresent(), "Protein must be present when all ingredients have protein");
        assertEquals(expectedProtein, result.getProtein().get(), DELTA,
                "Protein per serving must be (sum of scaled protein) / numberOfServings");
    }

    @Test
    public void testToFoodPerServingFatsAreDividedByServings() {
        FoodPortion chickenPortion = new FoodPortion(chickenBreast, 2.0);
        FoodPortion ricePortion = new FoodPortion(rice, 1.5);
        Recipe recipe = new Recipe(new RecipeId("Meal Prep", "Weekly"), List.of(chickenPortion, ricePortion), 2);

        Food result = recipe.toFoodPerServing();

        double expectedFats = (3.6 * 2.0 + 0.3 * 1.5) / 2;
        assertTrue(result.getFats().isPresent(), "Fats must be present when all ingredients have fats");
        assertEquals(expectedFats, result.getFats().get(), DELTA,
                "Fats per serving must be (sum of scaled fats) / numberOfServings");
    }

    @Test
    public void testToFoodPerServingCarbsAreDividedByServings() {
        FoodPortion chickenPortion = new FoodPortion(chickenBreast, 2.0);
        FoodPortion ricePortion = new FoodPortion(rice, 1.5);
        Recipe recipe = new Recipe(new RecipeId("Meal Prep", "Weekly"), List.of(chickenPortion, ricePortion), 2);

        Food result = recipe.toFoodPerServing();

        double expectedCarbs = (0.0 * 2.0 + 28.0 * 1.5) / 2;
        assertTrue(result.getCarbs().isPresent(), "Carbs must be present when all ingredients have carbs");
        assertEquals(expectedCarbs, result.getCarbs().get(), DELTA,
                "Carbs per serving must be (sum of scaled carbs) / numberOfServings");
    }

    @Test
    public void testToFoodPerServingHasRecipeSyntheticFoodId() {
        Recipe recipe = new Recipe(
                new RecipeId("Pasta Bolognese", "Classic"),
                List.of(new FoodPortion(chickenBreast, 1.0)),
                1
        );

        Food result = recipe.toFoodPerServing();

        assertEquals("Recipe", result.getId().brand(),
                "Synthetic FoodId brand must be 'Recipe'");
        assertEquals("Pasta Bolognese", result.getId().description(),
                "Synthetic FoodId description must equal the recipe name");
    }

    @Test
    public void testToFoodPerServingWithAbsentMacroRemainsAbsent() {
        Food noMacroFood = Food.builder(new FoodId("Plain", "Ingredient"), 50, 100).build();
        Recipe recipe = new Recipe(
                new RecipeId("Simple", "No Macros"),
                List.of(new FoodPortion(noMacroFood, 1.0)),
                1
        );

        Food result = recipe.toFoodPerServing();

        assertTrue(result.getFats().isEmpty(), "Fats must remain absent when no ingredient tracks fats");
        assertTrue(result.getProtein().isEmpty(), "Protein must remain absent when no ingredient tracks protein");
        assertTrue(result.getCarbs().isEmpty(), "Carbs must remain absent when no ingredient tracks carbs");
    }

    @Test
    public void testToFoodPerServingServingSizeIsDividedByServings() {
        FoodPortion chickenPortion = new FoodPortion(chickenBreast, 2.0);
        Recipe recipe = new Recipe(new RecipeId("Solo", "Chicken"), List.of(chickenPortion), 4);

        Food result = recipe.toFoodPerServing();

        double expectedServingSize = (100 * 2.0) / 4;
        assertEquals(expectedServingSize, result.getServingSize(), DELTA,
                "Serving size per portion must be total scaled size / numberOfServings");
    }
}
