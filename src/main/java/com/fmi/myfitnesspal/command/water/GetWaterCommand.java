package com.fmi.myfitnesspal.command.water;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.water.WaterDiary;
import com.fmi.myfitnesspal.exception.InvalidCommandException;

import java.time.LocalDate;
import java.util.List;

import static com.fmi.myfitnesspal.utility.DateHelper.parseDate;
import static com.fmi.myfitnesspal.utility.DateHelper.DATE_FORMATTER;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class GetWaterCommand implements ExecutableCommand {
    private static final String COMMAND_NAME = "get-water";
    private static final int ARGUMENTS_COUNT = 1;

    private final WaterDiary waterDiary;

    public GetWaterCommand(WaterDiary waterDiary) {
        this.waterDiary = waterDiary;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        LocalDate date = parseDate(arguments.get(0));

        int water = waterDiary.getDailyWater(date);
        return String.format("Water found for %s: %dml", date.format(DATE_FORMATTER), water);
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <date>";
    }
}
