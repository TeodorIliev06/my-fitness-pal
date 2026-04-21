package com.fmi.myfitnesspal.command.water;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.water.WaterDiary;
import com.fmi.myfitnesspal.water.Portion;
import com.fmi.myfitnesspal.exception.InvalidCommandException;

import java.time.LocalDate;
import java.util.List;

import static com.fmi.myfitnesspal.utility.DateHelper.parseDate;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parsePortion;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class AddWaterPortionCommand implements ExecutableCommand {
    private static final String COMMAND_NAME = "add-water-portion";
    private static final int ARGUMENTS_COUNT = 2;

    private final WaterDiary waterDiary;

    public AddWaterPortionCommand(WaterDiary waterDiary) {
        this.waterDiary = waterDiary;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        LocalDate date = parseDate(arguments.get(0));
        Portion portion = parsePortion(arguments.get(1));

        waterDiary.addWater(date, portion);
        return "Water added successfully!";
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <date> <portion>";
    }
}
