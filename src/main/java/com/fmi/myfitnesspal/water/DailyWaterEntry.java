package com.fmi.myfitnesspal.water;

import java.time.LocalDate;

public record DailyWaterEntry(LocalDate consumptionDate, int millilitres) {
}
