package com.fmi.myfitnesspal.command.user;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.user.UserId;
import com.fmi.myfitnesspal.user.UserPool;
import com.fmi.myfitnesspal.user.UserProfile;
import com.fmi.myfitnesspal.user.UserSession;

import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class SwitchUserCommand implements ExecutableCommand {

    private static final String COMMAND_NAME = "switch-user";
    private static final int ARGUMENTS_COUNT = 1;

    private final UserPool userPool;
    private final UserSession userSession;

    public SwitchUserCommand(UserPool userPool, UserSession userSession) {
        this.userPool = userPool;
        this.userSession = userSession;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);
        String username = arguments.get(0);
        UserId userId = new UserId(username);

        UserProfile targetProfile = userPool.findById(userId)
                .orElseThrow(() -> new InvalidCommandException(
                        "User " + username + " does not exist"));

        userSession.switchTo(targetProfile);
        return "You are now " + username;
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME + " <username>";
    }
}
