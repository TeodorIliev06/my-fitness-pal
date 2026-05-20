package com.fmi.myfitnesspal.cli;

import com.fmi.myfitnesspal.command.CommandExecutor;
import com.fmi.myfitnesspal.command.CommandParser;
import com.fmi.myfitnesspal.command.ExecutableCommandRegistry;
import com.fmi.myfitnesspal.command.help.HelpCommand;
import com.fmi.myfitnesspal.command.exercise.ShowExercisesCommand;
import com.fmi.myfitnesspal.command.exercise.CreateCardioExerciseCommand;
import com.fmi.myfitnesspal.command.exercise.CreateStrengthExerciseCommand;
import com.fmi.myfitnesspal.command.exercise.LogExerciseCommand;
import com.fmi.myfitnesspal.command.exercise.CreateWorkoutCommand;
import com.fmi.myfitnesspal.command.exercise.RemoveExerciseCommand;
import com.fmi.myfitnesspal.command.exercise.ShowCardioExercisesCommand;
import com.fmi.myfitnesspal.command.exercise.ShowDailyExerciseCommand;
import com.fmi.myfitnesspal.command.exercise.ShowStrengthExercisesCommand;
import com.fmi.myfitnesspal.command.exercise.ShowWeeklyCardioCommand;
import com.fmi.myfitnesspal.command.exercise.ShowWorkoutsCommand;
import com.fmi.myfitnesspal.command.food.RemoveFoodCommand;
import com.fmi.myfitnesspal.command.food.AddFoodCommand;
import com.fmi.myfitnesspal.command.food.RemoveMealCommand;
import com.fmi.myfitnesspal.command.food.AddMealCommand;
import com.fmi.myfitnesspal.command.food.ShowMealsCommand;
import com.fmi.myfitnesspal.command.food.ShowFoodsCommand;
import com.fmi.myfitnesspal.command.food.CreateMealCommand;
import com.fmi.myfitnesspal.command.food.CreateFoodCommand;
import com.fmi.myfitnesspal.command.food.AddRecipeCommand;
import com.fmi.myfitnesspal.command.food.CreateRecipeCommand;
import com.fmi.myfitnesspal.command.food.RemoveRecipeCommand;
import com.fmi.myfitnesspal.command.food.ShowRecipesCommand;
import com.fmi.myfitnesspal.command.food.ShowWeeklyCaloriesCommand;
import com.fmi.myfitnesspal.command.food.ShowDailyMealCaloriesCommand;
import com.fmi.myfitnesspal.command.food.ShowDailyNutrientsCommand;
import com.fmi.myfitnesspal.command.food.ShowWeeklyNutrientsCommand;
import com.fmi.myfitnesspal.command.food.ShowDailyFoodLogCommand;
import com.fmi.myfitnesspal.command.calorie.CheckCalorieGoalCommand;
import com.fmi.myfitnesspal.command.calorie.SetCalorieGoalCommand;
import com.fmi.myfitnesspal.command.user.CreateUserCommand;
import com.fmi.myfitnesspal.command.user.SwitchUserCommand;
import com.fmi.myfitnesspal.command.utility.NutritionSliceMapper;
import com.fmi.myfitnesspal.command.water.RemoveWaterCommand;
import com.fmi.myfitnesspal.command.water.RemoveWaterPortionCommand;
import com.fmi.myfitnesspal.exercise.ExerciseDiary;
import com.fmi.myfitnesspal.exercise.InMemoryExerciseDiary;
import com.fmi.myfitnesspal.exercise.ExercisePool;
import com.fmi.myfitnesspal.exercise.InMemoryExercisePool;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.food.InMemoryFoodDiary;
import com.fmi.myfitnesspal.food.MealPool;
import com.fmi.myfitnesspal.food.InMemoryMealPool;
import com.fmi.myfitnesspal.food.RecipePool;
import com.fmi.myfitnesspal.food.InMemoryRecipePool;
import com.fmi.myfitnesspal.calorie.CalorieGoalHolder;
import com.fmi.myfitnesspal.persistence.file.JsonPersistenceStoreFactory;
import com.fmi.myfitnesspal.persistence.DataTransferable;
import com.fmi.myfitnesspal.persistence.PersistenceStoreFactory;
import com.fmi.myfitnesspal.persistence.food.FoodDiaryFactory;
import com.fmi.myfitnesspal.persistence.food.FoodDiaryDtoMapper;
import com.fmi.myfitnesspal.persistence.food.FoodPoolFactory;
import com.fmi.myfitnesspal.persistence.food.FoodDtoMapper;
import com.fmi.myfitnesspal.persistence.food.UserAwareFoodDiary;
import com.fmi.myfitnesspal.persistence.user.UserPoolFactory;
import com.fmi.myfitnesspal.persistence.user.UserProfileDtoMapper;
import com.fmi.myfitnesspal.persistence.water.WaterDiaryFactory;
import com.fmi.myfitnesspal.persistence.water.DailyWaterEntryDtoMapper;
import com.fmi.myfitnesspal.persistence.water.UserAwareWaterDiary;
import com.fmi.myfitnesspal.user.GuestUserProfileFactory;
import com.fmi.myfitnesspal.user.UserProfile;
import com.fmi.myfitnesspal.user.UserSession;
import com.fmi.myfitnesspal.user.UserPool;
import com.fmi.myfitnesspal.water.WaterDiary;
import com.fmi.myfitnesspal.water.InMemoryWaterDiary;
import com.fmi.myfitnesspal.command.transfer.ImportCommand;
import com.fmi.myfitnesspal.command.transfer.ExportCommand;
import com.fmi.myfitnesspal.command.water.AddWaterCommand;
import com.fmi.myfitnesspal.command.water.AddWaterPortionCommand;
import com.fmi.myfitnesspal.command.water.GetWaterCommand;
import com.fmi.myfitnesspal.chart.BarChartDisplayer;
import com.fmi.myfitnesspal.chart.PieChartDisplayer;

import org.external.chart.BarChartWindow;
import org.external.chart.PieChartWindow;
import org.external.json.JsonConverter;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public final class Main {
    private static final boolean SHOULD_STORE_FOOD_IN_FILE = true;
    private static final boolean SHOULD_STORE_WATER_IN_FILE = true;
    private static final boolean SHOULD_STORE_USERS_IN_FILE = true;

    private static final Path FOOD_POOL_FILE_PATH = Path.of("food_pool.json");
    private static final Path USERS_ROOT_PATH = Path.of("users");

    private Main() {
    }

    public static void main(String[] args) {
        CalorieGoalHolder calorieGoalHolder = new CalorieGoalHolder();
        MealPool mealPool = new InMemoryMealPool();
        RecipePool recipePool = new InMemoryRecipePool();
        ExercisePool exercisePool = new InMemoryExercisePool();
        ExerciseDiary exerciseDiary = new InMemoryExerciseDiary(exercisePool);
        ExecutableCommandRegistry registry = new ExecutableCommandRegistry();
        CommandParser parser = new CommandParser();
        CommandExecutor executor = new CommandExecutor(registry);
        Scanner scanner = new Scanner(System.in);
        NutritionSliceMapper sliceMapper = new NutritionSliceMapper();

        BarChartDisplayer barChartDisplayer = new BarChartWindow();
        PieChartDisplayer pieChartDisplayer = new PieChartWindow();

        JsonMapper jsonMapper = JsonMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();
        JsonConverter jsonConverter = new JsonConverter(jsonMapper);
        PersistenceStoreFactory storeFactory = new JsonPersistenceStoreFactory(jsonConverter);

        FoodDtoMapper foodDtoMapper = new FoodDtoMapper();
        FoodPool foodPool = new FoodPoolFactory(
                SHOULD_STORE_FOOD_IN_FILE,
                storeFactory,
                foodDtoMapper,
                FOOD_POOL_FILE_PATH
        ).create();

        DailyWaterEntryDtoMapper dailyWaterEntryDtoMapper = new DailyWaterEntryDtoMapper();
        WaterDiaryFactory waterDiaryFactory = new WaterDiaryFactory(
                SHOULD_STORE_WATER_IN_FILE,
                storeFactory,
                dailyWaterEntryDtoMapper
        );

        FoodDiaryDtoMapper foodDiaryDtoMapper = new FoodDiaryDtoMapper(foodDtoMapper);
        FoodDiaryFactory foodDiaryFactory = new FoodDiaryFactory(
                SHOULD_STORE_FOOD_IN_FILE,
                storeFactory,
                foodDiaryDtoMapper
        );

        UserProfileDtoMapper userProfileDtoMapper = new UserProfileDtoMapper();
        UserPool userPool = new UserPoolFactory(
                SHOULD_STORE_USERS_IN_FILE,
                storeFactory,
                userProfileDtoMapper,
                USERS_ROOT_PATH
        ).create();

        UserAwareWaterDiary userAwareWaterDiary = new UserAwareWaterDiary(
                waterDiaryFactory, USERS_ROOT_PATH, new InMemoryWaterDiary(), SHOULD_STORE_WATER_IN_FILE
        );
        UserAwareFoodDiary userAwareFoodDiary = new UserAwareFoodDiary(
                foodDiaryFactory, USERS_ROOT_PATH, new InMemoryFoodDiary(), SHOULD_STORE_FOOD_IN_FILE
        );
        List<DataTransferable> dataTransferTargets =
                List.of(userAwareFoodDiary, userAwareWaterDiary);
        UserSession userSession = new UserSession(List.of(userAwareFoodDiary, userAwareWaterDiary));

        UserProfile guestProfile = new GuestUserProfileFactory().create();
        if (!userPool.contains(guestProfile.userId())) {
            userPool.addUser(guestProfile);
        }
        userSession.switchTo(userPool.findById(guestProfile.userId()).orElseThrow());

        fillRegistry(registry, userAwareWaterDiary, foodPool, userAwareFoodDiary,
                mealPool, recipePool, exercisePool, exerciseDiary,
                scanner, sliceMapper, barChartDisplayer, pieChartDisplayer,
                calorieGoalHolder, userSession, userPool, dataTransferTargets);

        Menu menu = new Menu(registry, scanner);
        menu.start();
    }

    private static void fillRegistry(ExecutableCommandRegistry registry, WaterDiary waterDiary,
                                     FoodPool foodPool, FoodDiary foodDiary,
                                     MealPool mealPool, RecipePool recipePool,
                                     ExercisePool exercisePool, ExerciseDiary exerciseDiary,
                                     Scanner scanner,
                                     NutritionSliceMapper sliceMapper,
                                     BarChartDisplayer barChartDisplayer, PieChartDisplayer pieChartDisplayer,
                                     CalorieGoalHolder calorieGoalHolder,
                                     UserSession userSession, UserPool userPool,
                                     List<DataTransferable> dataTransferTargets) {
        registry.addCommand(new AddWaterCommand(waterDiary));
        registry.addCommand(new AddWaterPortionCommand(waterDiary));
        registry.addCommand(new GetWaterCommand(waterDiary));
        registry.addCommand(new RemoveWaterCommand(waterDiary));
        registry.addCommand(new RemoveWaterPortionCommand(waterDiary));
        registry.addCommand(new CreateFoodCommand(foodPool));
        registry.addCommand(new AddFoodCommand(foodDiary, foodPool));
        registry.addCommand(new ShowFoodsCommand(foodDiary));
        registry.addCommand(new RemoveFoodCommand(foodDiary));
        registry.addCommand(new CreateMealCommand(mealPool, foodPool));
        registry.addCommand(new AddMealCommand(foodDiary, mealPool));
        registry.addCommand(new ShowMealsCommand(mealPool));
        registry.addCommand(new RemoveMealCommand(mealPool));
        registry.addCommand(new CreateRecipeCommand(recipePool, foodPool));
        registry.addCommand(new AddRecipeCommand(foodDiary, recipePool));
        registry.addCommand(new ShowRecipesCommand(recipePool));
        registry.addCommand(new RemoveRecipeCommand(recipePool));
        registry.addCommand(new ShowWeeklyCaloriesCommand(foodDiary));
        registry.addCommand(new HelpCommand(registry));
        registry.addCommand(new CreateUserCommand(userPool));
        registry.addCommand(new SwitchUserCommand(userPool, userSession));
        registry.addCommand(new CreateCardioExerciseCommand(exercisePool));
        registry.addCommand(new CreateStrengthExerciseCommand(exercisePool));
        registry.addCommand(new CreateWorkoutCommand(exercisePool));
        registry.addCommand(new LogExerciseCommand(exerciseDiary));
        registry.addCommand(new RemoveExerciseCommand(exerciseDiary));
        registry.addCommand(new ShowExercisesCommand(exercisePool));
        registry.addCommand(new ShowCardioExercisesCommand(exercisePool));
        registry.addCommand(new ShowDailyExerciseCommand(exerciseDiary));
        registry.addCommand(new ShowStrengthExercisesCommand(exercisePool));
        registry.addCommand(new ShowWorkoutsCommand(exercisePool));
        registry.addCommand(new ShowWeeklyCardioCommand(exerciseDiary));
        registry.addCommand(new ShowDailyMealCaloriesCommand(foodDiary, pieChartDisplayer, sliceMapper));
        registry.addCommand(new ShowDailyNutrientsCommand(foodDiary, pieChartDisplayer, sliceMapper));
        registry.addCommand(new ShowWeeklyNutrientsCommand(foodDiary, pieChartDisplayer, sliceMapper));
        registry.addCommand(new ShowDailyFoodLogCommand(foodDiary));
        registry.addCommand(new SetCalorieGoalCommand(calorieGoalHolder));
        registry.addCommand(new CheckCalorieGoalCommand(foodDiary, calorieGoalHolder, barChartDisplayer));
        registry.addCommand(new ImportCommand(dataTransferTargets));
        registry.addCommand(new ExportCommand(dataTransferTargets));
    }
}
