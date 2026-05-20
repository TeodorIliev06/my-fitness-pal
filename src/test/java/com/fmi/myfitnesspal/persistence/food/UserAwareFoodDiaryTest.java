package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.food.FoodDiary;
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
public final class UserAwareFoodDiaryTest {

    private static final Path USERS_ROOT = Path.of("users");
    private static final UserProfile TEST_PROFILE =
            new UserProfile(new UserId("ivan"), null);

    @Mock
    private FoodDiaryFactory foodDiaryFactoryMock;
    @Mock
    private FoodDiary guestDiaryMock;
    @Mock
    private FoodDiary switchedDiaryMock;
    @Mock
    private FoodDiary importedDiaryMock;

    private UserAwareFoodDiary inMemoryDiary;
    private UserAwareFoodDiary persistentDiary;

    @BeforeEach
    void setUp() {
        inMemoryDiary = new UserAwareFoodDiary(
                foodDiaryFactoryMock, USERS_ROOT, guestDiaryMock, false);
        persistentDiary = new UserAwareFoodDiary(
                foodDiaryFactoryMock, USERS_ROOT, guestDiaryMock, true);
    }

    @Test
    void testImportFromUserFilesInInMemoryModeReplacesActiveDiary() throws InvalidCommandException {
        when(foodDiaryFactoryMock.createIn(any())).thenReturn(switchedDiaryMock);
        when(foodDiaryFactoryMock.loadInMemoryFromFile(any())).thenReturn(importedDiaryMock);

        inMemoryDiary.onUserSwitched(TEST_PROFILE);
        inMemoryDiary.importFromUserFiles();
        inMemoryDiary.getAllDailyFoodEntries();

        verify(importedDiaryMock).getAllDailyFoodEntries();
    }

    @Test
    void testImportFromUserFilesCallsFactoryWithCorrectUserPath() throws InvalidCommandException {
        Path expectedUserPath = USERS_ROOT.resolve(TEST_PROFILE.userId().username());
        when(foodDiaryFactoryMock.createIn(any())).thenReturn(switchedDiaryMock);
        when(foodDiaryFactoryMock.loadInMemoryFromFile(expectedUserPath)).thenReturn(importedDiaryMock);

        inMemoryDiary.onUserSwitched(TEST_PROFILE);
        inMemoryDiary.importFromUserFiles();

        verify(foodDiaryFactoryMock).loadInMemoryFromFile(expectedUserPath);
    }

    @Test
    void testImportFromUserFilesInPersistentModeThrows() {
        assertThrows(InvalidCommandException.class,
                () -> persistentDiary.importFromUserFiles(),
                "importFromUserFiles must throw when the app is in persistent mode");
    }

    @Test
    void testExportToUserFilesInInMemoryModeDelegatesToFactory() throws InvalidCommandException {
        when(foodDiaryFactoryMock.createIn(any())).thenReturn(switchedDiaryMock);

        inMemoryDiary.onUserSwitched(TEST_PROFILE);
        inMemoryDiary.exportToUserFiles();

        verify(foodDiaryFactoryMock).saveToFile(any(), any());
    }

    @Test
    void testExportToUserFilesCallsFactoryWithCorrectUserPath() throws InvalidCommandException {
        Path expectedUserPath = USERS_ROOT.resolve(TEST_PROFILE.userId().username());
        when(foodDiaryFactoryMock.createIn(any())).thenReturn(switchedDiaryMock);

        inMemoryDiary.onUserSwitched(TEST_PROFILE);
        inMemoryDiary.exportToUserFiles();

        verify(foodDiaryFactoryMock).saveToFile(any(Path.class), any(FoodDiary.class));
    }

    @Test
    void testExportToUserFilesInPersistentModeThrows() {
        assertThrows(InvalidCommandException.class,
                () -> persistentDiary.exportToUserFiles(),
                "exportToUserFiles must throw when the app is in persistent mode");
    }
}
