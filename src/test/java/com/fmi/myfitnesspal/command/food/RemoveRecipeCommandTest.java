package com.fmi.myfitnesspal.command.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.RecipeId;
import com.fmi.myfitnesspal.food.RecipePool;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public final class RemoveRecipeCommandTest {

    private static final String RECIPE_NAME = "Moussaka";
    private static final String RECIPE_DESCRIPTION = "Traditional";
    private static final RecipeId STORED_RECIPE_ID = new RecipeId(RECIPE_NAME, RECIPE_DESCRIPTION);

    @Mock
    private RecipePool recipePool;

    @InjectMocks
    private RemoveRecipeCommand removeRecipeCommand;

    @Test
    public void testExecuteWithValidArgumentsReturnsSuccessMessage() throws InvalidCommandException {
        List<String> arguments = List.of(RECIPE_NAME, RECIPE_DESCRIPTION);

        String result = removeRecipeCommand.execute(arguments);

        assertEquals(GlobalConstants.SUCCESSFULLY_REMOVED_RECIPE_MESSAGE, result,
                "execute must return the removal success constant when arguments are valid");
    }

    @Test
    public void testExecuteWithValidArgumentsDelegatesRemoveRecipeToPool() throws InvalidCommandException {
        List<String> arguments = List.of(RECIPE_NAME, RECIPE_DESCRIPTION);

        removeRecipeCommand.execute(arguments);

        verify(recipePool).removeRecipe(STORED_RECIPE_ID);
    }

    @Test
    public void testExecuteWithNonExistingRecipeThrows() {
        RecipeId unknownId = new RecipeId("Ghost Recipe", "Does Not Exist");
        doThrow(new IllegalArgumentException(GlobalConstants.NOT_EXISTING_RECIPE_MESSAGE))
                .when(recipePool).removeRecipe(unknownId);
        List<String> arguments = List.of("Ghost Recipe", "Does Not Exist");

        assertThrows(IllegalArgumentException.class,
                () -> removeRecipeCommand.execute(arguments),
                "Removing a recipe absent from the pool must propagate IllegalArgumentException");
    }

    @Test
    public void testExecuteWithWrongArgumentCountThrows() {
        List<String> arguments = List.of(RECIPE_NAME);

        assertThrows(InvalidCommandException.class,
                () -> removeRecipeCommand.execute(arguments),
                "execute must throw InvalidCommandException when argument count is not exactly 2");
    }
}
