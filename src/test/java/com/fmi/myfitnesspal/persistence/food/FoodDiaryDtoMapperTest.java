package com.fmi.myfitnesspal.persistence.food;

import com.fmi.myfitnesspal.food.EatingTime;
import com.fmi.myfitnesspal.food.Food;
import com.fmi.myfitnesspal.food.DailyFoodEntry;
import com.fmi.myfitnesspal.food.FoodId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class FoodDiaryDtoMapperTest {

    private static final LocalDate CONSUMPTION_DATE = LocalDate.of(2026, 4, 1);
    private static final EatingTime EATING_TIME = EatingTime.LUNCH;
    private static final FoodId APPLE_ID = new FoodId("none", "apple");

    @Mock
    private FoodDtoMapper foodDtoMapper;

    @InjectMocks
    private FoodDiaryDtoMapper foodDiaryDtoMapper;

    @Test
    void testToDailyFoodEntryDtoMapsDateAndEatingTime() {
        Food apple = createFood();
        FoodDto appleDto = createFoodDto();
        DailyFoodEntry entry = new DailyFoodEntry(CONSUMPTION_DATE, EATING_TIME, apple);
        when(foodDtoMapper.toDto(apple)).thenReturn(appleDto);

        DailyFoodEntryDto dto = foodDiaryDtoMapper.toDailyFoodEntryDto(entry);

        assertEquals(CONSUMPTION_DATE, dto.date(),
                "toDailyFoodEntryDto should preserve the consumption date");
        assertEquals(EATING_TIME, dto.eatingTime(),
                "toDailyFoodEntryDto should preserve the eating time");
    }

    @Test
    void testToDailyFoodEntryDtoDelegatesToFoodDtoMapper() {
        Food apple = createFood();
        FoodDto appleDto = createFoodDto();
        DailyFoodEntry entry = new DailyFoodEntry(CONSUMPTION_DATE, EATING_TIME, apple);
        when(foodDtoMapper.toDto(apple)).thenReturn(appleDto);

        DailyFoodEntryDto dto = foodDiaryDtoMapper.toDailyFoodEntryDto(entry);

        assertEquals(appleDto, dto.food(),
                "toDailyFoodEntryDto should delegate food conversion to FoodDtoMapper");
    }

    @Test
    void testToDailyFoodEntryRestoresDateAndEatingTime() {
        Food apple = createFood();
        FoodDto appleDto = createFoodDto();
        DailyFoodEntryDto dto = new DailyFoodEntryDto(CONSUMPTION_DATE, EATING_TIME, appleDto);
        when(foodDtoMapper.toEntity(appleDto)).thenReturn(apple);

        DailyFoodEntry entry = foodDiaryDtoMapper.toDailyFoodEntry(dto);

        assertEquals(CONSUMPTION_DATE, entry.consumptionDate(),
                "toDailyFoodEntry should restore the consumption date from the DTO");
        assertEquals(EATING_TIME, entry.eatingTime(),
                "toDailyFoodEntry should restore the eating time from the DTO");
    }

    @Test
    void testToDailyFoodEntryDelegatesToFoodDtoMapper() {
        Food apple = createFood();
        FoodDto appleDto = createFoodDto();
        DailyFoodEntryDto dto = new DailyFoodEntryDto(CONSUMPTION_DATE, EATING_TIME, appleDto);
        when(foodDtoMapper.toEntity(appleDto)).thenReturn(apple);

        DailyFoodEntry entry = foodDiaryDtoMapper.toDailyFoodEntry(dto);

        assertEquals(apple, entry.food(),
                "toDailyFoodEntry should delegate food reconstruction to FoodDtoMapper");
    }

    @Test
    void testToDtoProducesRootDtoWithCorrectFoodList() {
        Food apple = createFood();
        DailyFoodEntry foodEntry = new DailyFoodEntry(CONSUMPTION_DATE, EATING_TIME, apple);
        FoodDto appleDto = createFoodDto();
        when(foodDtoMapper.toDto(apple)).thenReturn(appleDto);

        FoodDiaryDto diaryDto = foodDiaryDtoMapper.toDto(List.of(foodEntry));

        assertEquals(1, diaryDto.foods().size(),
                "toDto should produce one DailyFoodEntryDto per DailyFoodEntry");
    }

    @Test
    void testToDtoWithNoInputsProducesEmptyFoodList() {
        FoodDiaryDto diaryDto = foodDiaryDtoMapper.toDto(List.of());

        assertTrue(diaryDto.foods().isEmpty(),
                "toDto with no food entries should produce an empty foods list");
    }

    private Food createFood() {
        return Food.builder(APPLE_ID, 2.0, 180.0).build();
    }

    private FoodDto createFoodDto() {
        return new FoodDto("none", "apple", 2.0, 180.0, null, null, null);
    }
}
