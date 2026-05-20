package com.fmi.myfitnesspal.persistence.water;

import com.fmi.myfitnesspal.exception.WaterNotLoggedException;
import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.persistence.DataTransferable;
import com.fmi.myfitnesspal.user.UserAware;
import com.fmi.myfitnesspal.user.UserProfile;
import com.fmi.myfitnesspal.water.DailyWaterEntry;
import com.fmi.myfitnesspal.water.Portion;
import com.fmi.myfitnesspal.water.WaterDiary;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public final class UserAwareWaterDiary implements WaterDiary, UserAware, DataTransferable {

    private final WaterDiaryFactory waterDiaryFactory;
    private final Path usersRootPath;
    private final boolean persistToFile;
    private WaterDiary activeDiary;
    private Path currentUserDataPath;

    public UserAwareWaterDiary(WaterDiaryFactory waterDiaryFactory,
                               Path usersRootPath,
                               WaterDiary guestDiary,
                               boolean persistToFile) {
        this.waterDiaryFactory = waterDiaryFactory;
        this.usersRootPath = usersRootPath;
        this.activeDiary = guestDiary;
        this.persistToFile = persistToFile;
    }

    @Override
    public void onUserSwitched(UserProfile activeProfile) {
        currentUserDataPath = usersRootPath.resolve(activeProfile.userId().username());
        activeDiary = waterDiaryFactory.createIn(currentUserDataPath);
    }

    @Override
    public void importFromUserFiles() throws InvalidCommandException {
        validateInMemoryMode();
        activeDiary = waterDiaryFactory.loadInMemoryFromFile(currentUserDataPath);
    }

    @Override
    public void exportToUserFiles() throws InvalidCommandException {
        validateInMemoryMode();
        waterDiaryFactory.saveToFile(currentUserDataPath, activeDiary);
    }

    private void validateInMemoryMode() throws InvalidCommandException {
        if (persistToFile) {
            throw new InvalidCommandException(GlobalConstants.IN_MEMORY_MODE_REQUIRED_MESSAGE);
        }
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
