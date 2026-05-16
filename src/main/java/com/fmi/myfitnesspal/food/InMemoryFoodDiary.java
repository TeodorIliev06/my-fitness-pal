package com.fmi.myfitnesspal.food;

import com.fmi.myfitnesspal.constants.GlobalConstants;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.fmi.myfitnesspal.utility.DateHelper.getYearFrom;
import static com.fmi.myfitnesspal.utility.DateHelper.getWeekNumberFrom;

public final class InMemoryFoodDiary implements FoodDiary {

    private static final int DAYS_IN_WEEK = 7;

    private final Map<LocalDate, DailyFoodDiary> diary;

    public InMemoryFoodDiary() {
        this.diary = new HashMap<>();
    }

    @Override
    public void removeFood(LocalDate date, EatingTime eatingTime, FoodId foodId) {
        getValidatedDailyDiary(date).removeFood(eatingTime, foodId);
    }

    @Override
    public void addFood(LocalDate date, EatingTime eatingTime, Food food, double numberOfServings) {
        getOrCreateDailyDiary(date).addFood(eatingTime, food, numberOfServings);
    }

    @Override
    public void addFoodPortions(LocalDate date, EatingTime eatingTime, List<FoodPortion> foodPortions) {
        foodPortions.forEach(portion -> addFood(date, eatingTime, portion.food(), portion.servingsUsed()));
    }

    @Override
    public List<Food> getFoodsByDateAndEatingTime(LocalDate date, EatingTime eatingTime) {
        return getValidatedDailyDiary(date).getFoodsByEatingTime(eatingTime);
    }

    @Override
    public List<Food> getFoodsByWeekNumber(int weekNumber, int year) {
        return diary.keySet().stream()
                .filter(date -> getWeekNumberFrom(date) == weekNumber
                        && getYearFrom(date) == year)
                .flatMap(date -> diary.get(date).getAllFoods().stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<Food> getAllFoodsByDate(LocalDate date) {
        return findDailyDiary(date)
                .map(DailyFoodDiary::getAllFoods)
                .orElse(List.of());
    }

    @Override
    public List<Food> getAllFoodsByDateAndEatingTime(LocalDate date, EatingTime eatingTime) {
        return findDailyDiary(date)
                .map(dailyDiary -> dailyDiary.getFoodsByEatingTime(eatingTime))
                .orElse(List.of());
    }

    @Override
    public WeeklyNutritionSummary getWeeklyNutritionSummary(int weekNumber, int year) {
        NutritionTotals totals = sumNutritionFrom(getFoodsByWeekNumber(weekNumber, year));
        return new WeeklyNutritionSummary(
                weekNumber,
                year,
                totals.calories(),
                totals.protein(),
                totals.carbs(),
                totals.fats()
        );
    }

    @Override
    public WeeklyNutritionSummary getWeeklyNutritionSummary(LocalDate date) {
        int weekNumber = getWeekNumberFrom(date);
        int year = getYearFrom(date);

        return getWeeklyNutritionSummary(weekNumber, year);
    }

    @Override
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

    @Override
    public List<DailyNutritionSummary> getDailyNutritionSummariesForWeek(LocalDate targetDate) {
        LocalDate monday = targetDate.with(DayOfWeek.MONDAY);
        return IntStream.range(0, DAYS_IN_WEEK)
                .mapToObj(monday::plusDays)
                .map(this::getDailyNutritionSummary)
                .toList();
    }

    @Override
    public DailyMealCaloriesSummary getDailyMealCaloriesSummary(LocalDate date) {
        Map<EatingTime, Double> caloriesByEatingTime = new EnumMap<>(EatingTime.class);

        for (EatingTime eatingTime : EatingTime.values()) {
            caloriesByEatingTime.put(eatingTime, sumCaloriesForMealTime(date, eatingTime));
        }

        return new DailyMealCaloriesSummary(date, caloriesByEatingTime);
    }

    @Override
    public List<DailyFoodEntry> getAllDailyFoodEntries() {
        return diary.entrySet().stream()
                .flatMap(entry -> foodEntriesForDate(entry.getKey(), entry.getValue()))
                .toList();
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

    private Stream<DailyFoodEntry> foodEntriesForDate(LocalDate consumptionDate, DailyFoodDiary dailyDiary) {
        return Stream.of(EatingTime.values())
                .flatMap(eatingTime -> dailyDiary.getFoodsByEatingTime(eatingTime).stream()
                        .map(food -> new DailyFoodEntry(consumptionDate, eatingTime, food)));
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
