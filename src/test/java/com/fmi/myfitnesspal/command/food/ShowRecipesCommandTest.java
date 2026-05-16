package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.FoodPortion;
import com.fmi.myfitnesspal.food.Recipe;
import com.fmi.myfitnesspal.food.RecipeId;
import com.fmi.myfitnesspal.food.RecipePool;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ShowRecipesCommandTest {

    private static final Recipe MOUSSAKA = createMoussaka();

    @Mock
    private RecipePool recipePool;

    @InjectMocks
    private ShowRecipesCommand showRecipesCommand;

    @Test
    public void testExecuteShowsAllRecipesInPool() throws InvalidCommandException {
        when(recipePool.getAllRecipes()).thenReturn(List.of(MOUSSAKA));

        String result = showRecipesCommand.execute(List.of());

        assertEquals(MOUSSAKA.toString() + System.lineSeparator(), result,
                "ShowRecipesCommand must display each available recipe from the pool");
    }

    @Test
    public void testExecuteWithNoRecipesReturnsEmptyString() throws InvalidCommandException {
        when(recipePool.getAllRecipes()).thenReturn(List.of());

        String result = showRecipesCommand.execute(List.of());

        assertEquals("", result,
                "ShowRecipesCommand must return an empty string when no recipes exist in the pool");
    }

    private static Recipe createMoussaka() {
        Food egg = Food.builder(new FoodId("Farm", "Egg"), 60, 78).build();
        return new Recipe(
                new RecipeId("Moussaka", "Traditional"),
                List.of(new FoodPortion(egg, 2.0)),
                4
        );
    }
}
