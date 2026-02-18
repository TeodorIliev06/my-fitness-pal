package com.fmi.myfitnesspal.command.water;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.water.WaterDiary;
import com.fmi.myfitnesspal.water.Portion;
import com.fmi.myfitnesspal.exception.InvalidCommandException;

import java.time.LocalDate;
import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseDate;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parsePortion;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public class AddWaterPortionCommand implements ExecutableCommand {
    private static final String COMMAND_NAME = "add-water-portion";
    private static final int ARGUMENTS_COUNT = 2;

    private final WaterDiary diary;

    public AddWaterPortionCommand(WaterDiary diary) {
        this.diary = diary;
    }

    /**
     * Executes the command with the given arguments.
     *
     * @param arguments a list of string arguments for the command
     * @return the result of executing the command
     * @throws InvalidCommandException if the command execution fails due to invalid arguments or other reasons
     */
    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        LocalDate date = parseDate(arguments.get(0));
        Portion portion = parsePortion(arguments.get(1));

        diary.addWater(date, portion);
        return "Water added successfully!";
    }

    /**
     * Returns the name of the command.
     *
     * @return the command's name as a string
     */
    @Override
    public String name() {
        return COMMAND_NAME;
    }

    /**
     * Provides help information for the command.
     *
     * @return a string containing help information about the command
     */
    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <date> <portion>";
    }
}
