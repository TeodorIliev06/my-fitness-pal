package com.fmi.myfitnesspal.user.goal;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PossibleGoals {
    private final Map<GoalType, Set<String>> possibleSecondaryGoals = new HashMap<>();

    /**
     * Adds goal to the possible options
     *
     * @param primaryGoal
     * @param secondaryGoal
     */
    public void addGoal(GoalType primaryGoal, String secondaryGoal) {
        if (primaryGoal == null || secondaryGoal == null) {
            throw new IllegalArgumentException("The goals cannot be null");
        }

        possibleSecondaryGoals.putIfAbsent(primaryGoal, new HashSet<>());
        possibleSecondaryGoals.get(primaryGoal).add(secondaryGoal);
    }

    /**
     * @param goalType
     * @return set containing the possible secondary goals for the given primary goal
     */
    public Set<String> getSecondaryGoals(GoalType goalType) {
        if (goalType == null) {
            throw new IllegalArgumentException("The goalType cannot be null");
        }

        possibleSecondaryGoals.putIfAbsent(goalType, new HashSet<>());
        return Collections.unmodifiableSet(possibleSecondaryGoals.get(goalType));
    }

    /**
     * @param primaryGoal   - the primary goal type
     * @param secondaryGoal - string for the secondary goal
     * @return whether the secondary goal is compatible with the primary goal
     */
    public boolean isPossibleGoal(GoalType primaryGoal, String secondaryGoal) {
        return getSecondaryGoals(primaryGoal).contains(secondaryGoal);
    }
}
