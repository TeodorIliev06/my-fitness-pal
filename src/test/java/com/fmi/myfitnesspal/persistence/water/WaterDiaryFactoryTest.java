package com.fmi.myfitnesspal.persistence.water;

import com.fmi.myfitnesspal.water.InMemoryWaterDiary;
import com.fmi.myfitnesspal.water.Portion;
import com.fmi.myfitnesspal.water.WaterDiary;
import org.external.json.JsonConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public final class WaterDiaryFactoryTest {

    private static final LocalDate CONSUMPTION_DATE = LocalDate.of(2026, 3, 12);

    @TempDir
    Path tempDirectory;

    @Mock
    private JsonConverter jsonConverter;

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

        verifyNoInteractions(jsonConverter);
    }

    @Test
    void testCreateWithPersistenceReturnsWaterFileRepository() {
        WaterDiaryFactory factory = buildFactory(true);

        WaterDiary createdDiary = factory.createIn(tempDirectory);

        assertInstanceOf(WaterFileRepository.class, createdDiary,
                "create should return a WaterFileRepository when file persistence is enabled");
    }

    @Test
    void testCreateWithPersistenceAndNonexistingFileReturnsEmptyDiary() {
        WaterDiaryFactory factory = buildFactory(true);

        WaterDiary createdDiary = factory.createIn(tempDirectory);

        assertTrue(createdDiary.getAllDailyWaterEntries().isEmpty(),
                "The diary should be empty when no persisted file exists yet");
    }

    @Test
    void testCreateWithPersistenceAndNonexistingFileReturnsNoDailyWater() {
        WaterDiaryFactory factory = buildFactory(true);

        WaterDiary createdDiary = factory.createIn(tempDirectory);

        assertEquals(0, createdDiary.getDailyWater(CONSUMPTION_DATE),
                "getDailyWater should return 0 for any date when the diary starts empty");
    }

    @Test
    void testCreateWithPersistenceReturnsCorrectDiary() {
        when(jsonConverter.serialize(any())).thenReturn("[]");
        WaterDiaryFactory factory = new WaterDiaryFactory(
                true, jsonConverter, new DailyWaterEntryDtoMapper()
        );
        WaterDiary createdDiary = factory.createIn(tempDirectory);

        verifyNoInteractions(jsonConverter);
        createdDiary.addWater(CONSUMPTION_DATE, Portion.P_500);

        assertEquals(Portion.P_500.getQuantity(), createdDiary.getDailyWater(CONSUMPTION_DATE),
                "The WaterFileRepository returned by the factory should correctly track added water");
    }

    private WaterDiaryFactory buildFactory(boolean persistToFile) {
        return new WaterDiaryFactory(
                persistToFile,
                jsonConverter,
                new DailyWaterEntryDtoMapper()
        );
    }
}
