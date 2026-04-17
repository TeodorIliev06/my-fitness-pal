package com.fmi.myfitnesspal.persistence.water;

import com.fmi.myfitnesspal.exception.WaterNotLoggedException;
import com.fmi.myfitnesspal.persistence.file.PersistenceStore;
import com.fmi.myfitnesspal.water.DailyWaterEntry;
import com.fmi.myfitnesspal.water.Portion;
import com.fmi.myfitnesspal.water.WaterDiary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public final class WaterFileRepositoryTest {

    private static final LocalDate CONSUMPTION_DATE = LocalDate.of(2026, 3, 12);
    private static final int MILLILITRES = 500;

    @Mock
    private WaterDiary waterDiary;
    @Mock
    private DailyWaterEntryDtoMapper waterDtoMapper;
    @Mock
    private PersistenceStore<DailyWaterDto> persistenceStore;

    @InjectMocks
    private WaterFileRepository waterFileRepository;

    @Test
    void testLoadInitialStateWhenEmptyListAddsNothingToDiary() {
        when(persistenceStore.load()).thenReturn(List.of());

        waterFileRepository.loadInitialState();

        verify(persistenceStore).load();
        verifyNoInteractions(waterDtoMapper);
        verifyNoInteractions(waterDiary);
    }

    @Test
    void testLoadInitialStateWithSingleDtoMapsAndAddsItToDiary() {
        DailyWaterDto dto = new DailyWaterDto(CONSUMPTION_DATE, MILLILITRES);
        DailyWaterEntry entry = new DailyWaterEntry(CONSUMPTION_DATE, MILLILITRES);

        when(persistenceStore.load()).thenReturn(List.of(dto));
        when(waterDtoMapper.toEntity(dto)).thenReturn(entry);

        waterFileRepository.loadInitialState();

        verify(waterDtoMapper).toEntity(dto);
        verify(waterDiary).addWater(entry.consumptionDate(), entry.millilitres());
    }

    @Test
    void testLoadInitialStateWithMultipleDtosMapsAndAddsEachToDiary() {
        LocalDate secondDate = LocalDate.of(2024, 3, 13);
        DailyWaterDto firstDto = new DailyWaterDto(CONSUMPTION_DATE, MILLILITRES);
        DailyWaterDto secondDto = new DailyWaterDto(secondDate, 750);
        DailyWaterEntry firstEntry = new DailyWaterEntry(CONSUMPTION_DATE, MILLILITRES);
        DailyWaterEntry secondEntry = new DailyWaterEntry(secondDate, 750);

        when(persistenceStore.load()).thenReturn(List.of(firstDto, secondDto));
        when(waterDtoMapper.toEntity(firstDto)).thenReturn(firstEntry);
        when(waterDtoMapper.toEntity(secondDto)).thenReturn(secondEntry);

        waterFileRepository.loadInitialState();

        verify(waterDiary).addWater(firstEntry.consumptionDate(), firstEntry.millilitres());
        verify(waterDiary).addWater(secondEntry.consumptionDate(), secondEntry.millilitres());
    }

    @Test
    void testAddWaterByMillilitresDelegatesToDiaryAndPersistsCurrentState() {
        List<DailyWaterEntry> allEntries = List.of(new DailyWaterEntry(CONSUMPTION_DATE, MILLILITRES));
        List<DailyWaterDto> allDtos = List.of(new DailyWaterDto(CONSUMPTION_DATE, MILLILITRES));

        when(waterDiary.getAllDailyWaterEntries()).thenReturn(allEntries);
        when(waterDtoMapper.toDtos(allEntries)).thenReturn(allDtos);

        waterFileRepository.addWater(CONSUMPTION_DATE, MILLILITRES);

        verify(waterDiary).addWater(CONSUMPTION_DATE, MILLILITRES);
        verify(persistenceStore).save(allDtos);
    }

    @Test
    void testAddWaterByPortionDelegatesToDiaryAndPersistsCurrentState() {
        Portion portion = Portion.P_250;
        List<DailyWaterEntry> allEntries = List.of(new DailyWaterEntry(CONSUMPTION_DATE, portion.getQuantity()));
        List<DailyWaterDto> allDtos = List.of(new DailyWaterDto(CONSUMPTION_DATE, portion.getQuantity()));

        when(waterDiary.getAllDailyWaterEntries()).thenReturn(allEntries);
        when(waterDtoMapper.toDtos(allEntries)).thenReturn(allDtos);

        waterFileRepository.addWater(CONSUMPTION_DATE, portion);

        verify(waterDiary).addWater(CONSUMPTION_DATE, portion);
        verify(persistenceStore).save(allDtos);
    }

    @Test
    void testRemoveWaterByMillilitresDelegatesToDiaryAndPersistsCurrentState()
            throws WaterNotLoggedException {
        List<DailyWaterEntry> allEntries = List.of(new DailyWaterEntry(CONSUMPTION_DATE, MILLILITRES));
        List<DailyWaterDto> allDtos = List.of(new DailyWaterDto(CONSUMPTION_DATE, MILLILITRES));

        when(waterDiary.getAllDailyWaterEntries()).thenReturn(allEntries);
        when(waterDtoMapper.toDtos(allEntries)).thenReturn(allDtos);

        waterFileRepository.removeWater(CONSUMPTION_DATE, MILLILITRES);

        verify(waterDiary).removeWater(CONSUMPTION_DATE, MILLILITRES);
        verify(persistenceStore).save(allDtos);
    }

    @Test
    void testRemoveWaterByMillilitresWithNotLoggedWaterDoesNotPersist()
            throws WaterNotLoggedException {
        doThrow(new WaterNotLoggedException("No water logged for date"))
                .when(waterDiary).removeWater(CONSUMPTION_DATE, MILLILITRES);

        assertThrows(
                WaterNotLoggedException.class,
                () -> waterFileRepository.removeWater(CONSUMPTION_DATE, MILLILITRES),
                "removeWater should propagate WaterNotLoggedException from the inner diary"
        );

        verifyNoInteractions(persistenceStore);
    }

    @Test
    void testRemoveWaterByPortionDelegatesToDiaryAndPersistsCurrentState()
            throws WaterNotLoggedException {
        Portion portion = Portion.P_500;
        List<DailyWaterEntry> allEntries = List.of(new DailyWaterEntry(CONSUMPTION_DATE, 0));
        List<DailyWaterDto> allDtos = List.of(new DailyWaterDto(CONSUMPTION_DATE, 0));

        when(waterDiary.getAllDailyWaterEntries()).thenReturn(allEntries);
        when(waterDtoMapper.toDtos(allEntries)).thenReturn(allDtos);

        waterFileRepository.removeWater(CONSUMPTION_DATE, portion);

        verify(waterDiary).removeWater(CONSUMPTION_DATE, portion);
        verify(persistenceStore).save(allDtos);
    }

    @Test
    void testRemoveWaterByPortionWithNotLoggedWaterDoesNotPersist()
            throws WaterNotLoggedException {
        Portion portion = Portion.P_500;
        doThrow(new WaterNotLoggedException("No water logged for date"))
                .when(waterDiary).removeWater(CONSUMPTION_DATE, portion);

        assertThrows(
                WaterNotLoggedException.class,
                () -> waterFileRepository.removeWater(CONSUMPTION_DATE, portion),
                "removeWater should propagate WaterNotLoggedException from the inner diary"
        );

        verifyNoInteractions(persistenceStore);
    }

    @Test
    void testGetDailyWaterDelegatesToInnerDiaryAndReturnsResult() {
        when(waterDiary.getDailyWater(CONSUMPTION_DATE)).thenReturn(MILLILITRES);

        int result = waterFileRepository.getDailyWater(CONSUMPTION_DATE);

        assertEquals(MILLILITRES, result,
                "getDailyWater should return the millilitre count from the inner WaterDiary");
        verify(waterDiary).getDailyWater(CONSUMPTION_DATE);
    }

    @Test
    void testGetAllDailyWaterEntriesDelegatesToInnerDiaryAndReturnsAllResults() {
        List<DailyWaterEntry> expectedEntries =
                List.of(new DailyWaterEntry(CONSUMPTION_DATE, MILLILITRES));
        when(waterDiary.getAllDailyWaterEntries()).thenReturn(expectedEntries);

        List<DailyWaterEntry> actualEntries = waterFileRepository.getAllDailyWaterEntries();

        assertEquals(expectedEntries, actualEntries,
                "getAllDailyWaterEntries should return the list delegated from the inner WaterDiary");
        verify(waterDiary).getAllDailyWaterEntries();
    }
}
