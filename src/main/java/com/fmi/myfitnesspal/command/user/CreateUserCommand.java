package com.fmi.myfitnesspal.command.user;

import com.fmi.myfitnesspal.command.ExecutableCommand;
import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.user.PasswordHash;
import com.fmi.myfitnesspal.user.User;
import com.fmi.myfitnesspal.user.UserId;
import com.fmi.myfitnesspal.user.UserPool;
import com.fmi.myfitnesspal.user.UserProfile;
import com.fmi.myfitnesspal.user.country.Country;
import com.fmi.myfitnesspal.user.height.Height;
import com.fmi.myfitnesspal.user.height.LengthMeasurementUnit;
import com.fmi.myfitnesspal.user.sex.Sex;
import com.fmi.myfitnesspal.user.weight.Weight;
import com.fmi.myfitnesspal.user.weight.WeightMeasurementUnit;

import java.util.List;

import static com.fmi.myfitnesspal.utility.NumberParser.parseInteger;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseSex;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseUserId;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseLengthMeasurementUnit;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseWeightMeasurementUnit;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.parseCountry;
import static com.fmi.myfitnesspal.command.utility.CommandUtilities.validateArgumentsCount;

public final class CreateUserCommand implements ExecutableCommand {

    private static final String COMMAND_NAME = "create-user";
    private static final int ARGUMENTS_COUNT = 9;

    private final UserPool userPool;

    public CreateUserCommand(UserPool userPool) {
        this.userPool = userPool;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidCommandException {
        validateArgumentsCount(arguments, ARGUMENTS_COUNT);

        UserProfile userProfile = parseUserProfile(arguments);

        if (userPool.contains(userProfile.userId())) {
            throw new InvalidCommandException(
                    "User " + userProfile.userId().username() + " already exists");
        }

        userPool.addUser(userProfile);
        return "User " + userProfile.userId().username() + " created";
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Override
    public String getHelp() {
        return "Usage: " + COMMAND_NAME
                + " <username> <password> <height-value> <height-unit>"
                + " <weight-value> <weight-unit> <age> <sex> <country>";
    }

    private UserProfile parseUserProfile(List<String> arguments) throws InvalidCommandException {
        UserId id = parseUserId(arguments.get(0));
        String rawPassword = arguments.get(1);
        int heightValue = parseInteger(arguments.get(2));
        LengthMeasurementUnit heightUnit = parseLengthMeasurementUnit(arguments.get(3));
        int weightValue = parseInteger(arguments.get(4));
        WeightMeasurementUnit weightUnit = parseWeightMeasurementUnit(arguments.get(5));
        int age = parseInteger(arguments.get(6));
        Sex sex = parseSex(arguments.get(7));
        Country country = parseCountry(arguments.get(8));

        PasswordHash passwordHash = PasswordHash.of(rawPassword);
        Height height = new Height(heightValue, heightUnit);
        Weight weight = new Weight(weightValue, weightUnit);
        User user = new User(height, weight, age, sex, country);
        return new UserProfile(id, user, passwordHash);
    }
}
