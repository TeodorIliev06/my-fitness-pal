package com.fmi.myfitnesspal.command.utility;

import com.fmi.myfitnesspal.chart.PieSlice;
import com.fmi.myfitnesspal.food.DailyMealCaloriesSummary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.NutritionSummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class NutritionSliceMapperTest {

    private static final LocalDate TARGET_DATE = LocalDate.of(2025, 5, 25);
    private static final double STANDARD_CALORIES = 500.0;

    @Mock
    private NutritionSummary nutritionSummaryMock;

    @InjectMocks
    private NutritionSliceMapper mapper;

    @Test
    void testFromNutritionSummaryAlwaysReturnsThreeSlices() {
        stubAllNutrientsPresent(30.0, 100.0, 10.0);

        List<PieSlice> slices = mapper.fromNutritionSummary(nutritionSummaryMock);

        assertEquals(3, slices.size(),
                "fromNutritionSummary() must always produce exactly 3 slices");
    }

    @Test
    void testFromNutritionSummarySliceLabelsAreProteinCarbsFats() {
        stubAllNutrientsPresent(30.0, 100.0, 10.0);

        List<PieSlice> slices = mapper.fromNutritionSummary(nutritionSummaryMock);

        assertEquals("Protein", slices.get(0).label(), "First slice must be labelled Protein");
        assertEquals("Carbs",   slices.get(1).label(), "Second slice must be labelled Carbs");
        assertEquals("Fats",    slices.get(2).label(), "Third slice must be labelled Fats");
    }

    @Test
    void testFromNutritionSummaryUsesValuesWhenAllNutrientsPresent() {
        double expectedProtein = 30.0;
        double expectedCarbs = 100.0;
        double expectedFats = 10.0;
        stubAllNutrientsPresent(expectedProtein, expectedCarbs, expectedFats);

        List<PieSlice> slices = mapper.fromNutritionSummary(nutritionSummaryMock);

        assertEquals(expectedProtein, slices.get(0).value(), "Protein slice value must match the summary protein");
        assertEquals(expectedCarbs,   slices.get(1).value(), "Carbs slice value must match the summary carbs");
        assertEquals(expectedFats,    slices.get(2).value(), "Fats slice value must match the summary fats");
    }

    @Test
    void testFromNutritionSummaryFallsBackToZeroWhenAllNutrientsAbsent() {
        when(nutritionSummaryMock.protein()).thenReturn(Optional.empty());
        when(nutritionSummaryMock.carbs()).thenReturn(Optional.empty());
        when(nutritionSummaryMock.fats()).thenReturn(Optional.empty());

        List<PieSlice> slices = mapper.fromNutritionSummary(nutritionSummaryMock);

        assertEquals(0.0, slices.get(0).value(), "Protein should fall back to 0.0 when absent");
        assertEquals(0.0, slices.get(1).value(), "Carbs should fall back to 0.0 when absent");
        assertEquals(0.0, slices.get(2).value(), "Fats should fall back to 0.0 when absent");
    }

    @Test
    void testFromNutritionSummaryFallsBackToZeroForAbsentNutrientOnly() {
        when(nutritionSummaryMock.protein()).thenReturn(Optional.of(50.0));
        when(nutritionSummaryMock.carbs()).thenReturn(Optional.empty());
        when(nutritionSummaryMock.fats()).thenReturn(Optional.of(20.0));

        List<PieSlice> slices = mapper.fromNutritionSummary(nutritionSummaryMock);

        assertEquals(50.0, slices.get(0).value(), "Present protein must retain its value");
        assertEquals(0.0,  slices.get(1).value(), "Absent carbs must fall back to 0.0");
        assertEquals(20.0, slices.get(2).value(), "Present fats must retain their value");
    }

    @Test
    void testFromDailyMealCaloriesSummaryIncludesAllMealTimesWhenAllHaveCalories() {
        DailyMealCaloriesSummary summary = buildSummary(STANDARD_CALORIES);

        List<PieSlice> slices = mapper.fromDailyMealCaloriesSummary(summary);

        int expectedSize = EatingTime.values().length;
        assertEquals(expectedSize, slices.size(),
                "All eating times with calories should produce a corresponding slice");
    }

    @Test
    void testFromDailyMealCaloriesSummaryExcludesMealTimesWithZeroCalories() {
        Map<EatingTime, Double> caloriesMap = createMapWithCalories(STANDARD_CALORIES);
        caloriesMap.put(EatingTime.values()[0], 0.0);

        DailyMealCaloriesSummary summary = new DailyMealCaloriesSummary(TARGET_DATE, caloriesMap);

        List<PieSlice> slices = mapper.fromDailyMealCaloriesSummary(summary);

        int expectedSize = EatingTime.values().length - 1;
        assertEquals(expectedSize, slices.size(),
                "Meal times with 0 calories must be filtered out dynamically");
    }

    @Test
    void testFromDailyMealCaloriesSummaryReturnsEmptyListWhenAllMealTimesAreZero() {
        DailyMealCaloriesSummary summary = buildSummary(0.0);

        List<PieSlice> slices = mapper.fromDailyMealCaloriesSummary(summary);

        assertTrue(slices.isEmpty(),
                "All-zero summary must produce an empty slice list");
    }

    @Test
    void testFromDailyMealCaloriesSummarySliceLabelsAndValuesMatchDynamically() {
        Map<EatingTime, Double> caloriesMap = new EnumMap<>(EatingTime.class);

        double uniqueCalorieCounter = 100.0;
        for (EatingTime time : EatingTime.values()) {
            caloriesMap.put(time, uniqueCalorieCounter);
            uniqueCalorieCounter += 100.0;
        }

        DailyMealCaloriesSummary summary = new DailyMealCaloriesSummary(TARGET_DATE, caloriesMap);

        List<PieSlice> slices = mapper.fromDailyMealCaloriesSummary(summary);

        List<String> expectedLabels = Stream.of(EatingTime.values()).map(EatingTime::getLabel).toList();
        List<String> actualLabels = slices.stream().map(PieSlice::label).toList();

        List<Double> expectedValues = Stream.of(EatingTime.values()).map(caloriesMap::get).toList();
        List<Double> actualValues = slices.stream().map(PieSlice::value).toList();

        assertEquals(expectedLabels, actualLabels, "Slice labels must match the EatingTime labels");
        assertEquals(expectedValues, actualValues, "Slice values must match the EatingTime calories");
    }

    private void stubAllNutrientsPresent(double protein, double carbs, double fats) {
        when(nutritionSummaryMock.protein()).thenReturn(Optional.of(protein));
        when(nutritionSummaryMock.carbs()).thenReturn(Optional.of(carbs));
        when(nutritionSummaryMock.fats()).thenReturn(Optional.of(fats));
    }

    private DailyMealCaloriesSummary buildSummary(double defaultCalories) {
        return new DailyMealCaloriesSummary(TARGET_DATE, createMapWithCalories(defaultCalories));
    }

    private Map<EatingTime, Double> createMapWithCalories(double defaultCalories) {
        Map<EatingTime, Double> caloriesByEatingTime = new EnumMap<>(EatingTime.class);

        for (EatingTime time : EatingTime.values()) {
            caloriesByEatingTime.put(time, defaultCalories);
        }

        return caloriesByEatingTime;
    }
}
