package com.fmi.myfitnesspal.command.transfer;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.persistence.DataTransferable;

import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

abstract class AbstractTransferCommand implements ExecutableCommand {

    private static final int ARGUMENTS_COUNT = 0;

    private final List<DataTransferable> dataTransferTargets;

    protected AbstractTransferCommand(List<DataTransferable> dataTransferTargets) {
        this.dataTransferTargets = List.copyOf(dataTransferTargets);
    }

    @Override
    public final String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        transferAllTargets();

        return successMessage();
    }

    protected abstract void transferOne(DataTransferable target) throws InvalidCommandException;

    protected abstract String successMessage();

    private void transferAllTargets() throws InvalidCommandException {
        for (DataTransferable target : dataTransferTargets) {
            transferOne(target);
        }
    }
}
