package com.fmi.myfitnesspal.persistence.water;

import com.fmi.myfitnesspal.exception.WaterNotLoggedException;
import com.fmi.myfitnesspal.persistence.user.UserRegistry;
import com.fmi.myfitnesspal.user.UserAware;
import com.fmi.myfitnesspal.user.UserProfile;
import com.fmi.myfitnesspal.water.DailyWaterEntry;
import com.fmi.myfitnesspal.water.Portion;
import com.fmi.myfitnesspal.water.WaterDiary;

import java.time.LocalDate;
import java.util.List;

public final class UserAwareWaterDiary implements WaterDiary, UserAware {

    private final WaterDiaryFactory waterDiaryFactory;
    private final UserRegistry userRegistry;
    private WaterDiary activeDiary;

    public UserAwareWaterDiary(WaterDiaryFactory waterDiaryFactory,
                               UserRegistry userRegistry,
                               WaterDiary guestDiary) {
        this.waterDiaryFactory = waterDiaryFactory;
        this.userRegistry = userRegistry;
        this.activeDiary = guestDiary;
    }

    @Override
    public void onUserSwitched(UserProfile activeProfile) {
        activeDiary = waterDiaryFactory.createIn(
                userRegistry.resolveUserDataPath(activeProfile.username())
        );
    }

    @Override
    public void addWater(LocalDate consumptionDate, Portion portion) {
        activeDiary.addWater(consumptionDate, portion);
    }

    @Override
    public void addWater(LocalDate consumptionDate, int millilitres) {
        activeDiary.addWater(consumptionDate, millilitres);
    }

    @Override
    public void removeWater(LocalDate consumptionDate, int millilitres) throws WaterNotLoggedException {
        activeDiary.removeWater(consumptionDate, millilitres);
    }

    @Override
    public void removeWater(LocalDate consumptionDate, Portion portion) throws WaterNotLoggedException {
        activeDiary.removeWater(consumptionDate, portion);
    }

    @Override
    public int getDailyWater(LocalDate consumptionDate) {
        return activeDiary.getDailyWater(consumptionDate);
    }

    @Override
    public List<DailyWaterEntry> getAllDailyWaterEntries() {
        return activeDiary.getAllDailyWaterEntries();
    }
}
