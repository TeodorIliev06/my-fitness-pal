package com.fmi.myfitnesspal.persistence.water;

import com.fmi.myfitnesspal.persistence.PersistenceMapper;
import com.fmi.myfitnesspal.water.DailyWaterEntry;

public final class DailyWaterEntryDtoMapper implements PersistenceMapper<DailyWaterEntry, DailyWaterDto> {

    @Override
    public DailyWaterDto toDto(DailyWaterEntry we) {
        return new DailyWaterDto(we.consumptionDate(), we.millilitres());
    }

    @Override
    public DailyWaterEntry toEntity(DailyWaterDto dto) {
        return new DailyWaterEntry(dto.date(), dto.ml());
    }
}
