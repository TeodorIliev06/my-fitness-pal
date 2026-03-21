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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class NutritionSliceMapperTest {

    private static final LocalDate TARGET_DATE = LocalDate.of(2025, 5, 25);

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

        assertEquals("Protein", slices.get(0).label());
        assertEquals("Carbs", slices.get(1).label());
        assertEquals("Fats", slices.get(2).label());
    }

    @Test
    void testFromNutritionSummaryUsesValuesWhenAllNutrientsPresent() {
        double expectedProtein = 30.0;
        double expectedCarbs = 100.0;
        double expectedFats = 10.0;
        stubAllNutrientsPresent(expectedProtein, expectedCarbs, expectedFats);

        List<PieSlice> slices = mapper.fromNutritionSummary(nutritionSummaryMock);

        assertEquals(expectedProtein, slices.get(0).value());
        assertEquals(expectedCarbs, slices.get(1).value());
        assertEquals(expectedFats, slices.get(2).value());
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

        assertEquals(50.0, slices.get(0).value());
        assertEquals(0.0,  slices.get(1).value(), "Absent carbs should fall back to 0.0");
        assertEquals(20.0, slices.get(2).value());
    }

    @Test
    void testFromDailyMealCaloriesSummaryIncludesAllMealTimesWhenAllHaveCalories() {
        DailyMealCaloriesSummary summary = buildSummary(400.0, 600.0, 700.0, 150.0);

        List<PieSlice> slices = mapper.fromDailyMealCaloriesSummary(summary);

        assertEquals(4, slices.size(),
                "All four meal times with calories should produce 4 slices");
    }

    @Test
    void testFromDailyMealCaloriesSummaryExcludesMealTimesWithZeroCalories() {
        DailyMealCaloriesSummary summary = buildSummary(400.0, 0.0, 700.0, 0.0);

        List<PieSlice> slices = mapper.fromDailyMealCaloriesSummary(summary);

        assertEquals(2, slices.size(),
                "Meal times with 0 calories must be filtered out");
    }

    @Test
    void testFromDailyMealCaloriesSummaryReturnsEmptyListWhenAllMealTimesAreZero() {
        DailyMealCaloriesSummary summary = buildSummary(0.0, 0.0, 0.0, 0.0);

        List<PieSlice> slices = mapper.fromDailyMealCaloriesSummary(summary);

        assertTrue(slices.isEmpty(),
                "All-zero summary must produce an empty slice list");
    }

    @Test
    void testFromDailyMealCaloriesSummarySliceLabelsMatchEatingTimeLabels() {
        DailyMealCaloriesSummary summary = buildSummary(400.0, 600.0, 700.0, 150.0);

        List<PieSlice> slices = mapper.fromDailyMealCaloriesSummary(summary);

        assertEquals(EatingTime.BREAKFAST.getLabel(), slices.get(0).label());
        assertEquals(EatingTime.LUNCH.getLabel(), slices.get(1).label());
        assertEquals(EatingTime.DINNER.getLabel(), slices.get(2).label());
        assertEquals(EatingTime.SNACKS.getLabel(), slices.get(3).label());
    }

    @Test
    void testFromDailyMealCaloriesSummarySliceValuesMatchSummaryCalories() {
        double breakfast = 400.0;
        double lunch = 600.0;
        double dinner = 700.0;
        double snacks = 150.0;
        DailyMealCaloriesSummary summary = buildSummary(breakfast, lunch, dinner, snacks);

        List<PieSlice> slices = mapper.fromDailyMealCaloriesSummary(summary);

        assertEquals(breakfast, slices.get(0).value());
        assertEquals(lunch, slices.get(1).value());
        assertEquals(dinner, slices.get(2).value());
        assertEquals(snacks, slices.get(3).value());
    }

    private void stubAllNutrientsPresent(double protein, double carbs, double fats) {
        when(nutritionSummaryMock.protein()).thenReturn(Optional.of(protein));
        when(nutritionSummaryMock.carbs()).thenReturn(Optional.of(carbs));
        when(nutritionSummaryMock.fats()).thenReturn(Optional.of(fats));
    }

    private DailyMealCaloriesSummary buildSummary(
            double breakfast, double lunch, double dinner, double snacks) {
        return new DailyMealCaloriesSummary(TARGET_DATE, breakfast, lunch, dinner, snacks);
    }
}
