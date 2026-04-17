package com.fmi.myfitnesspal.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class InMemoryFoodDiary implements FoodDiary {

    private static final int DAYS_IN_WEEK = 7;

    private final Map<LocalDate, DailyFoodDiary> diary;

    public InMemoryFoodDiary() {
        this.diary = new HashMap<>();
    }

    public void removeFood(LocalDate date, EatingTime eatingTime, FoodId foodId) {
        getValidatedDailyDiary(date).removeFood(eatingTime, foodId);
    }

    public void removeMeal(LocalDate date, EatingTime eatingTime, MealId id) {
        getValidatedDailyDiary(date).removeMeal(eatingTime, id);
    }

    public void addFood(LocalDate date, EatingTime eatingTime, Food food, double numberOfServings) {
        getOrCreateDailyDiary(date).addFood(eatingTime, food, numberOfServings);
    }

    public void addMeal(LocalDate date, EatingTime eatingTime, Meal meal) {
        getOrCreateDailyDiary(date).addMeal(eatingTime, meal);
    }

    public List<Food> getFoodsByDateAndEatingTime(LocalDate date, EatingTime eatingTime) {
        return getValidatedDailyDiary(date).getFoodsByEatingTime(eatingTime);
    }

    public List<Meal> getMealsByDateAndEatingTime(LocalDate date, EatingTime eatingTime) {
        return getValidatedDailyDiary(date).getMealsByEatingTime(eatingTime);
    }

    public List<Food> getFoodsByWeekNumber(int weekNumber) {
        return diary.keySet().stream()
                .filter(date -> date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == weekNumber)
                .flatMap(date -> diary.get(date).getAllFoods().stream())
                .collect(Collectors.toList());
    }

    public List<Food> getAllFoodsByDate(LocalDate date) {
        return findDailyDiary(date)
                .map(DailyFoodDiary::getAllFoods)
                .orElse(List.of());
    }

    public List<Food> getAllFoodsByDateAndEatingTime(LocalDate date, EatingTime eatingTime) {
        return findDailyDiary(date)
                .map(dailyDiary -> dailyDiary.getAllFoodsByEatingTime(eatingTime))
                .orElse(List.of());
    }

    public WeeklyNutritionSummary getWeeklyNutritionSummary(int weekNumber) {
        NutritionTotals totals = sumNutritionFrom(getFoodsByWeekNumber(weekNumber));
        return new WeeklyNutritionSummary(
                weekNumber,
                totals.calories(),
                totals.protein(),
                totals.carbs(),
                totals.fats()
        );
    }

    public WeeklyNutritionSummary getWeeklyNutritionSummary(LocalDate date) {
        int weekNumber = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return getWeeklyNutritionSummary(weekNumber);
    }

    public DailyNutritionSummary getDailyNutritionSummary(LocalDate date) {
        NutritionTotals totals = sumNutritionFrom(getAllFoodsByDate(date));
        return new DailyNutritionSummary(
                date,
                totals.calories(),
                totals.protein(),
                totals.carbs(),
                totals.fats()
        );
    }

    public List<DailyNutritionSummary> getDailyNutritionSummariesForWeek(LocalDate targetDate) {
        LocalDate monday = targetDate.with(DayOfWeek.MONDAY);
        return IntStream.range(0, DAYS_IN_WEEK)
                .mapToObj(monday::plusDays)
                .map(this::getDailyNutritionSummary)
                .toList();
    }

    public DailyMealCaloriesSummary getDailyMealCaloriesSummary(LocalDate date) {
        Map<EatingTime, Double> caloriesByEatingTime = new EnumMap<>(EatingTime.class);

        for (EatingTime eatingTime : EatingTime.values()) {
            caloriesByEatingTime.put(eatingTime, sumCaloriesForMealTime(date, eatingTime));
        }

        return new DailyMealCaloriesSummary(date, caloriesByEatingTime);
    }

    private NutritionTotals sumNutritionFrom(List<Food> foods) {
        double calories = foods.stream().mapToDouble(Food::getCalories).sum();
        return new NutritionTotals(
                calories,
                sumMacro(foods, Food::getProtein),
                sumMacro(foods, Food::getCarbs),
                sumMacro(foods, Food::getFats)
        );
    }

    private double sumCaloriesForMealTime(LocalDate date, EatingTime eatingTime) {
        return getAllFoodsByDateAndEatingTime(date, eatingTime)
                .stream()
                .mapToDouble(Food::getCalories)
                .sum();
    }

    private Optional<Double> sumMacro(List<Food> foods, Function<Food, Optional<Double>> macroGetter) {
        boolean anyMacrosPresent = foods.stream().anyMatch(f -> macroGetter.apply(f).isPresent());

        if (!anyMacrosPresent) {
            return Optional.empty();
        }

        double total = foods.stream()
                .mapToDouble(f -> macroGetter.apply(f).orElse(0.0))
                .sum();

        return Optional.of(total);
    }

    @Override
    public List<DailyFoodEntry> getAllDailyFoodEntries() {
        return diary.entrySet().stream()
                .flatMap(entry -> foodEntriesForDate(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Override
    public List<DailyMealEntry> getAllDailyMealEntries() {
        return diary.entrySet().stream()
                .flatMap(entry -> mealEntriesForDate(entry.getKey(), entry.getValue()))
                .toList();
    }

    private Stream<DailyFoodEntry> foodEntriesForDate(LocalDate consumptionDate, DailyFoodDiary dailyDiary) {
        return Stream.of(EatingTime.values())
                .flatMap(eatingTime -> dailyDiary.getFoodsByEatingTime(eatingTime).stream()
                        .map(food -> new DailyFoodEntry(consumptionDate, eatingTime, food)));
    }

    private Stream<DailyMealEntry> mealEntriesForDate(LocalDate consumptionDate, DailyFoodDiary dailyDiary) {
        return Stream.of(EatingTime.values())
                .flatMap(eatingTime -> dailyDiary.getMealsByEatingTime(eatingTime).stream()
                        .map(meal -> new DailyMealEntry(consumptionDate, eatingTime, meal)));
    }

    private DailyFoodDiary getOrCreateDailyDiary(LocalDate date) {
        return this.diary.computeIfAbsent(date, d -> new DailyFoodDiary());
    }

    private DailyFoodDiary getValidatedDailyDiary(LocalDate date) {
        validateDate(date);
        return this.diary.get(date);
    }

    private Optional<DailyFoodDiary> findDailyDiary(LocalDate date) {
        return Optional.ofNullable(this.diary.get(date));
    }

    private void validateDate(LocalDate date) {
        if (!this.diary.containsKey(date)) {
            throw new IllegalArgumentException(GlobalConstants.NOT_EXISTING_DATE_IN_FOOD_DIARY_MESSAGE);
        }
    }

    private record NutritionTotals(
            double calories,
            Optional<Double> protein,
            Optional<Double> carbs,
            Optional<Double> fats
    ) {

    }
}
