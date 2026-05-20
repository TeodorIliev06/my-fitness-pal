package com.fmi.myfitnesspal.command.transfer;

import com.fmi.myfitnesspal.constants.GlobalConstants;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.persistence.DataTransferable;

import java.util.List;

public final class ImportCommand extends AbstractTransferCommand {

    private static final String COMMAND_NAME = "import";

    public ImportCommand(List<DataTransferable> dataTransferTargets) {
        super(dataTransferTargets);
    }

    @Override
    protected void transferOne(DataTransferable target) throws InvalidCommandException {
        target.importFromUserFiles();
    }

    @Override
    protected String successMessage() {
        return GlobalConstants.SUCCESSFULLY_IMPORTED_MESSAGE;
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME
                + " (loads data from the user's files into memory, overriding current in-memory state)";
    }
}
