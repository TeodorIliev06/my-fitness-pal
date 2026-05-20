package com.fmi.myfitnesspal.command.transfer;

import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.persistence.DataTransferable;

import java.util.List;

public final class ExportCommand extends AbstractTransferCommand {

    private static final String COMMAND_NAME = "export";

    public ExportCommand(List<DataTransferable> dataTransferTargets) {
        super(dataTransferTargets);
    }

    @Override
    protected void transferOne(DataTransferable target) throws InvalidCommandException {
        target.exportToUserFiles();
    }

    @Override
    protected String successMessage() {
        return GlobalConstants.SUCCESSFULLY_EXPORTED_MESSAGE;
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME
                + " (saves current in-memory data to the user's files, overriding file contents)";
    }
}
