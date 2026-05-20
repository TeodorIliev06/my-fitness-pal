package com.fmi.myfitnesspal.persistence.water;

import com.fmi.myfitnesspal.persistence.PersistenceStoreFactory;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;
import com.fmi.myfitnesspal.water.InMemoryWaterDiary;
import com.fmi.myfitnesspal.water.Portion;
import com.fmi.myfitnesspal.water.WaterDiary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public final class WaterDiaryFactoryTest {

    private static final LocalDate CONSUMPTION_DATE = LocalDate.of(2026, 3, 12);

    @TempDir
    Path tempDirectory;

    @Mock
    private PersistenceStoreFactory storeFactoryMock;
    @Mock
    private PersistenceStore<DailyWaterDto> waterStoreMock;

    @Test
    void testCreateWithoutPersistenceReturnInMemoryWaterDiary() {
        WaterDiaryFactory factory = buildFactory(false);

        WaterDiary createdDiary = factory.createIn(tempDirectory);

        assertInstanceOf(InMemoryWaterDiary.class, createdDiary,
                "create should return a plain InMemoryWaterDiary when file persistence is disabled");
    }

    @Test
    void testCreateWithoutPersistenceDoesNotCallJsonConverter() {
        WaterDiaryFactory factory = buildFactory(false);

        factory.createIn(tempDirectory);

        verifyNoInteractions(storeFactoryMock);
    }

    @Test
    void testCreateWithPersistenceReturnsWaterFileRepository() {
        when(storeFactoryMock.createListStore(any(), eq(DailyWaterDto.class))).thenReturn(waterStoreMock);
        when(waterStoreMock.load()).thenReturn(List.of());

        WaterDiaryFactory factory = buildFactory(true);

        WaterDiary createdDiary = factory.createIn(tempDirectory);

        assertInstanceOf(WaterFileRepository.class, createdDiary,
                "create should return a WaterFileRepository when file persistence is enabled");
    }

    @Test
    void testCreateWithPersistenceAndNonexistingFileReturnsEmptyDiary() {
        when(storeFactoryMock.createListStore(any(), eq(DailyWaterDto.class))).thenReturn(waterStoreMock);
        when(waterStoreMock.load()).thenReturn(List.of());

        WaterDiaryFactory factory = buildFactory(true);

        WaterDiary createdDiary = factory.createIn(tempDirectory);

        assertTrue(createdDiary.getAllDailyWaterEntries().isEmpty(),
                "The diary should be empty when no persisted file exists yet");
    }

    @Test
    void testCreateWithPersistenceAndNonexistingFileReturnsNoDailyWater() {
        when(storeFactoryMock.createListStore(any(), eq(DailyWaterDto.class))).thenReturn(waterStoreMock);
        when(waterStoreMock.load()).thenReturn(List.of());

        WaterDiaryFactory factory = buildFactory(true);

        WaterDiary createdDiary = factory.createIn(tempDirectory);

        assertEquals(0, createdDiary.getDailyWater(CONSUMPTION_DATE),
                "getDailyWater should return 0 for any date when the diary starts empty");
    }

    @Test
    void testCreateWithPersistenceReturnsCorrectDiary() {
        when(storeFactoryMock.createListStore(any(), eq(DailyWaterDto.class))).thenReturn(waterStoreMock);
        when(waterStoreMock.load()).thenReturn(List.of());

        WaterDiaryFactory factory = buildFactory(true);
        WaterDiary createdDiary = factory.createIn(tempDirectory);

        createdDiary.addWater(CONSUMPTION_DATE, Portion.P_500);

        assertEquals(Portion.P_500.getQuantity(), createdDiary.getDailyWater(CONSUMPTION_DATE),
                "The WaterFileRepository returned by the factory should correctly track added water");
    }

    @Test
    void testLoadInMemoryFromFileReturnsEmptyDiaryWhenFileDoesNotExist() {
        when(storeFactoryMock.createListStore(any(), eq(DailyWaterDto.class))).thenReturn(waterStoreMock);
        when(waterStoreMock.load()).thenReturn(List.of());

        WaterDiaryFactory factory = buildFactory(false);
        WaterDiary loadedDiary = factory.loadInMemoryFromFile(tempDirectory);

        assertTrue(loadedDiary.getAllDailyWaterEntries().isEmpty(),
                "loadInMemoryFromFile must return an empty diary when no file exists for the user");
    }

    @Test
    void testSaveToFileInvokesStoreWithMappedEntries() {
        when(storeFactoryMock.createListStore(any(), eq(DailyWaterDto.class))).thenReturn(waterStoreMock);

        WaterDiary sourceDiary = new InMemoryWaterDiary();
        WaterDiaryFactory factory = buildFactory(false);
        factory.saveToFile(tempDirectory, sourceDiary);

        verify(waterStoreMock).save(any());
    }

    private WaterDiaryFactory buildFactory(boolean persistToFile) {
        return new WaterDiaryFactory(
                persistToFile,
                storeFactoryMock,
                new DailyWaterEntryDtoMapper()
        );
    }
}
