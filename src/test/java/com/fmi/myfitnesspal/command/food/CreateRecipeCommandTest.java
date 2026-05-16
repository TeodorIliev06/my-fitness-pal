package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.food.Recipe;
import com.fmi.myfitnesspal.food.RecipePool;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class CreateRecipeCommandTest {

    private static final FoodId CHICKEN_ID = new FoodId("Brand", "Chicken");
    private static final FoodId RICE_ID = new FoodId("Brand", "Rice");
    private static final Food CHICKEN = Food.builder(CHICKEN_ID, 100, 165).build();
    private static final Food RICE = Food.builder(RICE_ID, 100, 130).build();

    @Mock
    private RecipePool recipePool;
    @Mock
    private FoodPool foodPool;

    @InjectMocks
    private CreateRecipeCommand createRecipeCommand;

    @Test
    public void testExecuteWithValidArgumentsReturnsSuccessMessage() throws InvalidCommandException {
        when(foodPool.getFood(CHICKEN_ID)).thenReturn(CHICKEN);
        when(foodPool.getFood(RICE_ID)).thenReturn(RICE);
        List<String> arguments = List.of(
                "ChickenRice", "Classic", "2",
                "Brand", "Chicken", "1.0",
                "Brand", "Rice", "1.5"
        );

        String result = createRecipeCommand.execute(arguments);

        assertEquals(GlobalConstants.SUCCESSFULLY_CREATED_RECIPE_MESSAGE, result,
                "execute must return the success constant when arguments are valid");
    }

    @Test
    public void testExecuteWithValidArgumentsDelegatesAddRecipeToPool() throws InvalidCommandException {
        when(foodPool.getFood(CHICKEN_ID)).thenReturn(CHICKEN);
        when(foodPool.getFood(RICE_ID)).thenReturn(RICE);
        List<String> arguments = List.of(
                "ChickenRice", "Classic", "2",
                "Brand", "Chicken", "1.0",
                "Brand", "Rice", "1.5"
        );

        createRecipeCommand.execute(arguments);
        verify(recipePool).addRecipe(any(Recipe.class));
    }

    @Test
    public void testExecuteWithMissingHeaderArgsThrows() {
        List<String> arguments = List.of("ChickenRice", "Classic");

        assertThrows(InvalidCommandException.class,
                () -> createRecipeCommand.execute(arguments),
                "Fewer than 3 header arguments must throw InvalidCommandException");
    }

    @Test
    public void testExecuteWithIncompleteIngredientTripletThrows() {
        List<String> arguments = List.of(
                "ChickenRice", "Classic", "2",
                "Brand", "Chicken"
        );

        assertThrows(InvalidCommandException.class, () -> createRecipeCommand.execute(arguments),
                "An incomplete ingredient triplet must throw InvalidCommandException");
    }

    @Test
    public void testExecuteWithNonExistentFoodThrows() {
        FoodId unknownId = new FoodId("Unknown", "Ingredient");
        when(foodPool.getFood(unknownId))
                .thenThrow(new IllegalArgumentException(GlobalConstants.NOT_EXISTING_FOOD_MESSAGE));
        List<String> arguments = List.of(
                "Mystery", "Dish", "1",
                "Unknown", "Ingredient", "1.0"
        );

        assertThrows(IllegalArgumentException.class, () -> createRecipeCommand.execute(arguments),
                "Referencing a food absent from the pool must propagate IllegalArgumentException");
    }
}
