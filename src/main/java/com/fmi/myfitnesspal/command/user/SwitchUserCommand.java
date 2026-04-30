package com.fmi.myfitnesspal.command.user;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.persistence.user.UserRegistry;
import com.fmi.myfitnesspal.registration_cli.UserRegistration;
import com.fmi.myfitnesspal.user.UserProfile;
import com.fmi.myfitnesspal.user.UserSession;

import java.util.List;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class SwitchUserCommand implements ExecutableCommand {

    private static final String COMMAND_NAME = "switch-user";
    private static final int ARGUMENTS_COUNT = 1;

    private final UserRegistry userRegistry;
    private final UserSession userSession;
    private final UserRegistration userRegistration;

    public SwitchUserCommand(UserRegistry userRegistry,
                             UserSession userSession,
                             UserRegistration userRegistration) {
        this.userRegistry = userRegistry;
        this.userSession = userSession;
        this.userRegistration = userRegistration;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);
        String username = arguments.get(0);

        UserProfile activeProfile = findProfileBy(username);
        userSession.switchTo(activeProfile);

        return "Switched to user: " + username;
    }

    private UserProfile findProfileBy(String username) {
        if (userRegistry.isRegistered(username)) {
            return userRegistry.load(username);
        }

        return registerNewProfile(username);
    }

    private UserProfile registerNewProfile(String username) {
        userRegistration.registerNewUser();
        userRegistry.register(username);

        return new UserProfile(username);
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
