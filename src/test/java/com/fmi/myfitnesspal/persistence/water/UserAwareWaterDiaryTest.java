package com.fmi.myfitnesspal.persistence.water;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.water.WaterDiary;
import com.fmi.myfitnesspal.user.UserId;
import com.fmi.myfitnesspal.user.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class UserAwareWaterDiaryTest {

    private static final Path USERS_ROOT = Path.of("users");
    private static final UserProfile TEST_PROFILE =
            new UserProfile(new UserId("ivan"), null);

    @Mock
    private WaterDiaryFactory waterDiaryFactoryMock;
    @Mock
    private WaterDiary guestDiaryMock;
    @Mock
    private WaterDiary switchedDiaryMock;
    @Mock
    private WaterDiary importedDiaryMock;

    private UserAwareWaterDiary inMemoryDiary;
    private UserAwareWaterDiary persistentDiary;

    @BeforeEach
    void setUp() {
        inMemoryDiary = new UserAwareWaterDiary(
                waterDiaryFactoryMock, USERS_ROOT, guestDiaryMock, false);
        persistentDiary = new UserAwareWaterDiary(
                waterDiaryFactoryMock, USERS_ROOT, guestDiaryMock, true);
    }

    @Test
    void testImportFromUserFilesInInMemoryModeReplacesActiveDiary() throws InvalidCommandException {
        when(waterDiaryFactoryMock.createIn(any())).thenReturn(switchedDiaryMock);
        when(waterDiaryFactoryMock.loadInMemoryFromFile(any())).thenReturn(importedDiaryMock);

        inMemoryDiary.onUserSwitched(TEST_PROFILE);
        inMemoryDiary.importFromUserFiles();
        inMemoryDiary.getAllDailyWaterEntries();

        verify(importedDiaryMock).getAllDailyWaterEntries();
    }

    @Test
    void testImportFromUserFilesCallsFactoryWithCorrectUserPath() throws InvalidCommandException {
        Path expectedUserPath = USERS_ROOT.resolve(TEST_PROFILE.userId().username());
        when(waterDiaryFactoryMock.createIn(any())).thenReturn(switchedDiaryMock);
        when(waterDiaryFactoryMock.loadInMemoryFromFile(expectedUserPath)).thenReturn(importedDiaryMock);

        inMemoryDiary.onUserSwitched(TEST_PROFILE);
        inMemoryDiary.importFromUserFiles();

        verify(waterDiaryFactoryMock).loadInMemoryFromFile(expectedUserPath);
    }

    @Test
    void testImportFromUserFilesInPersistentModeThrows() {
        assertThrows(InvalidCommandException.class,
                () -> persistentDiary.importFromUserFiles(),
                "importFromUserFiles must throw when the app is in persistent mode");
    }

    @Test
    void testExportToUserFilesInInMemoryModeDelegatesToFactory() throws InvalidCommandException {
        when(waterDiaryFactoryMock.createIn(any())).thenReturn(switchedDiaryMock);

        inMemoryDiary.onUserSwitched(TEST_PROFILE);
        inMemoryDiary.exportToUserFiles();

        verify(waterDiaryFactoryMock).saveToFile(any(), any());
    }

    @Test
    void testExportToUserFilesCallsFactoryWithCorrectUserPath() throws InvalidCommandException {
        Path expectedUserPath = USERS_ROOT.resolve(TEST_PROFILE.userId().username());
        when(waterDiaryFactoryMock.createIn(any())).thenReturn(switchedDiaryMock);

        inMemoryDiary.onUserSwitched(TEST_PROFILE);
        inMemoryDiary.exportToUserFiles();

        verify(waterDiaryFactoryMock).saveToFile(any(Path.class), any(WaterDiary.class));
    }

    @Test
    void testExportToUserFilesInPersistentModeThrows() {
        assertThrows(InvalidCommandException.class,
                () -> persistentDiary.exportToUserFiles(),
                "exportToUserFiles must throw when the app is in persistent mode");
    }
}
