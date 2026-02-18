package com.fmi.myfitnesspal.user.goal;

public class DefaultPossibleGoals extends PossibleGoals {
    private static final String LACK_OF_TIME = "lack of time";
    private static final String REGIMEN_HARD_TO_FOLLOW = "regimen hard to follow";
    private static final String HEALTHY_DIETS_LACK_VARIETY = "healthy diets lack variety";
    private static final String STRESS_AROUND_FOOD_CHOICES = "stress around food choices";
    private static final String HOLIDAYS_VACATION_SOCIAL_EVENTS = "holidays vacation social events";
    private static final String FOOD_CARVING = "food carving";
    private static final String LACK_OF_PROGRESS = "lack of progress";
    private static final String HEALTHY_FOOD_DOES_NOT_TASTE_GOOD = "healthy food does not taste good";
    private static final String HEALTHY_FOOD_TOO_EXPENSIVE = "healthy food too expensive";
    private static final String COOKING_TOO_HARD = "cooking too hard";
    private static final String DID_NOT_EXPERIENCE_BARRIERS = "did not experience barriers";
    private static final String COMPETITIVE_SPORT_PERFORMANCE = "competitive sport performance";
    private static final String GAIN_MUSCLE_FOR_GENERAL_FITNESS = "gain muscle for general fitness";
    private static final String UNDERWEIGHT = "underweight";
    private static final String HEALTHCARE_PROVIDER_RECOMMENDED = "healthcare provider recommended";
    private static final String OTHER = "other";
    private static final String GET_STRONG = "get strong";
    private static final String BULK_UP = "bulk up";
    private static final String TONE_UP = "tone up";
    private static final String TRACK_MACROS = "track macros";
    private static final String EAT_VEGAN = "eat vegan";
    private static final String EAT_VEGETARIAN = "eat vegetarian";
    private static final String EAT_PESCETARIAN = "eat pescetarian";
    private static final String LESS_SUGAR = "less sugar";
    private static final String LESS_PROTEIN = "less protein";
    private static final String LESS_DIARY = "less diary";
    private static final String FEWER_CARBS = "fewer carbs";
    private static final String LESS_FAT = "less fat";
    private static final String MORE_PROTEIN = "more protein";
    private static final String MORE_FAT = "more fat";
    private static final String MORE_FRUIT_AND_VEGETABLES = "more fruit and vegetables";
    private static final String WALK = "walk";
    private static final String RUN = "run";
    private static final String DO_STRENGTH_WORKOUT = "do strength workout";
    private static final String GO_ON_BIKE_RIDE = "go on bike ride";
    private static final String DO_YOGA = "do yoga";
    private static final String STRETCH = "stretch";
    private static final String WATCH_READ_LISTEN_MOTIVATIONAL = "watch read listen motivational";
    private static final String MEDIATE = "mediate";
    private static final String LISTEN_TO_MUSIC = "listen to music";
    private static final String DO_SOMETHING_ELSE = "do something else";
    private static final String NOTHING_HELPS = "nothing helps";


    public DefaultPossibleGoals() {
        addInitialSecondaryGoals();
    }

    private void addInitialSecondaryGoals() {
        addGoal(GoalType.LOSE_WEIGHT, LACK_OF_TIME);
        addGoal(GoalType.LOSE_WEIGHT, REGIMEN_HARD_TO_FOLLOW);
        addGoal(GoalType.LOSE_WEIGHT, HEALTHY_DIETS_LACK_VARIETY);
        addGoal(GoalType.LOSE_WEIGHT, STRESS_AROUND_FOOD_CHOICES);
        addGoal(GoalType.LOSE_WEIGHT, HOLIDAYS_VACATION_SOCIAL_EVENTS);
        addGoal(GoalType.LOSE_WEIGHT, FOOD_CARVING);
        addGoal(GoalType.LOSE_WEIGHT, LACK_OF_PROGRESS);
        addGoal(GoalType.LOSE_WEIGHT, HEALTHY_FOOD_DOES_NOT_TASTE_GOOD);
        addGoal(GoalType.LOSE_WEIGHT, HEALTHY_FOOD_TOO_EXPENSIVE);
        addGoal(GoalType.LOSE_WEIGHT, COOKING_TOO_HARD);
        addGoal(GoalType.LOSE_WEIGHT, DID_NOT_EXPERIENCE_BARRIERS);

        addGoal(GoalType.MAINTAIN_WEIGHT, LACK_OF_TIME);
        addGoal(GoalType.MAINTAIN_WEIGHT, REGIMEN_HARD_TO_FOLLOW);
        addGoal(GoalType.MAINTAIN_WEIGHT, HEALTHY_DIETS_LACK_VARIETY);
        addGoal(GoalType.MAINTAIN_WEIGHT, STRESS_AROUND_FOOD_CHOICES);
        addGoal(GoalType.MAINTAIN_WEIGHT, HOLIDAYS_VACATION_SOCIAL_EVENTS);
        addGoal(GoalType.MAINTAIN_WEIGHT, FOOD_CARVING);
        addGoal(GoalType.MAINTAIN_WEIGHT, LACK_OF_PROGRESS);
        addGoal(GoalType.MAINTAIN_WEIGHT, HEALTHY_FOOD_DOES_NOT_TASTE_GOOD);
        addGoal(GoalType.MAINTAIN_WEIGHT, HEALTHY_FOOD_TOO_EXPENSIVE);
        addGoal(GoalType.MAINTAIN_WEIGHT, COOKING_TOO_HARD);
        addGoal(GoalType.MAINTAIN_WEIGHT, DID_NOT_EXPERIENCE_BARRIERS);

        addGoal(GoalType.GAIN_WEIGHT, COMPETITIVE_SPORT_PERFORMANCE);
        addGoal(GoalType.GAIN_WEIGHT, GAIN_MUSCLE_FOR_GENERAL_FITNESS);
        addGoal(GoalType.GAIN_WEIGHT, UNDERWEIGHT);
        addGoal(GoalType.GAIN_WEIGHT, HEALTHCARE_PROVIDER_RECOMMENDED);
        addGoal(GoalType.GAIN_WEIGHT, OTHER);

        addGoal(GoalType.GAIN_MUSCLE, TONE_UP);
        addGoal(GoalType.GAIN_MUSCLE, BULK_UP);
        addGoal(GoalType.GAIN_MUSCLE, GET_STRONG);

        addGoal(GoalType.MODIFY_MY_DIET, TRACK_MACROS);
        addGoal(GoalType.MODIFY_MY_DIET, EAT_VEGAN);
        addGoal(GoalType.MODIFY_MY_DIET, EAT_VEGETARIAN);
        addGoal(GoalType.MODIFY_MY_DIET, EAT_PESCETARIAN);
        addGoal(GoalType.MODIFY_MY_DIET, LESS_SUGAR);
        addGoal(GoalType.MODIFY_MY_DIET, LESS_PROTEIN);
        addGoal(GoalType.MODIFY_MY_DIET, LESS_DIARY);
        addGoal(GoalType.MODIFY_MY_DIET, FEWER_CARBS);
        addGoal(GoalType.MODIFY_MY_DIET, LESS_FAT);
        addGoal(GoalType.MODIFY_MY_DIET, MORE_PROTEIN);
        addGoal(GoalType.MODIFY_MY_DIET, MORE_FAT);
        addGoal(GoalType.MODIFY_MY_DIET, MORE_FRUIT_AND_VEGETABLES);
        addGoal(GoalType.MODIFY_MY_DIET, OTHER);

        addGoal(GoalType.MANAGE_STRESS, WALK);
        addGoal(GoalType.MANAGE_STRESS, RUN);
        addGoal(GoalType.MANAGE_STRESS, DO_STRENGTH_WORKOUT);
        addGoal(GoalType.MANAGE_STRESS, GO_ON_BIKE_RIDE);
        addGoal(GoalType.MANAGE_STRESS, DO_YOGA);
        addGoal(GoalType.MANAGE_STRESS, STRETCH);
        addGoal(GoalType.MANAGE_STRESS, WATCH_READ_LISTEN_MOTIVATIONAL);
        addGoal(GoalType.MANAGE_STRESS, MEDIATE);
        addGoal(GoalType.MANAGE_STRESS, LISTEN_TO_MUSIC);
        addGoal(GoalType.MANAGE_STRESS, DO_SOMETHING_ELSE);
        addGoal(GoalType.MANAGE_STRESS, NOTHING_HELPS);
    }
}
