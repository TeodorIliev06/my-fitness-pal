package com.fmi.myfitnesspal.persistence.water;

import java.time.LocalDate;

public record DailyWaterDto(
        LocalDate date,
        int ml
) {
}
