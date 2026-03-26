package com.fmi.myfitnesspal.calorie;

import com.fmi.myfitnesspal.user.sex.Sex;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CalorieGoalCalculatorTest {

    @ParameterizedTest
    @MethodSource("provideAllSexAndGoalCombinations")
    void testCalculateCaloriesForAnySexAndGoalType(Sex sex, CalorieGoalType goalType) {
        int expectedCalorieTarget = sex.getBaseBmr() + goalType.getCalorieAdjustment();

        CalorieGoal result = CalorieGoalCalculator.calculateCalories(sex, goalType);

        assertEquals(expectedCalorieTarget, result.dailyCalorieTarget(),
                "The target calories must strictly equal the base BMR of the given sex plus the goal adjustment");
    }

    private static Stream<Arguments> provideAllSexAndGoalCombinations() {
        return Stream.of(Sex.values())
                .flatMap(sex -> Stream.of(CalorieGoalType.values())
                        .map(goalType -> Arguments.of(sex, goalType)));
    }
}
