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
import com.fmi.myfitnesspal.command.user.RegisterNewUserCommand;
import com.fmi.myfitnesspal.command.water.RemoveWaterCommand;
import com.fmi.myfitnesspal.command.water.RemoveWaterPortionCommand;
import com.fmi.myfitnesspal.exercise.ExerciseDiary;
import com.fmi.myfitnesspal.exercise.ExercisePool;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.FoodPool;
import com.fmi.myfitnesspal.food.MealPool;
import com.fmi.myfitnesspal.registration_cli.UserRegistration;
import com.fmi.myfitnesspal.user.UserHolder;
import com.fmi.myfitnesspal.water.WaterDiary;
import com.fmi.myfitnesspal.command.water.AddWaterCommand;
import com.fmi.myfitnesspal.command.water.AddWaterPortionCommand;
import com.fmi.myfitnesspal.command.water.GetWaterCommand;


import java.util.Scanner;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        UserHolder userHolder = new UserHolder();
        WaterDiary waterDiary = new WaterDiary();
        FoodDiary foodDiary = new FoodDiary();
        FoodPool foodPool = new FoodPool();
        MealPool mealPool = new MealPool();
        ExercisePool exercisePool = new ExercisePool();
        ExerciseDiary exerciseDiary = new ExerciseDiary(exercisePool);
        ExecutableCommandRegistry registry = new ExecutableCommandRegistry();
        CommandParser parser = new CommandParser();
        CommandExecutor executor = new CommandExecutor(registry);
        Scanner scanner = new Scanner(System.in);
        UserRegistration userRegistration = new UserRegistration(scanner);

        fillRegistry(registry, waterDiary, foodPool, foodDiary, mealPool, exercisePool, exerciseDiary, userHolder,
            scanner, userRegistration);

        Menu menu = new Menu(registry, scanner);
        menu.start();
    }

    private static void fillRegistry(ExecutableCommandRegistry registry, WaterDiary waterDiary,
                                     FoodPool foodPool, FoodDiary foodDiary, MealPool mealPool,
                                     ExercisePool exercisePool, ExerciseDiary exerciseDiary,
                                     UserHolder userHolder,
                                     Scanner scanner, UserRegistration userRegistration) {
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
    }
}
