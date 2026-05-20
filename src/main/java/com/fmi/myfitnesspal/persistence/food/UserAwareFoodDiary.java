package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.DailyFoodEntry;
import com.fmi.myfitnesspal.food.DailyMealCaloriesSummary;
import com.fmi.myfitnesspal.food.DailyNutritionSummary;
import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.FoodPortion;
import com.fmi.myfitnesspal.food.FoodDiary;
import com.fmi.myfitnesspal.food.FoodId;
import com.fmi.myfitnesspal.food.WeeklyNutritionSummary;
import com.fmi.myfitnesspal.persistence.DataTransferable;
import com.fmi.myfitnesspal.user.UserAware;
import com.fmi.myfitnesspal.user.UserProfile;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public final class UserAwareFoodDiary implements FoodDiary, UserAware, DataTransferable {

    private final FoodDiaryFactory foodDiaryFactory;
    private final Path usersRootPath;
    private final boolean persistToFile;
    private FoodDiary activeDiary;
    private Path currentUserDataPath;

    public UserAwareFoodDiary(FoodDiaryFactory foodDiaryFactory,
                              Path usersRootPath,
                              FoodDiary guestDiary,
                              boolean persistToFile) {
        this.foodDiaryFactory = foodDiaryFactory;
        this.usersRootPath = usersRootPath;
        this.activeDiary = guestDiary;
        this.persistToFile = persistToFile;
    }

    @Override
    public void onUserSwitched(UserProfile activeProfile) {
        currentUserDataPath = usersRootPath.resolve(activeProfile.userId().username());
        activeDiary = foodDiaryFactory.createIn(currentUserDataPath);
    }

    @Override
    public void importFromUserFiles() throws InvalidCommandException {
        validateInMemoryMode();
        activeDiary = foodDiaryFactory.loadInMemoryFromFile(currentUserDataPath);
    }

    @Override
    public void exportToUserFiles() throws InvalidCommandException {
        validateInMemoryMode();
        foodDiaryFactory.saveToFile(currentUserDataPath, activeDiary);
    }

    private void validateInMemoryMode() throws InvalidCommandException {
        if (persistToFile) {
            throw new InvalidCommandException(GlobalConstants.IN_MEMORY_MODE_REQUIRED_MESSAGE);
        }
    }

    @Override
    public void addFood(LocalDate consumptionDate, EatingTime eatingTime,
                        Food food, double numberOfServings) {
        activeDiary.addFood(consumptionDate, eatingTime, food, numberOfServings);
    }

    @Override
    public void addFoodPortions(LocalDate date, EatingTime eatingTime, List<FoodPortion> foodPortions) {
        activeDiary.addFoodPortions(date, eatingTime, foodPortions);
    }

    @Override
    public void removeFood(LocalDate consumptionDate, EatingTime eatingTime, FoodId foodId) {
        activeDiary.removeFood(consumptionDate, eatingTime, foodId);
    }

    @Override
    public List<Food> getFoodsByDateAndEatingTime(LocalDate consumptionDate, EatingTime eatingTime) {
        return activeDiary.getFoodsByDateAndEatingTime(consumptionDate, eatingTime);
    }

    @Override
    public List<Food> getFoodsByWeekNumber(int weekNumber, int year) {
        return activeDiary.getFoodsByWeekNumber(weekNumber, year);
    }

    @Override
    public List<Food> getAllFoodsByDate(LocalDate consumptionDate) {
        return activeDiary.getAllFoodsByDate(consumptionDate);
    }

    @Override
    public List<Food> getAllFoodsByDateAndEatingTime(LocalDate consumptionDate, EatingTime eatingTime) {
        return activeDiary.getAllFoodsByDateAndEatingTime(consumptionDate, eatingTime);
    }

    @Override
    public WeeklyNutritionSummary getWeeklyNutritionSummary(int weekNumber, int year) {
        return activeDiary.getWeeklyNutritionSummary(weekNumber, year);
    }

    @Override
    public WeeklyNutritionSummary getWeeklyNutritionSummary(LocalDate consumptionDate) {
        return activeDiary.getWeeklyNutritionSummary(consumptionDate);
    }

    @Override
    public DailyNutritionSummary getDailyNutritionSummary(LocalDate consumptionDate) {
        return activeDiary.getDailyNutritionSummary(consumptionDate);
    }

    @Override
    public List<DailyNutritionSummary> getDailyNutritionSummariesForWeek(LocalDate targetDate) {
        return activeDiary.getDailyNutritionSummariesForWeek(targetDate);
    }

    @Override
    public DailyMealCaloriesSummary getDailyMealCaloriesSummary(LocalDate consumptionDate) {
        return activeDiary.getDailyMealCaloriesSummary(consumptionDate);
    }

    @Override
    public List<DailyFoodEntry> getAllDailyFoodEntries() {
        return activeDiary.getAllDailyFoodEntries();
    }
}
