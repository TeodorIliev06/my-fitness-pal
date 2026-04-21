package com.fmi.myfitnesspal.command;

import com.fmi.myfitnesspal.exception.InvalidCommandException;

import java.util.List;

public abstract class BoundedCommand implements ExecutableCommand {

    @Override
    public final String execute(List<String> arguments) throws InvalidCommandException {
        if (arguments.size() < minArgCount() || arguments.size() > maxArgCount()) {
            throw new InvalidCommandException(
                    "Expected between " + minArgCount() + " and " + maxArgCount() + " arguments");
        }

        return doExecute(arguments);
    }

    protected abstract int minArgCount();
    protected abstract int maxArgCount();
    protected abstract String doExecute(List<String> arguments) throws InvalidCommandException;
}
