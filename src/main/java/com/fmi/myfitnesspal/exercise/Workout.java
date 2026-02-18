package com.fmi.myfitnesspal.exercise;

import com.fmi.myfitnesspal.exception.UnknownExerciseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.stream.Collectors;


public final class Workout extends Exercise {
    private List<Exercise> exercises;

    public Workout(String name, Collection<Exercise> exercises) {
        super(name, 0);
        this.exercises = new ArrayList<>();

        for (Exercise exercise : exercises) {
            addExercise(exercise);
        }
    }

    public boolean addExercise(Exercise exercise) {
        int updatedCalories = super.burnedCalories() + exercise.burnedCalories();
        super.setCalories(updatedCalories);
        return exercises.add(exercise);
    }

    public void removeExercise(String exerciseName) throws UnknownExerciseException {
        Exercise exerciseToRemove = null;
        for (Exercise exercise : exercises) {
            if (exercise.name().equals(exerciseName)) {
                exerciseToRemove = exercise;
            }
        }

        if (exerciseToRemove == null) {
            throw new UnknownExerciseException(String.format(
                "Cannot remove exercise with name %s is not in the workout", exerciseName));
        }

        int updatedCalories = super.burnedCalories() - exerciseToRemove.burnedCalories();
        super.setCalories(updatedCalories);

        exercises.remove(exerciseToRemove);
    }

    List<Exercise> getExercises() {
        return exercises;
    }

    @Override
    public String toString() {
        String exStrings = exercises.stream()
            .map(Object::toString)
            .collect(Collectors.joining(System.lineSeparator()));

        return "Workout{" + "name=" + super.name() + ", exercises=" + exStrings + '}';
    }
}
