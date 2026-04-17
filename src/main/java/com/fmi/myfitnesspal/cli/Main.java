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
import com.fmi.myfitnesspal.command.food.ShowWeeklyCaloriesCommand;
import com.fmi.myfitnesspal.command.food.ShowDailyMealCaloriesCommand;
import com.fmi.myfitnesspal.command.food.ShowDailyNutrientsCommand;
import com.fmi.myfitnesspal.command.food.ShowWeeklyNutrientsCommand;
import com.fmi.myfitnesspal.command.calorie.CheckCalorieGoalCommand;
import com.fmi.myfitnesspal.command.calorie.SetCalorieGoalCommand;
import com.fmi.myfitnesspal.command.user.RegisterNewUserCommand;
import com.fmi.myfitnesspal.command.utility.NutritionSliceMapper;
import com.fmi.myfitnesspal.command.water.RemoveWaterCommand;
import com.fmi.myfitnesspal.command.water.RemoveWaterPortionCommand;
import com.fmi.myfitnesspal.exercise.ExerciseDiary;
import com.fmi.myfitnesspal.exercise.InMemoryExerciseDiary;
import com.fmi.myfitnesspal.exercise.ExercisePool;
import com.fmi.myfitnesspal.exercise.InMemoryExercisePool;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.food.MealPool;
import com.fmi.myfitnesspal.calorie.CalorieGoalHolder;
import com.fmi.myfitnesspal.persistence.food.FoodDiaryFactory;
import com.fmi.myfitnesspal.persistence.food.FoodDiaryDtoMapper;
import com.fmi.myfitnesspal.persistence.food.FoodPoolFactory;
import com.fmi.myfitnesspal.persistence.food.FoodDtoMapper;
import com.fmi.myfitnesspal.persistence.water.WaterDiaryFactory;
import com.fmi.myfitnesspal.persistence.water.DailyWaterEntryDtoMapper;
import com.fmi.myfitnesspal.registration_cli.UserRegistration;
import com.fmi.myfitnesspal.user.UserHolder;
import com.fmi.myfitnesspal.water.WaterDiary;
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
import java.util.Scanner;

public final class Main {
    private static final boolean SHOULD_STORE_FOOD_IN_FILE = true;
    private static final boolean SHOULD_STORE_WATER_IN_FILE = true;

    private static final Path FOOD_POOL_FILE_PATH = Path.of("food_pool.json");
    private static final Path WATER_DIARY_FILE_PATH = Path.of("water_diary.json");
    private static final Path FOOD_DIARY_FILE_PATH = Path.of("food_diary.json");

    private Main() {
    }

    public static void main(String[] args) {
        UserHolder userHolder = new UserHolder();
        CalorieGoalHolder calorieGoalHolder = new CalorieGoalHolder();
        MealPool mealPool = new MealPool();
        ExercisePool exercisePool = new InMemoryExercisePool();
        ExerciseDiary exerciseDiary = new InMemoryExerciseDiary(exercisePool);
        ExecutableCommandRegistry registry = new ExecutableCommandRegistry();
        CommandParser parser = new CommandParser();
        CommandExecutor executor = new CommandExecutor(registry);
        Scanner scanner = new Scanner(System.in);
        UserRegistration userRegistration = new UserRegistration(scanner);
        NutritionSliceMapper sliceMapper = new NutritionSliceMapper();

        BarChartDisplayer barChartDisplayer = new BarChartWindow();
        PieChartDisplayer pieChartDisplayer = new PieChartWindow();

        JsonMapper jsonMapper = JsonMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();
        JsonConverter jsonConverter = new JsonConverter(jsonMapper);

        FoodDtoMapper foodDtoMapper = new FoodDtoMapper();
        FoodPool foodPool = new FoodPoolFactory(
                SHOULD_STORE_FOOD_IN_FILE,
                jsonConverter,
                foodDtoMapper,
                FOOD_POOL_FILE_PATH
        ).create();

        DailyWaterEntryDtoMapper dailyWaterEntryDtoMapper = new DailyWaterEntryDtoMapper();
        WaterDiary waterDiary = new WaterDiaryFactory(
                SHOULD_STORE_WATER_IN_FILE,
                jsonConverter,
                dailyWaterEntryDtoMapper,
                WATER_DIARY_FILE_PATH
        ).create();

        FoodDiaryDtoMapper foodDiaryDtoMapper = new FoodDiaryDtoMapper(foodDtoMapper);
        FoodDiary foodDiary = new FoodDiaryFactory(
                SHOULD_STORE_FOOD_IN_FILE,
                jsonConverter,
                foodDiaryDtoMapper,
                FOOD_DIARY_FILE_PATH
        ).create();

        fillRegistry(registry, waterDiary, foodPool, foodDiary,
                mealPool, exercisePool, exerciseDiary, userHolder,
                scanner, userRegistration, sliceMapper, barChartDisplayer, pieChartDisplayer,
                calorieGoalHolder);

        Menu menu = new Menu(registry, scanner);
        menu.start();
    }

    private static void fillRegistry(ExecutableCommandRegistry registry, WaterDiary waterDiary,
                                     FoodPool foodPool, FoodDiary foodDiary,
                                     MealPool mealPool, ExercisePool exercisePool, ExerciseDiary exerciseDiary,
                                     UserHolder userHolder,
                                     Scanner scanner, UserRegistration userRegistration,
                                     NutritionSliceMapper sliceMapper,
                                     BarChartDisplayer barChartDisplayer, PieChartDisplayer pieChartDisplayer,
                                     CalorieGoalHolder calorieGoalHolder) {
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
        registry.addCommand(new ShowMealsCommand(foodDiary));
        registry.addCommand(new RemoveMealCommand(foodDiary));
        registry.addCommand(new ShowWeeklyCaloriesCommand(foodDiary));
        registry.addCommand(new HelpCommand(registry));
        registry.addCommand(new RegisterNewUserCommand(scanner, userHolder, userRegistration));
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
        registry.addCommand(new SetCalorieGoalCommand(calorieGoalHolder));
        registry.addCommand(new CheckCalorieGoalCommand(foodDiary, calorieGoalHolder, barChartDisplayer));
    }
}
