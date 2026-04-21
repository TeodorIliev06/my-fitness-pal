package com.fmi.myfitnesspal.command.water;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.exception.WaterNotLoggedException;
import com.fmi.myfitnesspal.water.WaterDiary;

import java.time.LocalDate;
import java.util.List;

import static com.fmi.myfitnesspal.utility.DateHelper.parseDate;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseInt;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class RemoveWaterCommand implements ExecutableCommand {
    private static final String COMMAND_NAME = "remove-water";
    private static final int ARGUMENTS_COUNT = 2;

    private final WaterDiary waterDiary;

    public RemoveWaterCommand(WaterDiary waterDiary) {
        this.waterDiary = waterDiary;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        LocalDate date = parseDate(arguments.get(0));
        int quantity = parseInt(arguments.get(1));

        try {
            waterDiary.removeWater(date, quantity);
        } catch (WaterNotLoggedException e) {
            return e.getMessage();
        }

        return "Water removed successfully!";
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <date> <quantity>";
    }
}
