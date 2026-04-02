package com.fmi.myfitnesspal.command.calorie;

import com.fmi.myfitnesspal.calorie.CalorieGoal;
import com.fmi.myfitnesspal.calorie.CalorieGoalHolder;
import com.fmi.myfitnesspal.chart.BarChartDisplayer;
import com.fmi.myfitnesspal.chart.BarEntry;
import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.DailyNutritionSummary;
import com.fmi.myfitnesspal.food.FoodDiary;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseDate;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.toWeekNumber;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class CheckCalorieGoalCommand implements ExecutableCommand {

    private static final String COMMAND_NAME = "check-calorie-goal";
    private static final int ARGUMENTS_COUNT = 1;
    private static final String NO_GOAL_SET_MESSAGE =
            "No calorie goal has been set. Use set-calorie-goal first.";

    private final FoodDiary foodDiary;
    private final CalorieGoalHolder calorieGoalHolder;
    private final BarChartDisplayer barChartDisplayer;

    public CheckCalorieGoalCommand(
            FoodDiary foodDiary,
            CalorieGoalHolder calorieGoalHolder,
            BarChartDisplayer barChartDisplayer) {
        this.foodDiary = foodDiary;
        this.calorieGoalHolder = calorieGoalHolder;
        this.barChartDisplayer = barChartDisplayer;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);
        LocalDate targetDate = parseDate(arguments.get(0));

        CalorieGoal activeGoal = getActiveGoal();
        List<DailyNutritionSummary> weeklySummaries =
                foodDiary.getDailyNutritionSummariesForWeek(targetDate);

        displayBarChart(activeGoal, weeklySummaries, targetDate);
        return buildComparisonMessage(activeGoal, weeklySummaries);
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <date>";
    }

    private CalorieGoal getActiveGoal() throws InvalidCommandException {
        return calorieGoalHolder.getActiveCalorieGoal()
                .orElseThrow(() -> new InvalidCommandException(NO_GOAL_SET_MESSAGE));
    }

    private void displayBarChart(CalorieGoal activeGoal,
                                 List<DailyNutritionSummary> weeklySummaries,
                                 LocalDate targetDate) {
        int weekNumber = toWeekNumber(targetDate);

        String chartTitle = String.format(
                "Week %d — Daily Calorie Goal: %d calories",
                weekNumber, activeGoal.dailyCalorieTarget());
        barChartDisplayer.display(chartTitle, toBarEntries(weeklySummaries), activeGoal.dailyCalorieTarget());
    }

    private List<BarEntry> toBarEntries(List<DailyNutritionSummary> weeklySummaries) {
        return weeklySummaries.stream()
                .map(s -> new BarEntry(
                        s.date().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                        s.calories()))
                .toList();
    }

    private String buildComparisonMessage(CalorieGoal activeGoal,
                                          List<DailyNutritionSummary> weeklySummaries) {
        int averageCalories = computeAverageCalories(weeklySummaries);
        int deviation = averageCalories - activeGoal.dailyCalorieTarget();

        if (deviation == 0) {
            return String.format(
                    "This week you exactly met your %d calorie goal.", activeGoal.dailyCalorieTarget());
        }

        String direction = deviation > 0 ? "more" : "less";
        return String.format(
                "This week you had an average of %d calories %s than your %d calorie goal.",
                Math.abs(deviation), direction, activeGoal.dailyCalorieTarget());
    }

    private int computeAverageCalories(List<DailyNutritionSummary> weeklySummaries) {
        if (weeklySummaries.isEmpty()) {
            return 0;
        }

        double weeklyTotal = weeklySummaries.stream()
                .mapToDouble(DailyNutritionSummary::calories)
                .sum();
        return (int) Math.round(weeklyTotal / weeklySummaries.size());
    }
}
