package com.fmi.myfitnesspal.command.user;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.registration_cli.UserRegistration;
import com.fmi.myfitnesspal.user.UserHolder;

import java.util.List;
import java.util.Scanner;

import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class RegisterNewUserCommand implements ExecutableCommand {

    private static final String COMMAND_NAME = "register-new-user";
    private static final int ARGUMENTS_COUNT = 0;

    private final Scanner scanner;

    private UserHolder userHolder;

    private UserRegistration userRegistration;



    public RegisterNewUserCommand(Scanner scanner, UserHolder userHolder, UserRegistration userRegistration) {
        this.scanner = scanner;
        this.userHolder = userHolder;
        this.userRegistration = userRegistration;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        UserRegistration userRegistration = new UserRegistration(this.scanner);
        this.userHolder.setUser(userRegistration.registerNewUser());

        return "User registered successfully.";
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME;
    }
}
