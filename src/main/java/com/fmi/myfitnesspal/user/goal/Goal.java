package com.fmi.myfitnesspal.user.goal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.fmi.myfitnesspal.user.exception.SecondaryGoalNotApplicableException;

public class Goal {
    private final PossibleGoals possibleGoals;

    private final GoalType type;
    private final Set<String> secondaryGoals = new HashSet<>();

    public Goal(GoalType type) {
        this(type, new DefaultPossibleGoals());
    }

    public Goal(GoalType type, PossibleGoals possibleGoals) {
        this.type = type;
        this.possibleGoals = possibleGoals;
    }

    /**
     * Adds secondary goal to the primary goal
     * @param secondaryGoal
     * @throws IllegalArgumentException when the method is called with null, empty or blank string
     * @throws SecondaryGoalNotApplicableException when the secondary goal is not possible for this primary goal
     */
    public void addSecondaryGoal(String secondaryGoal) {
        if (secondaryGoal == null || secondaryGoal.isBlank()) {
            throw new IllegalArgumentException("the additional cannot be null");
        }
        if (!possibleGoals.isPossibleGoal(type, secondaryGoal)) {
            throw new SecondaryGoalNotApplicableException("Secondary goal not applicable");
        }

        secondaryGoals.add(secondaryGoal);
    }

    /**
     * @return the type of the goal
     */
    public GoalType getType() {
        return type;
    }

    /**
     * @return list of the additionals
     */
    public Set<String> getSecondaryGoals() {
        return Collections.unmodifiableSet(secondaryGoals);
    }
}
