package com.fmi.myfitnesspal.command.exercise;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exception.UnknownExerciseException;
import com.fmi.myfitnesspal.exercise.Exercise;
import com.fmi.myfitnesspal.exercise.ExercisePool;
import com.fmi.myfitnesspal.exercise.Workout;

import java.util.ArrayList;
import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class CreateWorkoutCommand implements ExecutableCommand {
    private static final int MIN_ARGUMENT_COUNT = 1;
    private static final String NAME = "create-workout";
    private final ExercisePool exercisePool;

    public CreateWorkoutCommand(ExercisePool exercisePool) {
        this.exercisePool = exercisePool;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, args -> args.size() > MIN_ARGUMENT_COUNT);

        String name = arguments.get(0);
        List<Exercise> exercises = new ArrayList<>();
        List<String> sublist = arguments.subList(1, arguments.size());

        for (String exerciseName : sublist) {
            Exercise exercise = null;
            try {
                exercise = exercisePool.getExerciseByName(exerciseName);
            } catch (UnknownExerciseException e) {
                return GlobalConstants.NOT_EXISTING_EXERCISE_MESSAGE;
            }

            exercises.add(exercise);
        }

        Exercise newWorkout = new Workout(name, exercises);
        exercisePool.createExercise(newWorkout);
        return String.format("%s was created successfully!", name);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + NAME + " <name> <exercise_name_1> <exercise_name_2> <exercise_name_3> ...";
    }
}
